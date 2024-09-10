package project.shared;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Queue;

import project.utils.DebugUtils;

public class ConnectionHandler {
	public interface IQueueItem {
		public INetworkMessage getMessage();
	}
	
	public static class CompoundMessage implements IQueueItem {
		private final Queue<INetworkMessage> messages;
		
		public CompoundMessage(Queue<INetworkMessage> messages) {
			this.messages = messages;
		}
		
		@Override
		public INetworkMessage getMessage() {
			return this.messages.poll();
		}
	}
	
	private class SingleMessage implements IQueueItem {
		private final INetworkMessage message;
		
		private SingleMessage(INetworkMessage message) {
			this.message = message;
		}
		
		@Override
		public INetworkMessage getMessage() {
			return message;
		}
	}
	
	
	private INetworkStandard networkStandard;
	private Socket socket;
	private DataInputStream from;
	private DataOutputStream to;
	//private Queue<INetworkMessage> inboundQueue;
	//private Queue<INetworkMessage> outboundQueue;
	private Queue<INetworkMessage> inboundQueue;	// Inbound messages are always treated separate
	private Queue<IQueueItem> outboundQueue; // Outbound messages can be merged via CompoundMessage for faster add 
	private int expectedSize;
	
	public ConnectionHandler(
		INetworkStandard networkStandard,
		Socket socket, 
		Queue<INetworkMessage> inboundQueue, 
		Queue<IQueueItem> outboundQueue
		//Queue<INetworkMessage> inboundQueue, 
		//Queue<INetworkMessage> outboundQueue
	) {
		this.networkStandard = networkStandard;
		this.socket = socket;
		this.from = null;
		this.to = null;
		this.inboundQueue = inboundQueue;
		this.outboundQueue = outboundQueue;
		this.resetExpectedSize();
	}
	
	
	public void initialize() throws IOException {
		this.from = new DataInputStream(this.socket.getInputStream());
		this.to = new DataOutputStream(this.socket.getOutputStream());
		this.resetExpectedSize();
	}
	
	public void receiveMessages() throws IOException {
		DataInputStream from = this.from;
		INetworkStandard ns = this.networkStandard;

			// Continue until there are no more complete messages left to read
		while( true ) {
			if( this.expectedSize == -1 ) {
				this.expectedSize = ns.readSizeIfAvailable(from);
				
				if( this.expectedSize == -1 ) {
					break;
				}
			}
			
			if( from.available() < this.expectedSize ) {
				break;
			}
			
			ByteBuffer messageBuffer = ByteBuffer.wrap(from.readNBytes(this.expectedSize));
			int head = ns.getMessageHead(messageBuffer);
			INetworkMessage template = ns.getTemplate(head);
			
			if( template == null ) {
				DebugUtils.log(
					this, 
					"WARNING: Received a malformed message!", 
					"Expected size: " + this.expectedSize, 
					"Head: " + head
				);
			} else {
				this.inboundQueue.add(template.deserialize(this.networkStandard, messageBuffer));
			}
			
			this.resetExpectedSize();
		}
	}
	
	public void sendMessages() throws IOException {
		DataOutputStream to = this.to;
		INetworkStandard ns = this.networkStandard;
		IQueueItem item;
		
		while( (item = this.outboundQueue.poll()) != null ) {
			INetworkMessage message;
			while( (message = item.getMessage()) != null ) {
				byte[] bytes = message.serialize(ns).array();
				int size = bytes.length + ns.sizeOfHead();
				
				ns.writeSize(to, size);
				ns.writeHead(to, message.getHead());
				to.write(bytes);
			}
		}
		to.flush();
	}
	
	public void queueMessage(INetworkMessage message) {
		this.outboundQueue.add(new SingleMessage(message));
	}
	
	public void queueMessage(Queue<INetworkMessage> compoundMessage) {
		this.outboundQueue.add(new CompoundMessage(compoundMessage));
	}
	
	public INetworkMessage pollMessage() {
		return this.inboundQueue.poll();
	}
	
	public void close() throws IOException {
		this.closeIfNotNull(this.from);
		this.closeIfNotNull(this.to);
		this.closeIfNotNull(this.socket);
	}
	
	private void closeIfNotNull(Closeable closeable) throws IOException {
		if( closeable != null ) {
			closeable.close();
		}
	}
	
	public void dispose() {
		this.from = null;
		this.to = null;
		this.socket = null;
	}
	
	//public Queue<INetworkMessage> getInboundMessages() {
	/*public Queue<INetworkMessage> getInboundMessages() {
		return this.inboundQueue;
	}*/
	
	//public Queue<INetworkMessage> getOutboundMessages() {
	/*public Queue<IQueueItem> getOutboundMessage() {
		return this.outboundQueue;
	}*/
	
	private void resetExpectedSize() {
		this.expectedSize = -1;
	}
}

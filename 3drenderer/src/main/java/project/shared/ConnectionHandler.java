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
	private INetworkStandard networkStandard;
	private Socket socket;
	private DataInputStream from;
	private DataOutputStream to;
	private Queue<ANetworkMessage> inboundQueue;
	private Queue<ANetworkMessage> outboundQueue;
	private int expectedSize;
	
	public ConnectionHandler(
		INetworkStandard networkStandard,
		Socket socket, 
		Queue<ANetworkMessage> inboundQueue, 
		Queue<ANetworkMessage> outboundQueue
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

		while( true ) {
			if( this.expectedSize == -1 ) {
				if( (this.expectedSize = ns.readSizeIfAvailable(from)) == -1 ) {
					break;
				}
			}
			
			if( from.available() < this.expectedSize ) {
				break;
			}
			
			ByteBuffer messageBuffer = ByteBuffer.wrap(from.readNBytes(this.expectedSize));
			int head = ns.getMessageHead(messageBuffer);
			ANetworkMessage template = ns.getTemplate(head);
			
			if( template == null ) {
				DebugUtils.log(this, "WARNING: Received a malformed message!");
			} else {
				this.inboundQueue.add(template.deserialize(messageBuffer));
			}
			
			this.resetExpectedSize();
		}
	}
	
	public void sendMessages() throws IOException {
		DataOutputStream to = this.to;
		INetworkStandard ns = this.networkStandard;
		ANetworkMessage message;
		
		while( (message = this.outboundQueue.poll()) != null ) {
			byte[] bytes = message.serialize(ns).array();
			int size = bytes.length + ns.sizeOfHead();
			message.resolve();
			
			ns.writeSize(to, size);
			ns.writeHead(to, message.getHead());
			to.write(message.serialize(ns).array());
		}
		
		to.flush();
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
	
	public Queue<ANetworkMessage> getInboundMessages() {
		return this.inboundQueue;
	}
	
	public Queue<ANetworkMessage> getOutboundMessages() {
		return this.outboundQueue;
	}
	
	private void resetExpectedSize() {
		this.expectedSize = -1;
	}
}

package project.core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import project.shared.INetworkMessage;
import project.shared.INetworkingStrategy;
import project.shared.MMessage;
import project.shared.NetUtils;
import project.shared.Networker;
import project.utils.DebugUtils;

public class Client implements INetworkingStrategy {
	private final String address;
	private final int port;
	private final Queue<INetworkMessage> inboundMessages;
	private final Queue<INetworkMessage> outboundMessages;
	private final INetworkMessage[] messageDeserializers;
	
	private Socket clientSocket;
	private DataInputStream from;
	private DataOutputStream to;
	private Networker networker;
	
	public Client(String address, int port) {
		this.address = address;
		this.port = port;
		this.inboundMessages = new ConcurrentLinkedQueue<>();
		this.outboundMessages = new ConcurrentLinkedQueue<>();
		
		this.messageDeserializers = new INetworkMessage[1];
		this.messageDeserializers[MMessage.MSG_MESSAGE] = new MMessage();
		
		this.clientSocket = null;
		this.from = null;
		this.to = null;
		this.networker = null;
	}
	
	
	@Override
	public void startUp() {
		try {
			this.clientSocket = new Socket(this.address, this.port);
			this.from = new DataInputStream(this.clientSocket.getInputStream());
			this.to = new DataOutputStream(this.clientSocket.getOutputStream());
		} catch( Exception e ) {
			DebugUtils.log(
				this, 
				"Unable to create the client socket or unable to connect to host!", 
				"IP: " + this.address + ", PORT: " + this.port
			);
			e.printStackTrace();
		}
	}

	@Override
	public void loop() {
		try {
			DataInputStream from = this.from;
			while( from.available() >= 1 ) {
				int size = NetUtils.readSize(from);
				int header = NetUtils.readHeader(from);
				byte[] bytes = from.readNBytes(size);
				ByteBuffer buffer = ByteBuffer.wrap(bytes);
				this.inboundMessages.add(this.messageDeserializers[header].deserialize(buffer));
			}
		} catch( Exception e ) {
			DebugUtils.log(this, "ERROR: Unable to read a message from the server!");
			e.printStackTrace();
			this.networker.shutdown();
		}
	}

	@Override
	public void close() {
		
			// Close socket when done "networking"
		if( this.clientSocket != null ) {
			try {
				this.from.close();
				this.to.close();
				this.clientSocket.close();
			} catch( Exception e ) {
				DebugUtils.log(this, "ERROR: Unable to close socket!");
				e.printStackTrace();
			}
		}
	}

	@Override
	public void dispose() {
		this.clientSocket = null;
		this.from = null;
		this.to = null;
	}
	
	public void handleInboundMessages() {
		try {
			INetworkMessage message;
			while( (message = this.inboundMessages.poll()) != null ) {
				message.resolve();
			}
		} catch( Exception e ) {
			DebugUtils.log(this, "ERROR: Unable to read messages from the server!");
			this.networker.shutdown();
		}
	}
	
	public void handleOutboundMessages() {
		try {
			INetworkMessage message;
			while( (message = this.outboundMessages.poll()) != null ) {
				this.to.write(message.serialize());
			}
			this.to.flush();
		} catch( Exception e ) {
			DebugUtils.log(this, "ERROR: Unable to write a message to the server!");
			this.networker.shutdown();
		}
	}
	
	public void queueMessage(INetworkMessage message) {
		this.outboundMessages.add(message);
	}
	
	public void assignToNetworker(Networker networker) {
		this.networker = networker;
	}
}

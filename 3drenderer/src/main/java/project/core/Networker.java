package project.core;

import java.io.IOException;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import project.shared.ConnectionHandler;
import project.shared.INetworkMessage;
import project.shared.INetworkStandard;
import project.shared.logger.Logger;

public class Networker {
	
	/************************* Session-class **************************/
	
	private class Session implements Runnable {

		@Override
		public void run() {
			Networker.this.connect();
			Networker.this.listenMessages();
			Networker.this.disconnect();
		}
	}
	
	
	/************************* Networker-class **************************/
	
	private final INetworkStandard networkStandard;
	
	private volatile boolean isSessionAuthorized;
	
	private String address;
	private int port;
	private Socket clientSocket;
	private ConnectionHandler connectionHandler;
	private Queue<ConnectionHandler.IQueueItem> outboundMessages;
	private Queue<INetworkMessage> inboundMessages;
	private Session currentSession;
	
	public Networker(INetworkStandard networkStandard) {
		this.networkStandard = networkStandard;
		this.isSessionAuthorized = false;
		
		this.address = null;
		this.port = -1;
		this.clientSocket = null;
		this.connectionHandler = null;
		this.outboundMessages = null;
		this.inboundMessages = null;
		this.currentSession = null;
	}
	
	
	public void launchSession(String address, int port) {
		this.address = address;
		this.port = port;
		this.outboundMessages = new ConcurrentLinkedQueue<>();
		this.inboundMessages = new ConcurrentLinkedQueue<>();
		this.currentSession = new Session();
		
		Thread sessionThread = new Thread(this.currentSession);
		sessionThread.start();
	}
	
	public void abortSession() {
		this.isSessionAuthorized = false;
	}
	
	private void connect() {
		try {
			Logger.get().info(this, "Connecting to " + this.address + ":" + this.port + "...");
			this.clientSocket = new Socket(this.address, this.port);
			this.connectionHandler = new ConnectionHandler(
				this.networkStandard, this.clientSocket, this.inboundMessages, this.outboundMessages
			);
			this.connectionHandler.initialize();
			this.isSessionAuthorized = true;
			Logger.get().info(this, "Connection established to " + this.address + ":" + this.port + "!");
		} catch( IOException e ) {
			Logger.get().error(
				this, 
				"Unable to connect to the server!", 
				"IP: " + this.address + ", PORT: " + this.port
			);
			e.printStackTrace();
		}
	}
	
	private void disconnect() {
		try {
			this.clientSocket.close();
			this.connectionHandler.close();
		} catch( IOException e ) {
			Logger.get().error(this, "Unable to close the connection!");
			e.printStackTrace();
		}
		
		this.connectionHandler.dispose();
		this.outboundMessages = null;
		this.inboundMessages = null;
	}
	
	private void listenMessages() {
		try {
			while( this.isSessionAuthorized ) {
				Networker.this.connectionHandler.receiveMessages();
			}
		} catch( IOException e ) {
			Logger.get().error(this, "Unable to receive messages from the server!");
			e.printStackTrace();
		}
	}
	
	public void handleInboundMessages() {
		INetworkMessage message;
		while( (message = this.inboundMessages.poll()) != null ) {
			message.resolve();
		}
	}
	
	public void handleOutboundMessages() {
		try {
			this.connectionHandler.sendMessages();
		} catch( IOException e ) {
			Logger.get().error(this, "Unable to send messages to the server!");
			e.printStackTrace();
		}
	}
	
	public void queueMessage(INetworkMessage message) {
		this.connectionHandler.queueMessage(message);
	}
}

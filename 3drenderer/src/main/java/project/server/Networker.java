package project.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import project.shared.ANetworkMessage;
import project.shared.ConnectionHandler;
import project.shared.NetworkStandard;
import project.utils.DebugUtils;


public class Networker implements Runnable {

	private final NetworkStandard networkStandard;
	
	private volatile boolean isSessionAuthorized;
	
	private int port;
	private ServerSocket serverSocket;
	private Map<ConnectionHandler, Boolean> connections;
	
	public Networker(NetworkStandard networkStandard, int port) {
		this.networkStandard = networkStandard;
		this.port = port;
		this.isSessionAuthorized = false;
		this.serverSocket = null;
		this.connections = new ConcurrentHashMap<>();
	}

	
	@Override
	public void run() {
		this.startUp();
		DebugUtils.log(this, "JOHNEngine server started!");
		this.listenConnections();
		this.closeDown();
	}
	
	private void startUp() {
		try {
			this.serverSocket = new ServerSocket(this.port);
			this.isSessionAuthorized = true;
		} catch( IOException e ) {
			DebugUtils.log(this, "ERROR: Unable to open server socket!", "PORT: " + this.port);
			e.printStackTrace();
		}
	}
	
	private void listenConnections() {
		DebugUtils.log(this, "Listening to port " + this.port + "...");
		while( this.isSessionAuthorized ) {
			try {
				Socket accepted = this.serverSocket.accept();
				if( accepted != null ) {
					ConnectionHandler handler = new ConnectionHandler(
						this.networkStandard,
						accepted,
						new ConcurrentLinkedQueue<>(),
						new ConcurrentLinkedQueue<>()
					);
					handler.initialize();
					this.connections.put(handler, true);
				}
			} catch( IOException e ) {
				DebugUtils.log(this, "ERROR: Unable to accept a connection!");
				e.printStackTrace();
			}
		}
	}
	
	private void closeDown() {
		for( ConnectionHandler handler : this.connections.keySet() ) {
			try {
				handler.close();
			} catch( IOException e ) {
				DebugUtils.log(this, "ERROR: Unable to close a connection handler!");
				e.printStackTrace();
			}
		}
		
		this.connections.clear();
	}
	
	public void handleInboundMessages() {
		for( ConnectionHandler handler : this.connections.keySet() ) {
			try {
				handler.receiveMessages();
			} catch( IOException e ) {
				DebugUtils.log(this, "ERROR: Unable to receive messages from a client!");
				e.printStackTrace();
			}
			
			ANetworkMessage message;
			Queue<ANetworkMessage> messages = handler.getInboundMessages();
			while( (message = messages.poll()) != null ) {
				message.resolve();
			}
		}
	}
	
	public void shutdown() {
		this.isSessionAuthorized = false;
		
		try {
			this.serverSocket.close();	// Server socket must be closed here to break the accept() block
		} catch( IOException e ) {
			DebugUtils.log(this, "ERROR: Unable to close the server socket!");
			e.printStackTrace();
		}
	}
}

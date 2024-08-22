package project.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import project.shared.INetworkingStrategy;
import project.shared.Networker;

public class Server implements INetworkingStrategy {
	private final int port;
	
	private Networker networker;
	private ServerSocket serverSocket;
	private Set<ConnectionHandler> connections;

	public Server(int port) {
		this.port = port;
		this.networker = null;
		this.connections = new HashSet<>();
		this.serverSocket = null;
	}
	
	
	@Override
	public void startUp() {
		int port = this.port;
		
		try {
			this.serverSocket = new ServerSocket(port);
			System.out.println("Listening to port " + port + "...");
		} catch( Exception e ) {
			System.out.println("Failed to create the server socket on port " + port + "!");
			System.out.println("Server shutting down...");
		}
	}
	
	@Override
	public void loop() {
		try {
			this.onNewConnection(this.serverSocket.accept());
		} catch( Exception e ) {
			System.out.println("ERROR: Failed to accept a connection on the server socket!");
			this.networker.shutdown();
		}
	}
	
	private void onNewConnection(Socket clientSocket) {
		System.out.println(
			"New connection from ip " + 
			clientSocket.getRemoteSocketAddress().toString() + 
			"(" + clientSocket.getLocalPort() + ")"
		);
		
		ConnectionHandler handler = new ConnectionHandler(clientSocket);
		this.connections.add(handler);
		
		Thread worker = new Thread(handler);
		worker.start();
	}

	@Override
	public void close() {
		try {
				// Close the server socket as well as all handlers
			/*for( ConnectionHandler handler : this.connections ) {
				handler.close();
			}*/
			
			this.serverSocket.close();
		} catch( Exception e ) {
			System.out.println("ERROR: Unable to close the server socket!");
		}
	}

	@Override
	public void dispose() {
		
			// Dispose all resources and handlers
		/*for( ConnectionHandler handler : this.connections ) {
			handler.dispose();
		}*/
		
		this.serverSocket = null;
		this.connections = null;
	}
	
	public void assignToNetworker(Networker networker) {
		this.networker = networker;
	}
}

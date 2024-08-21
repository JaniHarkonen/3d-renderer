package project.serversketch;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Networker implements Runnable {

	private Server server;
	private Set<ConnectionHandler> connections;
	
	public Networker(Server server) {
		this.server = server;
		this.connections = new HashSet<>();
	}
	
	
	@Override
	public void run() {
		int port = this.server.getPort();
		
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			System.out.println("Listening to port " + port + "...");
			
			while( true ) {
				this.onNewConnection(serverSocket.accept());
			}
			
		} catch( Exception e ) {
			System.out.println("Failed to create the server socket on port " + port + "!");
			System.out.println("Server shutting down...");
		}
	}
	
	private void onNewConnection(Socket clientSocket) {
		System.out.println("New connection from ip " + clientSocket.getRemoteSocketAddress().toString() + "(" + clientSocket.getLocalPort() + ")");
		
		ConnectionHandler handler = new ConnectionHandler(clientSocket);
		this.connections.add(handler);
		
		Thread worker = new Thread(handler);
		worker.start();
	}
}


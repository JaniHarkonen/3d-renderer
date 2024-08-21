package project.serversketch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ConnectionHandler implements Runnable {
	private final Socket socket;
	private BufferedWriter to;
	private BufferedReader from;
	
	public ConnectionHandler(Socket socket) {
		this.socket = socket;
		this.to = null;
		this.from = null;
	}

	
	@Override
	public void run() {
		try {
			this.to = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.from = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			long time = System.nanoTime();
			while( this.socket.isConnected() ) {
				if( System.nanoTime() - time >= 1000000000 ) {
					System.out.println("handling connection");
					time = System.nanoTime();
				}
			}
			
			System.out.println("User disconnected!");
			this.socket.close();
		} catch( Exception e ) {
			System.out.println("ERROR: Unable to create either the output writer or input reader for socket " + this.socket);
		}
	}
}

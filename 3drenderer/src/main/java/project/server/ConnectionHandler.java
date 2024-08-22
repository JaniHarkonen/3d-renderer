package project.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ConnectionHandler implements Runnable {
	private final Socket socket;
	private DataInputStream from;
	private DataOutputStream to;
	
	public ConnectionHandler(Socket socket) {
		this.socket = socket;
		this.to = null;
		this.from = null;
	}

	
	@Override
	public void run() {
		Socket socket = this.socket;
		try {
			this.from = new DataInputStream(socket.getInputStream());
			this.to = new DataOutputStream(socket.getOutputStream());
			
			while( socket.isConnected() ) {
				int size = this.from.read();
				
				if( size >= 0 ) {
					if( this.from.read() == 2 ) {
						byte[] messageBytes = new byte[size - 1];
						this.from.read(messageBytes);
						System.out.println(new String(messageBytes));
					}
				}
			}
			
			System.out.println("User disconnected!");
			this.socket.close();
			
		} catch( Exception e ) {
			System.out.println("ERROR: Unable to create either the output writer or input reader for socket " + this.socket);
		}
	}
}

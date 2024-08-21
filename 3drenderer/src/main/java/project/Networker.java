package project;

import java.net.Socket;

import project.utils.DebugUtils;

public final class Networker implements Runnable {

	private final String address;
	private final int port;
	
	private Socket clientSocket;
	
	public Networker(String address, int port) {
		this.address = address;
		this.port = port;
		this.clientSocket = null;
	}

	
	@Override
	public void run() {
		try {
			this.clientSocket = new Socket(this.address, this.port);
			
		} catch( Exception e ) {
			DebugUtils.log(
				this, 
				"ERROR: Unable to create a socket!", 
				"IP: '" + this.address + ", PORT: '" + this.port + "'"
			);
			e.printStackTrace();
		}
		
			// Close socket when done "networking"
		if( this.clientSocket != null ) {
			try {
				this.clientSocket.close();
			} catch( Exception e ) {
				DebugUtils.log(this, "ERROR: Unable to close socket!");
				e.printStackTrace();
			}
		}
	}
}

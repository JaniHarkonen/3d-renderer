package project.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

import project.shared.INetworkMessage;
import project.shared.MMessage;
import project.shared.NetUtils;

public class ConnectionHandler implements Runnable {
	private final Socket socket;
	private final INetworkMessage[] messageDeserializers;
	
	private DataInputStream from;
	private DataOutputStream to;
	
	public ConnectionHandler(Socket socket) {
		this.socket = socket;
		this.messageDeserializers = new INetworkMessage[1];
		this.messageDeserializers[MMessage.MSG_MESSAGE] = new MMessage();
		
		this.to = null;
		this.from = null;
	}

	
	@Override
	public void run() {
		Socket socket = this.socket;
		try {
			DataInputStream from = new DataInputStream(socket.getInputStream());
			DataOutputStream to = new DataOutputStream(socket.getOutputStream());
			
			this.from = from;
			this.to = to;
			
			long time = System.nanoTime();
			while( socket.isConnected() ) {
				if( System.nanoTime() - time >= 2000000000 ) {
					to.write(new MMessage("hello world!").serialize());
					to.flush();
					
					time = System.nanoTime();
				}
				
				while( from.available() >= 1 ) {
					int size = NetUtils.readSize(from);
					int header = NetUtils.readHeader(from);
					byte[] bytes = from.readNBytes(size);
					ByteBuffer buffer = ByteBuffer.wrap(bytes);
					
					this.messageDeserializers[header].deserialize(buffer).resolve();
				}
			}
			
			System.out.println("User disconnected!");
			this.socket.close();
			
		} catch( Exception e ) {
			System.out.println(
				"ERROR: Unable to create either the output writer or input reader for socket " + 
				this.socket
			);
		}
	}
}

package project.shared;

import java.nio.ByteBuffer;

public interface INetworkMessage {

	public ByteBuffer serialize();
	
	public INetworkMessage deserialize(ByteBuffer messageBuffer);
	
	public void resolve();
	
	public int getSize();
	
	public int getHead();
}

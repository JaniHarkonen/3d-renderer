package project.shared;

import java.nio.ByteBuffer;

public interface INetworkMessage {

	public byte[] serialize();
	
	public INetworkMessage deserialize(ByteBuffer buffer);
	
	public void resolve();
}

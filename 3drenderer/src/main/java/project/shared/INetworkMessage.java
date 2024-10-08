package project.shared;

import java.nio.ByteBuffer;

public interface INetworkMessage {

	public ByteBuffer serialize(INetworkStandard networkStandard);

	public INetworkMessage deserialize(INetworkStandard networkStandard, ByteBuffer messageBuffer);

	public void resolve();
	
	public int getHead();
}

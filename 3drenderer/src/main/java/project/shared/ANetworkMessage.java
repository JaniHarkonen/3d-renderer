package project.shared;

import java.nio.ByteBuffer;

public abstract class ANetworkMessage {

	protected final INetworkStandard networkStandard;
	
	public ANetworkMessage(INetworkStandard networkStandard) {
		this.networkStandard = networkStandard;
	}
	
	public INetworkStandard getNetworkStandard() {
		return this.networkStandard;
	}
	
	public abstract ByteBuffer serialize(INetworkStandard networkStandard);

	public abstract MMessage deserialize(ByteBuffer messageBuffer);

	public abstract void resolve();
	
	public abstract int getHead();
}

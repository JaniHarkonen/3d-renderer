package project.shared;

import java.nio.ByteBuffer;

public class MCompleteWorldUpdate implements INetworkMessage {
	
	public static final int MSG_COMPLETE_WORLD_UPDATE = 2;
	
	private final INetworkMessage[] messages;	// This is a compound network message
	
	public MCompleteWorldUpdate(INetworkMessage... messages) {
		this.messages = messages;
	}
	

	@Override
	public ByteBuffer serialize(INetworkStandard networkStandard) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public INetworkMessage deserialize(INetworkStandard networkStandard, ByteBuffer messageBuffer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void resolve() {
		
	}

	@Override
	public int getHead() {
		return MSG_COMPLETE_WORLD_UPDATE;
	}
}

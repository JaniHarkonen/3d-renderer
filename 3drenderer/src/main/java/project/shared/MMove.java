package project.shared;

import java.nio.ByteBuffer;

import project.utils.DebugUtils;

public class MMove implements INetworkMessage {
	public static final byte MSG_MOVE = 2;
	
	public MMove() {
		
	}
	
	
	@Override
	public ByteBuffer serialize(INetworkStandard networkStandard) {
		return ByteBuffer.allocate(0);
	}

	@Override
	public MMove deserialize(INetworkStandard networkStandard, ByteBuffer messageBuffer) {
		return new MMove();
	}

	@Override
	public void resolve() {
		DebugUtils.log(this, "moving");
	}
	
	@Override
	public int getHead() {
		return MSG_MOVE;
	}
}

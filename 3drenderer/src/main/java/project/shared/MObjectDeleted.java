package project.shared;

import java.nio.ByteBuffer;

public class MObjectDeleted implements INetworkMessage {
	
	public static final int MSG_OBJECT_DELETED = 5;

	private int objectID;
	
	public MObjectDeleted(int objectID) {
		this.objectID = objectID;
	}
	
	
	@Override
	public ByteBuffer serialize(INetworkStandard networkStandard) {
		ByteBuffer buffer = ByteBuffer.allocate(networkStandard.sizeOfGameObjectID());
		buffer.putInt(this.objectID);
		return buffer;
	}

	@Override
	public INetworkMessage deserialize(INetworkStandard networkStandard, ByteBuffer messageBuffer) {
		return new MObjectDeleted(messageBuffer.getInt());
	}

	@Override
	public void resolve() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getHead() {
		return MSG_OBJECT_DELETED;
	}
}

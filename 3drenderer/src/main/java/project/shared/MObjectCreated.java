package project.shared;

import java.nio.ByteBuffer;

public class MObjectCreated implements INetworkMessage {
	
	public static final int MSG_OBJECT_CREATED = 4;

	private int objectType;
	private int objectID;
	
	public MObjectCreated(int objectType, int objectID) {
		this.objectType = objectType;
		this.objectID = objectID;
	}
	
	
	@Override
	public ByteBuffer serialize(INetworkStandard networkStandard) {
		ByteBuffer buffer = ByteBuffer.allocate(
			networkStandard.sizeOfGameObjectType() + networkStandard.sizeOfGameObjectID()
		);
		buffer.putInt(this.objectType);
		buffer.putInt(this.objectID);
		return buffer;
	}

	@Override
	public INetworkMessage deserialize(INetworkStandard networkStandard, ByteBuffer messageBuffer) {
		return new MObjectCreated(messageBuffer.getInt(), messageBuffer.getInt());
	}

	@Override
	public void resolve() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getHead() {
		return MSG_OBJECT_CREATED;
	}
}

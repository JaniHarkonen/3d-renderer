package project.shared;

import java.nio.ByteBuffer;

import project.Application;
import project.scene.Scene;
import project.testing.TestAssets;
import project.testing.TestDummy;

public class MObjectCreated implements INetworkMessage {
	
	public static final int MSG_OBJECT_CREATED = 4;

	private int objectType;
	private int objectID;
	
	public MObjectCreated(int objectType, int objectID) {
		this.objectType = objectType;
		this.objectID = objectID;
	}
	
	public MObjectCreated() {
		this.objectType = -1;
		this.objectID = -1;
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
		Scene scene = Application.getApp().getScene();
		scene.DEBUGserverSoldier = new TestDummy(scene, TestAssets.createTestSoldier(scene));
		scene.DEBUGserverSoldier.getTransform().getRotator().setXAngle((float) Math.toRadians(-85.0d));
		scene.addObject(scene.DEBUGserverSoldier);
	}

	@Override
	public int getHead() {
		return MSG_OBJECT_CREATED;
	}
}

package project.shared;

import java.nio.ByteBuffer;

import org.joml.Vector3f;

import project.Application;
import project.utils.DebugUtils;

public class MTransformUpdate implements INetworkMessage {
	
	public static final int MSG_POSITION_UPDATE = 3;

	private int ownerID;

	private Vector3f updatedPosition;
	private Vector3f updatedEulerRotation;
	private Vector3f updatedScale;
	
	public MTransformUpdate(
		int ownerID, Vector3f updatedPosition, Vector3f updatedEulerRotation, Vector3f updatedScale
	) {
		this.ownerID = ownerID;
		this.updatedPosition = updatedPosition;
		this.updatedEulerRotation = updatedEulerRotation;
		this.updatedScale= updatedScale;
	}
	
	public MTransformUpdate() {
		this.ownerID = -1;
		this.updatedPosition = null;
		this.updatedEulerRotation = null;
		this.updatedScale = null;
	}
	
	public MTransformUpdate(
		int ownerID, 
		float updatedPositionX,
		float updatedPositionY,
		float updatedPositionZ,
		float updatedEulerRotationX, 
		float updatedEulerRotationY,
		float updatedEulerRotationZ,
		float updatedScaleX,
		float updatedScaleY,
		float updatedScaleZ
	) {
		this.ownerID = ownerID;
		this.updatedPosition = new Vector3f(updatedPositionX, updatedPositionY, updatedPositionZ);
		this.updatedEulerRotation = new Vector3f(
			updatedEulerRotationX, updatedEulerRotationY, updatedEulerRotationZ
		);
		this.updatedScale = new Vector3f(updatedScaleX, updatedScaleY, updatedScaleZ);
	}
	
	
	@Override
	public ByteBuffer serialize(INetworkStandard networkStandard) {
		return ByteBuffer.allocate(
			networkStandard.sizeOfGameObjectID() + INetworkStandard.SIZEOF_FLOAT * 9
		)
		.putInt(this.ownerID)
		.putFloat(this.updatedPosition.x)
		.putFloat(this.updatedPosition.y)
		.putFloat(this.updatedPosition.z)
		.putFloat(this.updatedEulerRotation.x)
		.putFloat(this.updatedEulerRotation.y)
		.putFloat(this.updatedEulerRotation.z)
		.putFloat(this.updatedScale.x)
		.putFloat(this.updatedScale.y)
		.putFloat(this.updatedScale.z);
	}

	@Override
	public INetworkMessage deserialize(INetworkStandard networkStandard, ByteBuffer messageBuffer) {
		return new MTransformUpdate(
			networkStandard.getGameObjectID(messageBuffer),
			messageBuffer.getFloat(),
			messageBuffer.getFloat(),
			messageBuffer.getFloat(),
			messageBuffer.getFloat(),
			messageBuffer.getFloat(),
			messageBuffer.getFloat(),
			messageBuffer.getFloat(),
			messageBuffer.getFloat(),
			messageBuffer.getFloat()
		);
	}

	@Override
	public void resolve() {
		DebugUtils.log(
			this, 
			"TRANSFORM UPDATE", 
			"pos: (" + 
				this.updatedPosition.x + "," + 
				this.updatedPosition.y + ", " + 
				this.updatedPosition.z +
			")"
		);
		Application.getApp().getScene().DEBUGserverSoldier.getTransform().setPosition(
			this.updatedPosition.x, this.updatedPosition.y, this.updatedPosition.z
		);
	}

	@Override
	public int getHead() {
		return MSG_POSITION_UPDATE;
	}
}

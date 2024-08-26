package project.shared;

import java.nio.ByteBuffer;

import project.utils.DebugUtils;

public class MMessage implements INetworkMessage {
	public static final byte MSG_MESSAGE = 1;

	public String message;
	
	public MMessage() {
		this("");
	}
	
	public MMessage(String message) {
		this.message = message;
	}
	
	
	@Override
	public ByteBuffer serialize(INetworkStandard networkStandard) {
		return networkStandard.buildStringBuffer(this.message);
	}

	@Override
	public MMessage deserialize(INetworkStandard networkStandard, ByteBuffer messageBuffer) {
		return new MMessage(networkStandard.getString(messageBuffer));
	}

	@Override
	public void resolve() {
		DebugUtils.log(this, "Resolved MMessage:", this.message);
	}
	
	@Override
	public int getHead() {
		return MSG_MESSAGE;
	}
}

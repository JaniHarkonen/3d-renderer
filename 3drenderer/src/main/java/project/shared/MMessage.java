package project.shared;

import java.nio.ByteBuffer;

import project.utils.DebugUtils;

public class MMessage extends ANetworkMessage {
	public static final byte MSG_MESSAGE = 1;

	public String message;
	
	public MMessage(INetworkStandard networkStandard) {
		super(networkStandard);
		this.message = "";
	}
	
	public MMessage(String message) {
		super(null);
		this.message = message;
	}
	
	
	@Override
	public ByteBuffer serialize(INetworkStandard networkStandard) {
		return networkStandard.buildStringBuffer(this.message);
	}

	@Override
	public MMessage deserialize(ByteBuffer messageBuffer) {
		return new MMessage(this.getNetworkStandard().getString(messageBuffer));
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

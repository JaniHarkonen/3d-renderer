package project.shared;

import java.nio.ByteBuffer;

import project.utils.DebugUtils;

public class MMessage implements INetworkMessage {
	public static final byte MSG_MESSAGE = 0;

	public String message;
	
	public MMessage() {
		this("");
	}
	
	public MMessage(String message) {
		this.message = message;
	}
	
	
	@Override
	public byte[] serialize() {
		byte[] messageBytes = this.message.getBytes();
		int length = messageBytes.length;
		byte[] bytes = new byte[length + 3];
		
		bytes[0] = (byte) (length + 1);
		bytes[1] = MSG_MESSAGE;
		bytes[2] = (byte) length;
		
		for( int i = 0; i < length; i++ ) {
			bytes[i + 3] = messageBytes[i];
		}
		
		return bytes;
	}

	@Override
	public MMessage deserialize(ByteBuffer buffer) {
		byte[] bytes = new byte[buffer.get()];
		buffer.get(1, bytes);
		return new MMessage(new String(bytes));
	}

	@Override
	public void resolve() {
		DebugUtils.log(this, "Resolved MMessage:", this.message);
	}
	
	@Override
	public int getType() {
		return MSG_MESSAGE;
	}
}

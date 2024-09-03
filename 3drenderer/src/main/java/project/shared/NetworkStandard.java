package project.shared;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public final class NetworkStandard implements INetworkStandard {
	
	/************************* MessageTypeTable-class **************************/
	
	private class MessageTypeTable {
		private Map<Integer, INetworkMessage> table;
		
		private MessageTypeTable() {
			this.table = new HashMap<>();
		}
		
		private MessageTypeTable declareType(INetworkMessage template) {
			this.table.put(template.getHead(), template);
			return this;
		}
		
		private INetworkMessage getTemplate(int type) {
			return this.table.get(type);
		}
	}
	
	
	/************************* NetworkStandard-class **************************/
	
	private final MessageTypeTable messageTypeTable;
	
	public NetworkStandard() {
		this.messageTypeTable = new MessageTypeTable();
	}
	
	
	@Override
	public void declare() {
		this.messageTypeTable
		.declareType(new MMessage())
		.declareType(new MMove());
	}
	
	@Override
	public int readSizeIfAvailable(DataInputStream in) throws IOException {
		return (in.available() >= this.sizeOfSize()) ? in.read() : -1;
	}
	
	@Override
	public void writeSize(DataOutputStream out, int size) throws IOException {
		out.write(size);
	}
	
	@Override
	public void writeHead(DataOutputStream out, int head) throws IOException {
		out.write(head);
	}
	
	@Override
	public int getMessageHead(ByteBuffer messageBuffer) {
		return messageBuffer.get();
	}
	
	@Override
	public int getGameObjectID(ByteBuffer messageBuffer) {
		return messageBuffer.getInt();
	}
	
	@Override
	public String getString(ByteBuffer messageBuffer) {
		int length = messageBuffer.get();
		StringBuilder builder = new StringBuilder(length);
		
		for( int i = 0; i < length; i++ ) {
			builder.append(messageBuffer.getChar());
		}
		
		return builder.toString();
	}
	
	@Override
	public ByteBuffer buildStringBuffer(String string) {
		int charSize = this.sizeOfStringChar();
		int length = string.length();
		ByteBuffer buffer = ByteBuffer.allocate(length * charSize + charSize);
		
		buffer.put((byte) length);
		for( int i = 0; i < length; i++ ) {
			buffer.putChar(string.charAt(i));
		}
		
		return buffer;
	}
	
	@Override
	public INetworkMessage getTemplate(int head) {
		return this.messageTypeTable.getTemplate(head);
	}
	
	@Override
	public int sizeOfSize() {
		return INetworkStandard.SIZEOF_BYTE;
	}
	
	@Override
	public int sizeOfHead() {
		return INetworkStandard.SIZEOF_BYTE;
	}
	
	@Override
	public int sizeOfStringChar() {
		return 2;
	}
	
	@Override
	public int sizeOfStringLength() {
		return INetworkStandard.SIZEOF_BYTE;
	}
	
	@Override
	public int sizeOfGameObjectType() {
		return INetworkStandard.SIZEOF_INT;
	}
	
	@Override
	public int sizeOfGameObjectID() {
		return INetworkStandard.SIZEOF_INT;
	}
}

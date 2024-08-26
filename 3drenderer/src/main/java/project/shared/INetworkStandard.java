package project.shared;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public interface INetworkStandard {
	
	/**
	 * "Declares" the networking standard by instantiating message templates and other
	 * networking values.
	 */
	public void declare();
	
	/**
	 * Reads the size of the next incoming message from a given DataInputStream, or 
	 * returns -1, if the size is not yet available. The size will be return as an int, 
	 * however, the read operation may fetch smaller data types, such as bytes.
	 * <br/><br/>
	 * This method should be non-blocking meaning it only issues a read call (which is
	 * blocking) when enough data is available for reading.
	 * 
	 * @param in DataInputStream where the size of the message should be.
	 * @return Size of the incoming message (as int) or -1, if the size of the next
	 * message is not yet available.
	 * @throws IOException If reading from the DataInputStream failed.
	 */
	public int readSizeIfAvailable(DataInputStream in) throws IOException;
	
	/**
	 * Standard for writing the size of a message to a given DataOutputStream. The size
	 * is passed in as an int, however, the write operation may write smaller data types,
	 * such as bytes.
	 * 
	 * @param out DataOutputStream where the size of the message should be written.
	 * @param size The size of the message (as int).
	 * @throws IOException If writing to the DataOutputStream failed.
	 */
	public void writeSize(DataOutputStream out, int size) throws IOException;
	
	/**
	 * Standard for writing the head of a message to a given DataOutputStream. The head
	 * is passed in as an int, however, the write operation may write smaller data types,
	 * such as bytes.
	 * 
	 * @param out DataOutputStream where the head of the message should be written.
	 * @param size The head of the message (as int).
	 * @throws IOException If writing to the DataOutputStream failed.
	 */
	public void writeHead(DataOutputStream out, int head) throws IOException;
	
	/**
	 * Standard for extracting the head of a message from a message stored as a ByteBuffer.
	 * This operation will advance the cursor in the ByteBuffer by the length of the head. 
	 * The head will be returned as an int even if the read operation returns a smaller data
	 * type, such as a byte.
	 * 
	 * @param messageBuffer ByteBuffer where the message along with its head is to be found.
	 * @return The head of the message (as int).
	 */
	public int getMessageHead(ByteBuffer messageBuffer);
	
	/**
	 * Standard for extracting a string from a message stored as a ByteBuffer. This operation 
	 * will advance the cursor in the ByteBuffer by the length of the string.
	 * 
	 * @param messageBuffer ByteBuffer from which to read the string.
	 * @return The resulting string.
	 */
	public String getString(ByteBuffer messageBuffer);
	
	/**
	 * Standard for formatting a string inside a ByteBuffer that can be immediately attached
	 * to any message ByteBuffer. The result is a ByteBuffer containing the string in a 
	 * serialized form.
	 * 
	 * @param string String to serialize.
	 * @return ByteBuffer containing the string in a form comporting with the network 
	 * standard.
	 */
	public ByteBuffer buildStringBuffer(String string);
	
	/**
	 * Returns the network message template for a message with a given head or NULL if no
	 * such message type exists.
	 * 
	 * @param head Head of the message.
	 * @return The corresponding network message template or NULL if no such template exists.
	 */
	public ANetworkMessage getTemplate(int head);
	
	/**
	 * @return The length of the "size"-part of a message in bytes.
	 */
	public int sizeOfSize();
	
	/**
	 * @return The length of the "head"-part of a message in bytes.
	 */
	public int sizeOfHead();
	
	/**
	 * @return The length of a single character in a string.
	 */
	public int sizeOfStringChar();
	
	/**
	 * @return The length of the value that denotes the length of a string.
	 */
	public int sizeOfStringLength();
}


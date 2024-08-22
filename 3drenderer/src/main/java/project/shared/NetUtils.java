package project.shared;

import java.io.DataInputStream;
import java.io.IOException;

public final class NetUtils {

	public static int readSize(DataInputStream in) throws IOException {
		return in.read();
	}
	
	public static int readHeader(DataInputStream in) throws IOException {
		return in.read();
	}
	
	public static String readString(DataInputStream in) throws IOException {
		int length = in.readShort();
		byte[] bytes = new byte[length];
		in.read(bytes);
		return new String(bytes);
	}
}

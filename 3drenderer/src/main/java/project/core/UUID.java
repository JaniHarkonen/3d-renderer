package project.core;

public final class UUID {

	public static final long FIRST_INDEX = 10;
	
	private static long nextID = FIRST_INDEX;
	
	public static long getUUID() {
		return nextID++;
	}
}

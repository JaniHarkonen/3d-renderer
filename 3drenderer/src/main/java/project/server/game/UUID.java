package project.server.game;

public final class UUID {
	
	public static final int FIRST_INDEX = 10;

	private static int nextID = FIRST_INDEX;
	
	public static int nextID() {
		return nextID++;
	}
}

package project.utils;

public final class DebugUtils {

	public static void log(Object me, Object... messages) {
		System.out.println("[" + me + "]");
		
		for( Object message : messages ) {
			System.out.println(message);
		}
	}
}

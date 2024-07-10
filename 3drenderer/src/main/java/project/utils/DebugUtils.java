package project.utils;

public final class DebugUtils {

	public static void log(Object me, Object message) {
		System.out.println("[" + me + "]");
		System.out.println(message);
	}
}

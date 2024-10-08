package project.utils;

public final class ObjectUtils {

	public static <T> T returnIfNotNull(T o) {
		return (o != null) ? o : null;
	}
	
	public static <T> T returnOrDefault(T o, T _default) {
		return (o != null) ? o : _default;
	}
}

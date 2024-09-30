package project.gui.props;

import java.util.HashSet;
import java.util.Set;

import org.joml.Vector4f;

public class Property {
	public static final String PX = "px";			// Pixels (float)
	public static final String WPC = "%w";			// Parent's width percent (float)
	public static final String HPC = "%h";			// Parent's height percent (float)
	public static final String C = "c";				// Width of column in parent's grid (float)
	public static final String R = "r";				// Height of row in parent's grid (float)
	public static final String THEME = "theme";		// Theme property reference (String)
	public static final String STRING = "str";		// String
	public static final String NUMBER = "num";		// Floating point value
	public static final String EXPRESSION = "expr";	// Expression (String)
	public static final String COLOR = "color";		// RGBA-color (Vector4f)
	
	public static final String FUNCTION_THEME = "t";		// Theme property getter function
	public static final String FUNCTION_MIN = "min";		// Returns minimum of a set of values
	public static final String FUNCTION_MAX = "max";		// Returns maximum of a set of values
	public static final String FUNCTION_CLAMP = "clamp";	// Limits a value between two edges
	public static final String FUNCTION_RGB = "rgb";		// Creates an RGB-color with 1 alpha
	public static final String FUNCTION_RGBA = "rgba";		// Creates an RGBA-color
	
	public static final String FUNCTION_TEST = "test";
	
	private static final Set<String> functionSet;
	static {
		functionSet = new HashSet<>();
		functionSet.add(FUNCTION_THEME);
		functionSet.add(FUNCTION_MIN);
		functionSet.add(FUNCTION_MAX);
		functionSet.add(FUNCTION_CLAMP);
		functionSet.add(FUNCTION_RGB);
		functionSet.add(FUNCTION_RGBA);
		functionSet.add(FUNCTION_TEST);
	}
	
	public static boolean functionExists(String propertyName) {
		return functionSet.contains(propertyName);
	}
	
	private final String name;
	private String type;
	private Object value;
	private boolean isNumeric;
	
	public Property(String name, Object value, String type, boolean isNumeric) {
		this.name = name;
		this.value = value;
		this.type = type;
		this.isNumeric = isNumeric;
	}
	
	public Property(String name, float value, String type) {
		this(name, (Object) value, type, true);
	}
	
	public Property(String name, String value, String type) {
		this(name, (Object) value, type, false);
	}
	
	public Property(String name, Vector4f value, String type) {
		this(name, (Object) value, type, false);
	}
	
	public Property(String name) {
		this(name, null, null, false);
	}
	
	public Property(Property src) {
		this.name = src.name;
		this.type = src.type;
		this.value = src.value;	// NOT A DEEP COPY EITHER
	}
	
	
	public void set(float value, String type) {
		this.isNumeric = true;
		this.set((Object) value, type);
	}
	
	public void set(String value, String type) {
		this.isNumeric = false;
		this.set((Object) value, type);
	}
	
	public void set(Vector4f value, String type) {
		this.isNumeric = false;
		this.set((Object) value, type);
	}
	
	private void set(Object value, String type) {
		this.value = value;
		this.type = type;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getType() {
		return this.type;
	}
	
	public Object getValue() {
		return this.value;
	}
	
	public boolean isNumeric() {
		return this.isNumeric;
	}
	
	@Override
	public boolean equals(Object o) {
		if( !(o instanceof Property) ) {
			return false;
		}
		
		Property p = (Property) o;
		return (
			this.name.equals(p.name)  &&
			this.type.equals(p.type) &&
			this.value.equals(p.value)
		);
	}
}

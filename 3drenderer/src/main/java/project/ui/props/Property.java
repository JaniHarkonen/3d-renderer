package project.ui.props;

import java.util.HashSet;
import java.util.Set;

import org.joml.Vector4f;

public class Property {
	public static final String PX = "px";					// Pixels (float)
	public static final String WPERCENT = "WIDTH%";			// Parent's width percent (float)
	public static final String HPERCENT = "HEIGHT%";		// Parent's height percent (float)
	public static final String PERCENT = "%";				// Percentage (ambiguous type) (float)
	public static final String C = "c";						// Width of column in parent's grid (float)
	public static final String R = "r";						// Height of row in parent's grid (float)
	public static final String THEME = "THEME";				// Theme property reference (String)
	public static final String STRING = "STRING";			// String
	public static final String NUMBER = "NUMBER";			// Floating point value
	public static final String EXPRESSION = "EXPRESSION";	// Expression (String)
	public static final String COLOR = "COLOR";				// RGBA-color (Vector4f)
	
	public static final String FUNCTION_THEME_ABBR = "t"; 	// Theme property getter function
	public static final String FUNCTION_EXPR_ABBR = "e";	// Expression evaluation function (abbr.)
	public static final String FUNCTION_EXPR = "expr";		// Expression evaluation function
	public static final String FUNCTION_MIN = "min";		// Returns minimum of a set of values
	public static final String FUNCTION_MAX = "max";		// Returns maximum of a set of values
	public static final String FUNCTION_CLAMP = "clamp";	// Limits a value between two edges
	public static final String FUNCTION_RGB = "rgb";		// Creates an RGB-color with 1 alpha
	public static final String FUNCTION_RGBA = "rgba";		// Creates an RGBA-color
	
	public static final String RQUERY_DIMENSION_SEPARATOR = "x";
	public static final String RQUERY_ASPECT_RATIO_SEPARATOR = ":";
	
	public static final Property NULL_PX = 
		new Property(null, 0.0f, Property.PX);	// Null property, 0px
	public static final Property NULL_COLOR = 
		new Property(null, new Vector4f(0, 0, 0, 1), Property.COLOR);	// Null color, (0, 0, 0, 1)
	
	private static final Set<String> functionSet;
	static {
		functionSet = new HashSet<>();
		functionSet.add(FUNCTION_THEME_ABBR);
		functionSet.add(FUNCTION_EXPR);
		functionSet.add(FUNCTION_EXPR_ABBR);
		functionSet.add(FUNCTION_MIN);
		functionSet.add(FUNCTION_MAX);
		functionSet.add(FUNCTION_CLAMP);
		functionSet.add(FUNCTION_RGB);
		functionSet.add(FUNCTION_RGBA);
	}
	
	public static boolean functionExists(String propertyName) {
		return functionSet.contains(propertyName);
	}
	
	public static boolean isNumeric(Property property) {
		return (property == null) ? false : property.isNumeric();
	}
	
	public static String getType(Property property) {
		return (property == null) ? "" : property.getType();
	}
	
	public static boolean typeOf(Property property, String type) {
		return (property == null) ? false : property.getType().equals(type);
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
	
	public Property(String name, Object value, String type) {
		this(name, value, type, value instanceof Float);
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

package project.gui.props4;

public class Property {

	public static final String PX = "px";
	public static final String PC = "%";
	public static final String C = "c";
	public static final String R = "r";
	public static final String THEME = "theme";
	public static final String STRING = "str";
	public static final String NUMBER = "num";
	public static final String EXPRESSION = "expr";
	public static final String COLOR = "color";
	
	private final String name;
	private String type;
	private Object value;
	
	public Property(String name) {
		this.name = name;
		this.type = null;
		this.value = null;
	}
	
	public Property(Property src) {
		this.name = src.name;
		this.type = src.type;
		this.value = src.value;	// NOT A DEEP COPY EITHER
	}
	
	
	public void set(Object value, String type) {
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
	
	@Override
	public boolean equals(Object o) {
		if( !(o instanceof Property) ) {
			return false;
		}
		
		Property p = (Property) o;
		
			// DON'T USE EQUALS FOR NAME AND TYPE AS THEY REFERENCE STATIC FIELDS
		return (
			this.name == p.name &&
			this.type == p.type &&
			this.value.equals(p.value)
		);
	}
}

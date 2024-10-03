package project.gui.props;

public class PropertyBuilder {

	public Object value;
	public String dataType;
	
	public PropertyBuilder(Object value, String dataType) {
		this.value = value;
		this.dataType = dataType;
	}
	
	public PropertyBuilder(float value) {
		this((Object) value, null);
	}
	
	
	public Property build(String name) {
		return new Property(name, this.value, this.dataType);
	}
}

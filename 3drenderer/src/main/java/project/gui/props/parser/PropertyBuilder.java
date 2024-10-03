package project.gui.props.parser;

import project.gui.props.Property;

public class PropertyBuilder {

	public final String name;
	public final Object value;
	public final String dataType;
	
	public PropertyBuilder(String name, Object value, String dataType) {
		this.name = name;
		this.value = value;
		this.dataType = dataType;
	}
	
	
	public Property build() {
		return new Property(this.name, this.value, this.dataType);
	}
}

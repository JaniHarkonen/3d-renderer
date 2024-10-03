package project.gui.props.parser;

import project.gui.props.Properties;
import project.gui.props.Property;

public class PercentageBuilder extends PropertyBuilder {
	public PercentageBuilder(float value) {
		super((Object) value, null);
	}
	
	
	public Property build(String name) {
		Properties.Orientation orientation = Properties.getOrientation(name);
		if( orientation == Properties.Orientation.HORIZONTAL ) {
			return new Property(name, this.value, Property.WPERCENT);
		} else if( orientation == Properties.Orientation.VERTICAL ) {
			return new Property(name, this.value, Property.HPERCENT);
		}
		return null;
	}
}

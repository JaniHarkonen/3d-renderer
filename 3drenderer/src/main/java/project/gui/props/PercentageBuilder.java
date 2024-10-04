package project.gui.props;

public class PercentageBuilder extends PropertyBuilder {
	public PercentageBuilder(float value) {
		super((Object) value, null);
	}
	
	
	public Property build(String name) {
		Properties.Orientation orientation = Properties.getOrientation(name);
		if( orientation == Properties.Orientation.HORIZONTAL ) {
			return new Property(name, (float) this.value / 100f, Property.WPERCENT);
		} else if( orientation == Properties.Orientation.VERTICAL ) {
			return new Property(name, (float) this.value / 100f, Property.HPERCENT);
		}
		return null;
	}
	
	@Override
	public String toString() {
		return this.value + "%";
	}
}

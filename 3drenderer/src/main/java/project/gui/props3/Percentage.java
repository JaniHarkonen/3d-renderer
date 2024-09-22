package project.gui.props3;

public class Percentage implements IPropValue<Float> {

	private Float value;
	
	@Override
	public void setValue(Float value) {
		this.value = value;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Float getValue(Properties propertiesContext, String propertyName) {
		return ((Property<Float>) propertiesContext.getProperty(propertyName)).get(propertiesContext) * this.value;
	}
}

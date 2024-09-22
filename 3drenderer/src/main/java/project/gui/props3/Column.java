package project.gui.props3;

public class Column implements IPropValue<Float> {

	private Float value;
	
	public Column(Float value) {
		this.value = value;
	}
	
	
	@Override
	public void setValue(Float value) {
		this.value = value;
	}

	@Override
	public Float getValue(Properties propertiesContext, String propertyName) {
		return propertiesContext;
	}
}

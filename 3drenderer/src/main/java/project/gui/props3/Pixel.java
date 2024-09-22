package project.gui.props3;

public class Pixel implements IPropValue<Float> {

	private Float value;
	
	public Pixel(Float value) {
		this.value = value;
	}
	
	
	@Override
	public void setValue(Float value) {
		this.value = value;
	}
	
	@Override
	public Float getValue(Properties propertiesContext, String propertyName) {
		return this.value;
	}
}

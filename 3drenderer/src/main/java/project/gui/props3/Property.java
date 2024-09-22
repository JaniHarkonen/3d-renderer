package project.gui.props3;

public class Property<T> {

	private final String name;
	private IPropValue<T> value;
	
	public Property(String name) {
		this.name = name;
		this.value = null;
	}
	
	
	public void set(IPropValue<T> propertyValue) {
		this.value = propertyValue;
	}
	
	public T get(Properties propertiesContext) {
		return this.value.getValue(propertiesContext, this.name);
	}
	
	public String getName() {
		return this.name;
	}
}

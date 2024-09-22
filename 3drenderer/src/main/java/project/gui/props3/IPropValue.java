package project.gui.props3;

public interface IPropValue<T> {

	public void setValue(T value);
	public T getValue(Properties propertiesContext, String propertyName);
}

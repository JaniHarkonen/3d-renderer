package project.gui.props3;

import java.util.HashMap;
import java.util.Map;

public class Properties {

	private final Map<String, Property<?>> propertiesMap;
	
	public Properties() {
		this.propertiesMap = new HashMap<>();
	}
	
	
	public Property<?> getProperty(String propertyName) {
		return this.propertiesMap.get(propertyName);
	}
}

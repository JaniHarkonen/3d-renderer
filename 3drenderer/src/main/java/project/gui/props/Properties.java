package project.gui.props;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Properties {
	
	public static final String LEFT = "left";
	public static final String TOP = "top";
	public static final String WIDTH = "width";
	public static final String HEIGHT = "height";
	public static final String COLS = "cols";
	public static final String ROWS = "rows";
	public static final String PRIMARY_COLOR = "primaryColor";
	public static final String SECONDARY_COLOR = "secondaryColor";
	public static final String ANCHOR_X = "anchorX";
	public static final String ANCHOR_Y = "anchorY";
	
	
	private final Map<String, Property> propertiesMap;
	
	public Properties() {
		this.propertiesMap = new LinkedHashMap<>();
		this.addProperty(new Property(LEFT))
		.addProperty(new Property(TOP))
		.addProperty(new Property(WIDTH))
		.addProperty(new Property(HEIGHT))
		.addProperty(new Property(COLS))
		.addProperty(new Property(ROWS))
		.addProperty(new Property(PRIMARY_COLOR))
		.addProperty(new Property(SECONDARY_COLOR))
		.addProperty(new Property(ANCHOR_X))
		.addProperty(new Property(ANCHOR_Y));
	}
	
	public Properties(Properties src) {
		this.propertiesMap = new HashMap<>(src.propertiesMap.size());
		for( Map.Entry<String, Property> en : src.propertiesMap.entrySet() ) {
			this.propertiesMap.put(en.getKey(), new Property(en.getValue()));
		}
	}
	
	
	private Properties addProperty(Property property) {
		this.propertiesMap.put(property.getName(), property);
		return this;
	}
	
	public Property getProperty(String key) {
		return this.propertiesMap.get(key);
	}
	
	public Collection<Property> getPropertiesCollection() {
		return this.propertiesMap.values();
	}
	
	@Override
	public boolean equals(Object o) {
		if( !(o instanceof Properties) ) {
			return false;
		}
		
		Properties p = (Properties) o;
		for( Map.Entry<String, Property> en : this.propertiesMap.entrySet() ) {
			if( !en.getValue().equals(p.getProperty(en.getKey())) ) {
				return false;
			}
		}
		
		return true;
	}
}

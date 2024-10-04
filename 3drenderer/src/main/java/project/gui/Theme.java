package project.gui;

import java.util.HashMap;
import java.util.Map;

import project.gui.props.Property;

public class Theme {
	public static final Theme NULL_THEME = new Theme("NULL_THEME");
	
	private final String id;
	private final Map<String, Theme> sections;
	private final Map<String, Property> properties;
	
	public Theme(String id) {
		this.id = id;
		this.sections = new HashMap<>();
		this.properties = new HashMap<>();
	}
	
	public Theme(Theme src) {
		this.id = src.id;
		this.sections = new HashMap<>(src.sections.size());
		this.properties = new HashMap<>(src.properties.size());
		
		for( Map.Entry<String, Property> en : src.properties.entrySet() ) {
			this.properties.put(en.getKey(), new Property(en.getValue()));
		}
		
		for( Map.Entry<String, Theme> en : src.sections.entrySet() ) {
			this.sections.put(en.getKey(), new Theme(en.getValue()));
		}
	}
	
	
	public void setSection(String key, Theme section) {
		this.sections.put(key, section);
	}
	
	public void setProperty(Property property) {
		this.properties.put(property.getName(), property);
	}
	
	public String getID() {
		return this.id;
	}
	
	public Property getProperty(String key) {
		if( key == null || key.length() == 0 ) {
			return null;
		}
		
		return this.getProperty(key.split("\\."), 0);
	}
	
	private Property getProperty(String[] split, int index) {
		if( index == split.length - 1 ) {
			return this.properties.get(split[index]);
		}
		
		Theme section = this.sections.get(split[index]);
		if( section == null ) {
			return null;
		}
		
		return section.getProperty(split, ++index);
	}
}

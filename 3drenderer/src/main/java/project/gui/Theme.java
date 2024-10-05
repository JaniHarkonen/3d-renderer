package project.gui;

import java.util.HashMap;
import java.util.Map;

import project.gui.props.PropertyBuilder;

public class Theme {
	private final Map<String, Theme> sections;
	
		// Using property builders as names of the properties that the theme properties will be 
		// applied to are ambiguous
	private final Map<String, PropertyBuilder> properties;
	
	public Theme() {
		this.sections = new HashMap<>();
		this.properties = new HashMap<>();
	}
	
	public Theme(Theme src) {
		this.sections = new HashMap<>(src.sections.size());
		this.properties = new HashMap<>(src.properties.size());
		
		for( Map.Entry<String, PropertyBuilder> en : src.properties.entrySet() ) {
			this.properties.put(en.getKey(), en.getValue());
		}
		
		for( Map.Entry<String, Theme> en : src.sections.entrySet() ) {
			this.sections.put(en.getKey(), new Theme(en.getValue()));
		}
	}
	
	
	public void setSection(String key, Theme section) {
		this.sections.put(key, section);
	}
	
	public void setProperty(String key, PropertyBuilder builder) {
		this.properties.put(key, builder);
	}
	
	public PropertyBuilder getPropertyBuilder(String key) {
		if( key == null || key.length() == 0 ) {
			return null;
		}
		
		return this.getPropertyBuilder(key.split("\\."), 0);
	}
	
	private PropertyBuilder getPropertyBuilder(String[] split, int index) {
		if( index == split.length - 1 ) {
			return this.properties.get(split[index]);
		}
		
		Theme section = this.sections.get(split[index]);
		if( section == null ) {
			return null;
		}
		
		return section.getPropertyBuilder(split, ++index);
	}
}

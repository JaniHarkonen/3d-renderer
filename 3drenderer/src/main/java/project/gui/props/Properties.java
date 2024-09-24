package project.gui.props;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.joml.Vector4f;

import project.gui.AGUIElement;

public class Properties {
	public static final float DEFAULT_LEFT = 0;
	public static final float DEFAULT_TOP = 0;
	
	public static final float DEFAULT_MIN_WIDTH = 0;
	public static final float DEFAULT_MIN_HEIGHT = 0;
	public static final float DEFAULT_MAX_WIDTH = Float.MAX_VALUE;
	public static final float DEFAULT_MAX_HEIGHT = Float.MAX_VALUE;
	public static final float DEFAULT_WIDTH = 0;
	public static final float DEFAULT_HEIGHT = 0;
	
	public static final float DEFAULT_COLS = 1;
	public static final float DEFAULT_ROWS = 1;
	public static final Vector4f DEFAULT_PRIMARY_COLOR = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);
	public static final Vector4f DEFAULT_SECONDARY_COLOR = new Vector4f(1.0f, 1.0f, 1.0f, 0.0f);
	public static final float DEFAULT_ANCHOR_X = 0;
	public static final float DEFAULT_ANCHOR_Y = 0;
	public static final float DEFAULT_LINE_HEIGHT = 22;
	public static final float DEFAULT_BASELINE = 16;
	
	public static final String LEFT = "left";
	public static final String TOP = "top";
	
	public static final String MIN_WIDTH = "minWidth";
	public static final String MIN_HEIGHT = "minHeight";
	public static final String MAX_WIDTH = "maxWidth";
	public static final String MAX_HEIGHT = "maxHeight";
	public static final String WIDTH = "width";
	public static final String HEIGHT = "height";
	
	public static final String COLS = "cols";
	public static final String ROWS = "rows";
	public static final String PRIMARY_COLOR = "primaryColor";
	public static final String SECONDARY_COLOR = "secondaryColor";
	public static final String ANCHOR_X = "anchorX";
	public static final String ANCHOR_Y = "anchorY";
	public static final String LINE_HEIGHT = "lineHeight";
	public static final String BASELINE = "baseline";
	
	private final AGUIElement owner;
	private final Map<String, Property> propertiesMap;
	
	public Properties(AGUIElement owner) {
		this.owner = owner;
		this.propertiesMap = new LinkedHashMap<>();
		this.addProperty(new Property(LEFT, DEFAULT_LEFT, Property.PX))
		.addProperty(new Property(TOP, DEFAULT_TOP, Property.PX))
		.addProperty(new Property(MIN_WIDTH, DEFAULT_MIN_WIDTH, Property.PX))
		.addProperty(new Property(MIN_HEIGHT, DEFAULT_MIN_HEIGHT, Property.PX))
		.addProperty(new Property(MAX_WIDTH, DEFAULT_MAX_WIDTH, Property.PX))
		.addProperty(new Property(MAX_HEIGHT, DEFAULT_MAX_HEIGHT, Property.PX))
		.addProperty(new Property(WIDTH, DEFAULT_WIDTH, Property.PX))
		.addProperty(new Property(HEIGHT, DEFAULT_HEIGHT, Property.PX))
		.addProperty(new Property(COLS, DEFAULT_COLS, Property.NUMBER))
		.addProperty(new Property(ROWS, DEFAULT_ROWS, Property.NUMBER))
		.addProperty(new Property(PRIMARY_COLOR, DEFAULT_PRIMARY_COLOR, Property.COLOR))
		.addProperty(new Property(SECONDARY_COLOR, DEFAULT_SECONDARY_COLOR, Property.COLOR))
		.addProperty(new Property(ANCHOR_X, DEFAULT_ANCHOR_X, Property.PX))
		.addProperty(new Property(ANCHOR_Y, DEFAULT_ANCHOR_Y, Property.PX))
		.addProperty(new Property(LINE_HEIGHT, DEFAULT_LINE_HEIGHT, Property.PX))
		.addProperty(new Property(BASELINE, DEFAULT_BASELINE, Property.PX));
	}
	
	public Properties(Properties src) {
		this.owner = src.owner;
		this.propertiesMap = new LinkedHashMap<>(src.propertiesMap.size());
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
	
	public Collection<Property> getAsCollection() {
		return this.propertiesMap.values();
	}
	
	public AGUIElement getOwner() {
		return this.owner;
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

package project.gui.props;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.joml.Vector4f;

import project.gui.AGUIElement;

public class Properties {
	/**
	 * This enum holds the axes a property can pertain to. For example, "width" or 
	 * "left" properties pertain to the horizontal axis, while "height" and "top"
	 * pertain to the vertical axis. These orientations are returned by the 
	 * Properties.getOrientation(String)-method that maps a property name to its 
	 * orientation.
	 * 
	 * @author Jani Härkönen
	 *
	 */
	public static enum Orientation {
		/**
		 * Property pertains to the horizontal axis.
		 */
		HORIZONTAL,
		
		/**
		 * Property pertains to the vertical axis.
		 */
		VERTICAL
	}
	
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
	public static final String RIGHT = "right";
	public static final String BOTTOM = "bottom";
	
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
	
	private static final Map<String, Orientation> propertyToOrientation;
	static {
		propertyToOrientation = new HashMap<>();
		propertyToOrientation.put(LEFT, Orientation.HORIZONTAL);
		propertyToOrientation.put(WIDTH, Orientation.HORIZONTAL);
		propertyToOrientation.put(MIN_WIDTH, Orientation.HORIZONTAL);
		propertyToOrientation.put(MAX_WIDTH, Orientation.HORIZONTAL);
		propertyToOrientation.put(ANCHOR_X, Orientation.HORIZONTAL);
		
		propertyToOrientation.put(TOP, Orientation.VERTICAL);
		propertyToOrientation.put(HEIGHT, Orientation.VERTICAL);
		propertyToOrientation.put(MIN_HEIGHT, Orientation.VERTICAL);
		propertyToOrientation.put(MAX_HEIGHT, Orientation.VERTICAL);
		propertyToOrientation.put(ANCHOR_Y, Orientation.VERTICAL);
		propertyToOrientation.put(LINE_HEIGHT, Orientation.VERTICAL);
		propertyToOrientation.put(BASELINE, Orientation.VERTICAL);
	}
	
	public static Orientation getOrientation(String propertyName) {
		return propertyToOrientation.get(propertyName);
	}
	
	
	public class Style {
		private RQuery responsivenessQuery;
		private Map<String, Property> properties;
		
		private Style(RQuery responsivenessQuery, Map<String, Property> properties) {
			this.responsivenessQuery = responsivenessQuery;
			this.properties = properties;
		}
		
		public void addProperty(Property property) {
			this.properties.put(property.getName(), property);
		}
	}
	
	private final AGUIElement owner;
	private final List<Style> stylesByResponsiveness;
	
	public Properties(AGUIElement owner) {
		this.owner = owner;
		this.stylesByResponsiveness = new ArrayList<>();
		
		Map<String, Property> defaultProperties = new LinkedHashMap<>();
		this.addProperty(new Property(LEFT, DEFAULT_LEFT, Property.PX), defaultProperties);
		this.addProperty(new Property(TOP, DEFAULT_TOP, Property.PX), defaultProperties);
		this.addProperty(
			new Property(MIN_WIDTH, DEFAULT_MIN_WIDTH, Property.PX), defaultProperties
		);
		this.addProperty(
			new Property(MIN_HEIGHT, DEFAULT_MIN_HEIGHT, Property.PX), defaultProperties
		);
		this.addProperty(
			new Property(MAX_WIDTH, DEFAULT_MAX_WIDTH, Property.PX), defaultProperties
		);
		this.addProperty(
			new Property(MAX_HEIGHT, DEFAULT_MAX_HEIGHT, Property.PX), defaultProperties
		);
		this.addProperty(new Property(WIDTH, DEFAULT_WIDTH, Property.PX), defaultProperties);
		this.addProperty(new Property(HEIGHT, DEFAULT_HEIGHT, Property.PX), defaultProperties);
		this.addProperty(new Property(COLS, DEFAULT_COLS, Property.NUMBER), defaultProperties);
		this.addProperty(new Property(ROWS, DEFAULT_ROWS, Property.NUMBER), defaultProperties);
		this.addProperty(new Property(
			PRIMARY_COLOR, DEFAULT_PRIMARY_COLOR, Property.COLOR), defaultProperties
		);
		this.addProperty(new Property(
			SECONDARY_COLOR, DEFAULT_SECONDARY_COLOR, Property.COLOR), defaultProperties
		);
		this.addProperty(new Property(ANCHOR_X, DEFAULT_ANCHOR_X, Property.PX), defaultProperties);
		this.addProperty(new Property(ANCHOR_Y, DEFAULT_ANCHOR_Y, Property.PX), defaultProperties);
		this.addProperty(
			new Property(LINE_HEIGHT, DEFAULT_LINE_HEIGHT, Property.PX), defaultProperties
		);
		this.addProperty(new Property(BASELINE, DEFAULT_BASELINE, Property.PX), defaultProperties);
		
		this.stylesByResponsiveness.add(new Style(new RQuery(), defaultProperties));
	}
	
	public Properties(Properties src) {
		this.owner = src.owner;
		this.stylesByResponsiveness = new ArrayList<>(src.stylesByResponsiveness.size());
		
		for( Style style : src.stylesByResponsiveness ) {
			Map<String, Property> propertiesMap = new LinkedHashMap<>(style.properties.size());
			for( Map.Entry<String, Property> en : style.properties.entrySet() ) {
				propertiesMap.put(en.getKey(), new Property(en.getValue()));
			}
			this.stylesByResponsiveness.add(
				new Style(new RQuery(style.responsivenessQuery), propertiesMap)
			);
		}
	}
	
	
	private Properties addProperty(Property property, Map<String, Property> properties) {
		properties.put(property.getName(), property);
		return this;
	}
	
	public Style addResponsiveStyle(RQuery responsivenessQuery) {
		Style style = new Style(responsivenessQuery, new LinkedHashMap<>());
		this.stylesByResponsiveness.add(this.stylesByResponsiveness.size() - 1, style);
		return style;
	}
	
	/**
	 * Returns a property given its name from the default property map. The default
	 * property map is always the last map in the list of props entries. If the 
	 * property cannot be found, null is returned.
	 * 
	 * @param key Name of the property to be returned.
	 * @return Property with the given name, or null if no such property exists in 
	 * the default property map.
	 */
	public Property getProperty(String key) {
		Style style = this.stylesByResponsiveness.get(this.stylesByResponsiveness.size() - 1);
		return style.properties.get(key);
	}
	
	/**
	 * Returns an appropriate property given its name whose responsiveness criteria
	 * matches current window dimensions. If no responsiveness criteria matches the
	 * window size, the default properties map will be queried. If the property 
	 * cannot be found in any property map, null is returned.
	 * 
	 * @param key Name of the property to be returned.
	 * @param windowWidth Window width that will be used in the responsiveness query.
	 * @param window HeightWindow width that will be used in the responsiveness query.
	 * @return Property with the given name that matches the responsiveness criteria, 
	 * or null if no such property exists.
	 */
	public Property getProperty(String key, float windowWidth, float windowHeight) {
		for( Style style : this.stylesByResponsiveness ) {
			Property property;
			if( 
				style.responsivenessQuery.check(windowWidth, windowHeight) && 
				(property = style.properties.get(key)) != null 
			) {
				return property;
			}
		}
		return null;
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
		
		if( this.stylesByResponsiveness.size() != p.stylesByResponsiveness.size() ) {
			return false;
		}
		
		for( int i = 0; i < this.stylesByResponsiveness.size(); i++ ) {
			Style style = this.stylesByResponsiveness.get(i);
			Style otherStyle = p.stylesByResponsiveness.get(i);
			
			if( style.properties.size() != otherStyle.properties.size() ) {
				return false;
			}
			
			if( !style.responsivenessQuery.equals(otherStyle.responsivenessQuery) ) {
				return false;
			}
			
			for( Map.Entry<String, Property> en : style.properties.entrySet() ) {
				if( !en.getValue().equals(otherStyle.properties.get(en.getKey())) ) {
					return false;
				}
			}
		}
		
		return true;
	}
}

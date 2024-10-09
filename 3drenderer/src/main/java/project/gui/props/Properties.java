package project.gui.props;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joml.Vector4f;

import project.gui.AGUIElement;

public class Properties {
	public static class Statistics {
		public static final Statistics DEFAULT_EVALUATION = new Statistics();
		public float left;
		public float top;
		
		public float minWidth;
		public float minHeight;
		public float maxWidth;
		public float maxHeight;
		public float width;
		public float height;
		
		public float columns;
		public float rows;
		public Vector4f primaryColor;
		public Vector4f secondaryColor;
		public float lineHeight;
		public float baseline;
		public float anchorX;
		public float anchorY;
		
		public Statistics() {
			this.left = 0;
			this.top = 0;
			
			this.minWidth = 0;
			this.minHeight = 0;
			this.maxWidth = Float.MAX_VALUE;
			this.maxHeight = Float.MAX_VALUE;
			this.width = 0;
			this.height = 0;
			
			this.columns = 1;
			this.rows = 1;
			this.primaryColor = new Vector4f(0, 0, 0, 1);
			this.secondaryColor = new Vector4f(1, 1, 1, 0);
			this.lineHeight = 22;
			this.baseline = 16;
			this.anchorX = 0;
			this.anchorY = 0;
		}
		
		public Statistics(Statistics src) {
			this.left = src.left;
			this.top = src.top;
			
			this.minWidth = src.minWidth;
			this.minHeight = src.minHeight;
			this.maxWidth = src.maxWidth;
			this.maxHeight = src.maxHeight;
			this.width = src.width;
			this.height = src.height;
			
			this.columns = src.columns;
			this.rows = src.rows;
			this.primaryColor = new Vector4f(src.primaryColor);
			this.secondaryColor = new Vector4f(src.secondaryColor);
			this.lineHeight = src.lineHeight;
			this.baseline = src.baseline;
			this.anchorX = src.anchorX;
			this.anchorY = src.anchorY;
		}
		
		
		@Override
		public boolean equals(Object o) {
			if( this == o ) {
				return true;
			}
			
			if( !(o instanceof Statistics) ) {
				return false;
			}
			
			Statistics s = (Statistics) o;
			return (
				this.left == s.left && 
				this.top == s.top && 
				this.minWidth == s.minWidth && 
				this.minHeight == s.minHeight && 
				this.maxWidth == s.maxWidth && 
				this.maxHeight == s.maxHeight && 
				this.width == s.width && 
				this.height == s.height && 
				this.columns == s.columns && 
				this.rows == s.rows && 
				this.primaryColor.equals(s.primaryColor) && 
				this.secondaryColor.equals(s.secondaryColor) && 
				this.lineHeight == s.lineHeight && 
				this.baseline == s.baseline && 
				this.anchorX == s.anchorX && 
				this.anchorY == s.anchorY
			);
		}
	}
	
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
		 * Property has no orientation.
		 */
		NONE,
		
		/**
		 * Property pertains to the horizontal axis.
		 */
		HORIZONTAL,
		
		/**
		 * Property pertains to the vertical axis.
		 */
		VERTICAL
	}
	
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
	
	private static final Set<String> propertySet;
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
		
		propertySet = new HashSet<>();
		propertySet.add(LEFT);
		propertySet.add(TOP);
		propertySet.add(RIGHT);
		propertySet.add(BOTTOM);
		propertySet.add(MIN_WIDTH);
		propertySet.add(MIN_HEIGHT);
		propertySet.add(MAX_WIDTH);
		propertySet.add(MAX_HEIGHT);
		propertySet.add(WIDTH);
		propertySet.add(HEIGHT);
		propertySet.add(COLS);
		propertySet.add(ROWS);
		propertySet.add(PRIMARY_COLOR);
		propertySet.add(SECONDARY_COLOR);
		propertySet.add(ANCHOR_X);
		propertySet.add(ANCHOR_Y);
		propertySet.add(LINE_HEIGHT);
		propertySet.add(BASELINE);
	}
	
	public static Orientation getOrientation(String propertyName) {
		return propertyToOrientation.getOrDefault(propertyName, Orientation.NONE);
	}
	
	public static boolean isProperty(String propertyName) {
		return propertySet.contains(propertyName);
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
		
		public Property getProperty(String key) {
			return this.properties.get(key);
		}
	}
	
	private final AGUIElement owner;
	private final List<Style> stylesByResponsiveness;
	
	public Properties(AGUIElement owner) {
		this.owner = owner;
		Map<String, Property> defaultProperties = new LinkedHashMap<>();
		this.stylesByResponsiveness = new ArrayList<>();
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
	
	public Style addResponsiveStyle(RQuery responsivenessQuery) {
		Style style = new Style(responsivenessQuery, new LinkedHashMap<>());
		this.stylesByResponsiveness.add(this.stylesByResponsiveness.size() - 1, style);
		return style;
	}
	
	public void setProperty(String key, Property property) {
		Style style = this.stylesByResponsiveness.get(this.stylesByResponsiveness.size() - 1);
		style.properties.put(property.getName(), property);
	}
	
	/**
	 * Returns a property given its name from the default style. The default
	 * style is always the last one in the list of styles. If the property cannot 
	 * be found, null is returned.
	 * 
	 * @param key Name of the property to be returned.
	 * @return Property with the given name, or null if no such property exists in 
	 * the default property map.
	 */
	public Property getProperty(String key) {
		Style style = this.stylesByResponsiveness.get(this.stylesByResponsiveness.size() - 1);
		return style.properties.get(key);
	}
	
	public Style getStyle(float windowWidth, float windowHeight) {
		for( Style style : this.stylesByResponsiveness ) {
			if( style.responsivenessQuery.check(windowWidth, windowHeight) ) {
				return style;
			}
		}
		return this.stylesByResponsiveness.get(this.stylesByResponsiveness.size() - 1);
	}
	
	/**
	 * Returns an appropriate property given its name whose responsiveness criteria
	 * matches current window dimensions. If no responsiveness criteria matches the
	 * window size, the default properties map will be queried. If the property 
	 * cannot be found in any property map, null is returned.
	 * 
	 * @param key Name of the property to be returned.
	 * @param windowWidth Window width that will be used in the responsiveness query.
	 * @param windowHeight Window width that will be used in the responsiveness query.
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

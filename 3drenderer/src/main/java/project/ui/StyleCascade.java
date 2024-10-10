package project.ui;

import org.joml.Vector4f;

import project.Window;
import project.shared.logger.Logger;
import project.ui.props.IStyleCascade;
import project.ui.props.Properties;
import project.ui.props.Property;
import project.ui.props.parser.ExpressionRunner;

public class StyleCascade implements IStyleCascade {
	private static final ExpressionRunner RUNNER;
	static {
		RUNNER = new ExpressionRunner();
	}
	
	private final Window window;
	private final Theme activeTheme;
	private Properties.Statistics lastStats;
	
	public StyleCascade(Window window, Theme activeTheme) {
		this.window = window;
		this.activeTheme = activeTheme;
		this.lastStats = new Properties.Statistics();
	}
	
	StyleCascade(StyleCascade src) {
		this.window = src.window;
		this.activeTheme = src.activeTheme;
		this.lastStats = new Properties.Statistics(src.lastStats);
	}
	
	
	@Override
	public Properties.Statistics evaluateProperties(Properties p) {
		Properties.Statistics stats = new Properties.Statistics();
		stats.left = this.lastStats.left + this.evaluateNumeric(this.getProperty(p, Properties.LEFT));
		stats.top = this.lastStats.top + this.evaluateNumeric(this.getProperty(p, Properties.TOP));
		
		stats.minWidth = this.evaluateNumeric(this.getProperty(p, Properties.MIN_WIDTH));
		stats.minHeight = this.evaluateNumeric(this.getProperty(p, Properties.MIN_HEIGHT));
		stats.maxWidth = this.evaluateNumeric(this.getProperty(p, Properties.MAX_WIDTH), Float.MAX_VALUE);
		stats.maxHeight = this.evaluateNumeric(this.getProperty(p, Properties.MAX_HEIGHT), Float.MAX_VALUE);
		
		stats.width = Math.max(
			stats.minWidth, Math.min(stats.maxWidth, this.evaluateNumeric(this.getProperty(p, Properties.WIDTH)))
		);
		stats.height = Math.max(
			stats.minHeight, Math.min(stats.maxHeight, this.evaluateNumeric(this.getProperty(p, Properties.HEIGHT)))
		);
		
		stats.columns = this.evaluateNumeric(this.getProperty(p, Properties.COLS));
		stats.rows = this.evaluateNumeric(this.getProperty(p, Properties.ROWS));
		stats.primaryColor = this.evaluateColor(
				this.getProperty(p, Properties.PRIMARY_COLOR), 
			this.lastStats.primaryColor
		);
		stats.secondaryColor = this.evaluateColor(
			this.getProperty(p, Properties.SECONDARY_COLOR), 
			Properties.Statistics.DEFAULT_EVALUATION.secondaryColor
		);
		
			// It is crucial that these properties are evaluated after the dimensions of 
			// the element have been evaluated (see "width" and "height" above), as their
			// percentages are dependent on the dimensions of the element itself (e.g.
			// percent values in ANCHOR_X correspond to the element rather than the parent)
		this.lastStats = stats;
		stats.anchorX = this.evaluateNumeric(this.getProperty(p, Properties.ANCHOR_X));
		stats.anchorY = this.evaluateNumeric(this.getProperty(p, Properties.ANCHOR_Y));
		stats.lineHeight = this.evaluateNumeric(
			this.getProperty(p, Properties.LINE_HEIGHT), this.lastStats.lineHeight
		);
		stats.baseline = this.evaluateNumeric(
			this.getProperty(p, Properties.BASELINE), this.lastStats.baseline
		);
		
			// Warn of elements that are invisible due to their dimensions
		Logger.get().warn(this, (message) -> {
			String ownerID = p.getOwner().getID();
			
			if( stats.width != 0 && stats.height != 0 ) {
				return false;
			}
			
			if( stats.width == 0 ) {
				message.addMessage("Width of element '" + ownerID + "' is 0!");
			}
			
			if( stats.height == 0 ) {
				message.addMessage("Height of element '" + ownerID + "' is 0!");
			}
			
			return true;
		});
		
		return stats;
	}
	
	private Property getProperty(Properties properties, String propertyName) {
		return properties.getProperty(propertyName, this.window.getWidth(), this.window.getHeight());
	}
	
	public float evaluateNumeric(Property property) {
		return this.evaluateNumeric(property, 0.0f);
	}
	
	@Override
	public float evaluateNumeric(Property property, float defaultValue) {
		return (float) this.evaluate(property, defaultValue);
	}
	
	@Override
	public String evaluateString(Property property, String defaultValue) {
		return (String) this.evaluate(property, defaultValue);
	}
	
	@Override
	public Vector4f evaluateColor(Property property, Vector4f defaultValue) {
		return (Vector4f) this.evaluate(property, defaultValue);
	}
	
	//@Override
	public Object evaluate(Property property, Object defaultValue) {
		if( property == null ) {
			return defaultValue;
		}
		
		float width = this.lastStats.width;
		float height = this.lastStats.height;
		float columns = this.lastStats.columns;
		float rows = this.lastStats.rows;
		
		switch( property.getType() ) {
				// Direct return, no evaluation needed
			case Property.NUMBER:
			case Property.STRING:
			case Property.COLOR:
			case Property.PX: return property.getValue();
			
				// Relative dimensions
			case Property.WPERCENT: return ((float) property.getValue()) * width;
			case Property.HPERCENT: return ((float) property.getValue()) * height;
			
				// Grid dimensions
			case Property.C: 
				return width / columns * ((float) property.getValue());
			case Property.R: 
				return height / rows * ((float) property.getValue());
				
				// Evaluate expression
			case Property.EXPRESSION: 
				return this.evaluate(this.parseExpression(property), null);
				
			/*case Property.THEME: {
				String key = (String) property.getValue();
				Property themeProperty = this.activeTheme.getProperty(key);
				
					// Property not found in theme
				if( themeProperty == null ) {
					// handle property not found in theme
				}
				
				return this.evaluate(themeProperty);
			}*/
		}
		return null;
	}
	
	@Override
	public StyleCascade newCascade() {
		return new StyleCascade(this);
	}
	
	private Property parseExpression(Property expression) {
		return RUNNER.evaluateExpression(
			expression.getName(), (String) expression.getValue(), this
		);
	}
}

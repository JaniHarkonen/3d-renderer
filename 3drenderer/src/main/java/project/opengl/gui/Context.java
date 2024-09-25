package project.opengl.gui;

import org.joml.Vector4f;

import project.Window;
import project.gui.AGUIElement;
import project.gui.props.Properties;
import project.gui.props.Property;
import project.shared.logger.Logger;

class Context {
	float left;
	float top;
	
	float minWidth;
	float minHeight;
	float maxWidth;
	float maxHeight;
	float width;
	float height;
	
	float columns;
	float rows;
	Vector4f primaryColor;
	Vector4f secondaryColor;
	float lineHeight;
	float baseline;
	float anchorX;
	float anchorY;
	
	private final Window window;
	
	Context(Window window) {
		this.window = window;
		
		this.left = Properties.DEFAULT_LEFT;
		this.top = Properties.DEFAULT_TOP;
		
		this.minWidth = Properties.DEFAULT_MIN_WIDTH;
		this.minHeight = Properties.DEFAULT_MIN_HEIGHT;
		this.maxWidth = Properties.DEFAULT_MAX_WIDTH;
		this.maxHeight = Properties.DEFAULT_MAX_HEIGHT;
		this.width = Properties.DEFAULT_WIDTH;
		this.height = Properties.DEFAULT_HEIGHT;
		
		this.columns = Properties.DEFAULT_COLS;
		this.rows = Properties.DEFAULT_ROWS;
		this.primaryColor = Properties.DEFAULT_PRIMARY_COLOR;
		this.secondaryColor = Properties.DEFAULT_SECONDARY_COLOR;
		this.lineHeight = Properties.DEFAULT_LINE_HEIGHT;
		this.baseline = Properties.DEFAULT_BASELINE;
		this.anchorX = Properties.DEFAULT_ANCHOR_X;
		this.anchorY = Properties.DEFAULT_ANCHOR_Y;
	}
	
	Context(Context src) {
		this.window = src.window;
		
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
		this.primaryColor = src.primaryColor;
		this.secondaryColor = src.secondaryColor;
		this.lineHeight = src.lineHeight;
		this.baseline = src.baseline;
		this.anchorX = src.anchorX;
		this.anchorY = src.anchorY;
	}
	
	
	void evaluateProperties(Properties properties) {
		float ww = window.getWidth();
		float wh = window.getHeight();
		float left = this.evaluateFloat(properties.getProperty(Properties.LEFT, ww, wh));
		float top = this.evaluateFloat(properties.getProperty(Properties.TOP, ww, wh));
		
		float minWidth = this.evaluateFloat(properties.getProperty(Properties.MIN_WIDTH, ww, wh));
		float minHeight = this.evaluateFloat(properties.getProperty(Properties.MIN_HEIGHT, ww, wh));
		float maxWidth = this.evaluateFloat(properties.getProperty(Properties.MAX_WIDTH, ww, wh));
		float maxHeight = this.evaluateFloat(properties.getProperty(Properties.MAX_HEIGHT, ww, wh));
		float _width = this.evaluateFloat(properties.getProperty(Properties.WIDTH, ww, wh));
		float _height = this.evaluateFloat(properties.getProperty(Properties.HEIGHT, ww, wh));
		
		float columns = this.evaluateFloat(properties.getProperty(Properties.COLS, ww, wh));
		float rows = this.evaluateFloat(properties.getProperty(Properties.ROWS, ww, wh));
		Vector4f primaryColor = this.evaluateColor(
			properties.getProperty(Properties.PRIMARY_COLOR, ww, wh)
		);
		Vector4f secondaryColor = this.evaluateColor(
			properties.getProperty(Properties.SECONDARY_COLOR, ww, wh)
		);
		float anchorX = this.evaluateFloat(properties.getProperty(Properties.ANCHOR_X, ww, wh));
		float anchorY = this.evaluateFloat(properties.getProperty(Properties.ANCHOR_Y, ww, wh));
		float lineHeight = this.evaluateFloat(
			properties.getProperty(Properties.LINE_HEIGHT, ww, wh)
		);
		float baseline = this.evaluateFloat(properties.getProperty(Properties.BASELINE, ww, wh));
		
		this.left += left;
		this.top += top;
		
		this.minWidth = minWidth;
		this.minHeight = minHeight;
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
		this.width = Math.max(this.minWidth, Math.min(this.maxWidth, _width));
		this.height = Math.max(this.minHeight, Math.min(this.maxHeight, _height));
		
		this.columns = columns;
		this.rows = rows;
		this.primaryColor = primaryColor;
		this.secondaryColor = secondaryColor;
		this.anchorX = anchorX;
		this.anchorY = anchorY;
		this.lineHeight = lineHeight;
		this.baseline = baseline;
		
		Logger.get().warn(this, (message) -> {
			if( width != 0 && height != 0 ) {
				return false;
			}
			
			AGUIElement owner = properties.getOwner();
			
			if( width == 0 ) {
				message.addMessage("Width of element '" + owner.getID() + "' is 0!");
			}
			
			if( height == 0 ) {
				message.addMessage("Height of element '" + owner.getID() + "' is 0!");
			}
			
			message.addMessage("OWNER: " + owner);
			return true;
		});
	}
	
	private float evaluateFloat(Property property) {
		return (float) this.evaluate(property);
	}
	
	private String evaluateString(Property property) {
		return (String) this.evaluate(property);
	}
	
	private Vector4f evaluateColor(Property property) {
		return (Vector4f) this.evaluate(property);
	}
	
	private Object evaluate(Property property) {
		switch( property.getType() ) {
				// Direct return, no evaluation needed
			case Property.NUMBER:
			case Property.STRING:
			case Property.PX: return property.getValue();
				// Calculate width or height depending on prop name
			case Property.PC: {
				String propertyName = property.getName();
				float target;
				
					// Avoid equals(), unless custom properties are introduced
				if(
					propertyName == Properties.LEFT || 
					propertyName == Properties.WIDTH || 
					propertyName == Properties.ANCHOR_X 
				) {
					target = this.width;
				} else {
					target = this.height;
				}
				
				return ((float) property.getValue()) * target;
			}
				// Grid dimensions
			case Property.C: 
				return this.width / this.columns * ((float) property.getValue());
			case Property.R: 
				return this.height / this.rows * ((float) property.getValue());
				// Return primary or secondary color depending on prop name
			case Property.COLOR: {
					// Avoid equals(), unless custom properties are introduced
				Vector4f defaultColor = (property.getName() == Properties.PRIMARY_COLOR) ? 
					Properties.DEFAULT_PRIMARY_COLOR : Properties.DEFAULT_SECONDARY_COLOR;
				return this.returnOrDefault((Vector4f) property.getValue(), defaultColor);
			}
				// Evaluate expression
			case Property.EXPRESSION: 
				return this.evaluate(this.parseExpression((String) property.getValue()));
			case Property.THEME: break;	// to be implemented
		}
		return null;
	}
	
	private Property parseExpression(String expression) {
		Property result = new Property((String) null); // Temporary prop, doesn't have to be named 
		String[] tokens = expression.split(" ");
		return result;
	}
	
	private Object returnOrDefault(Object value, Object defaultValue) {
		return (value == null) ? defaultValue : value;
	}
}

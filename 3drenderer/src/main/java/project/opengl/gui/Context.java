package project.opengl.gui;

import org.joml.Vector4f;

import project.gui.props.Properties;
import project.gui.props.Property;

class Context {
	static final float DEFAULT_LINE_HEIGHT = 22;
	static final float DEFAULT_BASELINE = 16;
	static final Vector4f DEFAULT_PRIMARY_COLOR = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);
	static final Vector4f DEFAULT_SECONDARY_COLOR = new Vector4f(1.0f, 1.0f, 1.0f, 0.0f);
	
	float left;
	float top;
	float width;
	float height;
	float columns;
	float rows;
	float lineHeight;
	float baseline;
	float anchorX;
	float anchorY;
	Vector4f primaryColor;
	Vector4f secondaryColor;
	
	Context() {
		this.left = 0;
		this.top = 0;
		this.width = 0;
		this.height = 0;
		this.columns = 1;
		this.rows = 1;
		this.lineHeight = DEFAULT_LINE_HEIGHT;
		this.baseline = DEFAULT_BASELINE;
		this.anchorX = 0;
		this.anchorY = 0;
		this.primaryColor = DEFAULT_PRIMARY_COLOR;
		this.secondaryColor = DEFAULT_SECONDARY_COLOR;
	}
	
	
	void evaluateProperties(Properties properties) {
		float left = this.evaluateFloat(properties.getProperty(Properties.LEFT));
		float top = this.evaluateFloat(properties.getProperty(Properties.TOP));
		float width = this.evaluateFloat(properties.getProperty(Properties.WIDTH));
		float height = this.evaluateFloat(properties.getProperty(Properties.HEIGHT));
		float columns = this.evaluateFloat(properties.getProperty(Properties.COLS));
		float rows = this.evaluateFloat(properties.getProperty(Properties.ROWS));
		float anchorX = this.evaluateFloat(properties.getProperty(Properties.ANCHOR_X));
		float anchorY = this.evaluateFloat(properties.getProperty(Properties.ANCHOR_Y));
		//float lineHeight = this.evaluateFloat(properties.getProperty(Properties.LINE_HEIGHT));
		//float baseline = this.evaluateFloat(properties.getProperty(Properties.BASELINE));
		Vector4f primaryColor = this.evaluateColor(properties.getProperty(Properties.PRIMARY_COLOR));
		Vector4f secondaryColor = this.evaluateColor(properties.getProperty(Properties.SECONDARY_COLOR));
		
		this.left = left;
		this.top = top;
		this.width = width;
		this.height = height;
		this.columns = columns;
		this.rows = rows;
		this.anchorX = anchorX;
		this.anchorY = anchorY;
		//this.lineHeight = lineHeight;
		//this.baseline = baseline;
		this.primaryColor = primaryColor;
		this.secondaryColor = secondaryColor;
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
				if( propertyName == Properties.WIDTH || propertyName == Properties.ANCHOR_X ) {
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
					DEFAULT_PRIMARY_COLOR : DEFAULT_SECONDARY_COLOR;
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

package project.opengl.gui;

import org.joml.Vector4f;

import project.gui.props4.Properties;
import project.gui.props4.Property;

class Context {
	static final float DEFAULT_LINE_HEIGHT = 22;
	static final float DEFAULT_BASE_LINE = 16;
	static final Vector4f DEFAULT_PRIMARY_COLOR = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);
	static final Vector4f DEFAULT_SECONDARY_COLOR = new Vector4f(1.0f, 1.0f, 1.0f, 0.0f);
	
	float parentLeft;
	float parentTop;
	float parentWidth;
	float parentHeight;
	float parentColumns;
	float parentRows;
	float lineHeight;
	float baseLine;
	Vector4f parentPrimaryColor;
	Vector4f parentSecondaryColor;
	
	Context() {
		this.parentLeft = 0;
		this.parentTop = 0;
		this.parentWidth = 0;
		this.parentHeight = 0;
		this.parentColumns = 1;
		this.parentRows = 1;
		this.lineHeight = DEFAULT_LINE_HEIGHT;
		this.baseLine = DEFAULT_BASE_LINE;
		this.parentPrimaryColor = DEFAULT_PRIMARY_COLOR;
		this.parentSecondaryColor = DEFAULT_SECONDARY_COLOR;
	}
	
	Object evaluate(Property property) {
		switch( property.getType() ) {
			case Property.NUMBER:
			case Property.STRING:
			case Property.PX: return property.getValue();
			case Property.PC: {
				float target = property.getName().equals(Properties.WIDTH) ? 
					this.parentWidth : this.parentHeight;
				return ((float) property.getValue()) * target;
			}
			case Property.C: return this.parentWidth / this.parentColumns * ((float) property.getValue());
			case Property.R: return this.parentHeight / this.parentRows * ((float) property.getValue());
			case Property.COLOR: {
				Vector4f parentColor = property.getName().equals(Properties.PRIMARY_COLOR) ? 
					this.parentPrimaryColor : this.parentSecondaryColor;
				return this.returnOrDefault((Vector4f) property.getValue(), parentColor);
			}
			case Property.EXPRESSION: return this.evaluate(this.parseExpression((String) property.getValue()));
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

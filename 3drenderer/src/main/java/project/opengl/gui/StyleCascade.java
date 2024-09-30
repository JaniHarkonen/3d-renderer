package project.opengl.gui;

import java.util.List;

import org.joml.Vector4f;

import project.Window;
import project.gui.AGUIElement;
import project.gui.props.Properties;
import project.gui.props.Property;
import project.gui.props.parser.Evaluator;
import project.gui.props.parser.ExpressionParser;
import project.gui.props.parser.ExpressionTokenizer;
import project.gui.props.parser.IStyleCascade;
import project.gui.props.parser.Token;
import project.shared.logger.Logger;
import project.utils.DebugUtils;

class StyleCascade implements IStyleCascade {
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
	
	StyleCascade(Window window) {
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
	
	StyleCascade(StyleCascade src) {
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
	
	
	@Override
	public void evaluateProperties(Properties properties) {
		ExpressionTokenizer tokenizer = new ExpressionTokenizer();
		//List<Token> tokens = tokenizer.tokenize(null, "expr(5+6-1*7)");
		//List<Token> tokens = tokenizer.tokenize(null, "expr(1+2-3*3/4+9-7+6+4*2-1/1)");
		//List<Token> tokens = tokenizer.tokenize(null, "expr(min(85752,72,241,042,45324)+1)");
		List<Token> tokens = tokenizer.tokenize(null, "expr(9)");
		ExpressionParser parser = new ExpressionParser();
		Evaluator ast = parser.parse(tokens);
		//DebugUtils.log(this, ast.operator.id, ast.getArgument(0), ast.getArgument(1));
		Property prop = ast.evaluate(this);
		DebugUtils.log(this, prop.getValue(), prop.getType());
		
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
		
			// It is crucial that these properties are evaluated after the dimensions of 
			// the element have been evaluated (see "width" and "height" above), as their
			// percentages are dependent on the dimensions of the element itself (e.g.
			// percent values in ANCHOR_X correspond to the element rather than the parent)
		this.anchorX = this.evaluateFloat(properties.getProperty(Properties.ANCHOR_X, ww, wh));
		this.anchorY = this.evaluateFloat(properties.getProperty(Properties.ANCHOR_Y, ww, wh));
		this.lineHeight = this.evaluateFloat(
			properties.getProperty(Properties.LINE_HEIGHT, ww, wh)
		);
		this.baseline = this.evaluateFloat(properties.getProperty(Properties.BASELINE, ww, wh));
		
			// Warn of elements that are invisible due to their dimensions
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
	
	@Override
	public float evaluateFloat(Property property) {
		return (float) this.evaluate(property);
	}
	
	@Override
	public String evaluateString(Property property) {
		return (String) this.evaluate(property);
	}
	
	@Override
	public Vector4f evaluateColor(Property property) {
		return (Vector4f) this.evaluate(property);
	}
	
	@Override
	public Object evaluate(Property property) {
		switch( property.getType() ) {
				// Direct return, no evaluation needed
			case Property.NUMBER:
			case Property.STRING:
			case Property.PX: return property.getValue();
			
				// Relative dimensions
			case Property.WPC: return ((float) property.getValue()) * this.width;
			case Property.HPC: return ((float) property.getValue()) * this.height;
			
				// Grid dimensions
			case Property.C: 
				return this.width / this.columns * ((float) property.getValue());
			case Property.R: 
				return this.height / this.rows * ((float) property.getValue());
				
				// Return primary or secondary color depending on prop name
			case Property.COLOR: {
				Vector4f defaultColor = property.getName().equals(Properties.PRIMARY_COLOR) ? 
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
		return result;
	}
	
	private Object returnOrDefault(Object value, Object defaultValue) {
		return (value == null) ? defaultValue : value;
	}
}

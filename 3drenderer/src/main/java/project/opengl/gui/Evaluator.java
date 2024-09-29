package project.opengl.gui;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector4f;

import project.gui.props.Property;
import project.opengl.gui.ExpressionParser.OpCode;
import project.shared.logger.Logger;

class Evaluator {
	Evaluator parent;
	OpCode opCode;
	List<Object> arguments;
	
	Evaluator() {
		this.parent = null;
		this.opCode = OpCode.NONE;
		this.arguments = new ArrayList<>();
	}
	
	@SuppressWarnings("incomplete-switch")
	public Property evaluate(Context context) {
		
			// Handle non two-operand operations
		if( this.opCode == OpCode.FUNCTION_CALL ) {
			String functionName = (String) this.arguments.get(0);
			switch( functionName ) {
				case Property.FUNCTION_MIN: return this.min(context);
				case Property.FUNCTION_MAX: return this.max(context);
				case Property.FUNCTION_CLAMP: return this.clamp(context);
				case Property.FUNCTION_RGB: return this.rgb(context);
				case Property.FUNCTION_RGBA:return this.rgba(context);
			}
		} else if( this.opCode == OpCode.NEGATE ) {
			Property arg1 = this.evaluateArgument(this.arguments.get(0), context);
			Object o1 = arg1.getValue();
			String propertyName = arg1.getName();
			
			String errorMessage = "Negation is only possible with numeric values";
			if( !this.requireFloat(errorMessage, arg1) ) {
				return new Property(propertyName, 1.0f, Property.PX);
			}
			
			return new Property(propertyName, -((float) o1), Property.PX);
		}
		
		Property arg1 = this.evaluateArgument(this.arguments.get(0), context);
		Property arg2 = this.evaluateArgument(this.arguments.get(1), context);
		
			// Arbitrary, both arguments should have the same name
		String propertyName = arg1.getName();
		
		switch( this.opCode ) {
			case MUL: 
			case DIV: {
					// Allow multiplication and division only for floats
				String errorMessage = (
					"Multiplication, division and are only possible with numeric "
					+ "values, such as pixels, percentages, rows, columns or numbers"
				);
				if( !this.requireFloat(errorMessage, arg1, arg2) ) {
					break;
				}
				
				float f1 = (float) arg1.getValue();
				float f2 = (float) arg2.getValue();
				
				if( this.opCode == OpCode.DIV ) {
					if( f2 == 0.0f ) {
						Logger.get().error(this, ExpressionParser.FAILED_TO_PARSE, "Division by 0.0");
						break;
					}
					
					f2 = 1.0f / f2; // Flip to divide through multiplication
				}
				
				return new Property(propertyName, f1 * f2, Property.PX);
			}
			case ADD: 
			case SUB: {
				Object o1 = arg1.getValue();
				Object o2 = arg2.getValue();
				
				boolean isStringConcatenation = (
					this.opCode == OpCode.ADD && (o1 instanceof String || o2 instanceof String)
				);
				
				if( this.opCode == OpCode.SUB || !isStringConcatenation ) {
					String errorMessage = (
						"Addition is only possible with numeric values and strings, and "
						+ "subtraction is only possible with numerical values."
					);
					if( this.requireFloat(errorMessage, arg1, arg2) ) {
						break;
					}
				}
				
				if( isStringConcatenation ) {
					return (
						new Property(propertyName, o1.toString() + o2.toString(), Property.STRING)
					);
				}
				
				float f1 = (float) o1;
				float f2 = (this.opCode == OpCode.ADD) ? (float) o2 : -(float) o2; // Flip
				return new Property(propertyName, f1 + f2, Property.PX);
			}
			case NONE: return null;
		}
		
		return new Property(propertyName, 1.0f, Property.PX);
	}
	
	private Property evaluateArgument(Object argument, Context context) {
		if( argument instanceof Evaluator ) {
			return ((Evaluator) argument).evaluate(context);
		}
		
		Property initial = (Property) argument;
		
		if( initial.getType().equals(Property.STRING) ) {
			return initial;
		}
		
		return new Property(initial.getName(), context.evaluate(initial), Property.PX, true);
	}
	
	void addArgument(Evaluator node) {
		this.arguments.add(node);
		node.parent = this;
	}
	
	void addArgument(Object ambiguous) {
		this.arguments.add(ambiguous);
	}
	
	private boolean requireFloat(String errorMessage, Property... properties) {
		for( Property property : properties ) {
			if( !property.isNumeric() ) {
				Logger.get().error(
					this, ExpressionParser.FAILED_TO_PARSE, errorMessage
				);
				return false;
			}
		}
		return true;
	}
	
	private boolean requireArgumentMinCount(int count, String functionName) {
		if( this.arguments.size() >= count ) {
			Logger.get().error(
				this, 
				ExpressionParser.FAILED_TO_PARSE, 
				functionName + "() requires at least one value."
			);
			return false;
		}
		return true;
	}
	
	private boolean requireExactArgumentCount(int count, String functionName) {
		if( this.arguments.size() == count ) {
			Logger.get().error(
				this, 
				ExpressionParser.FAILED_TO_PARSE, 
				functionName + "() requires " + count + " values."
			);
			return false;
		}
		return true;
	}
	
	private Property min(Context context) {
		String propertyName = "";
		float min = 1;
		
		if( this.requireArgumentMinCount(1, "min") ) {
			min = Float.MAX_VALUE;
			for( int i = 1; i < this.arguments.size(); i++ ) {
				Property arg = this.evaluateArgument(this.arguments.get(i), context);
				propertyName = arg.getName();
				
				if( !this.requireFloat("min() only accepts numeric values.", arg) ) {
					break;
				}
				
				min = Math.min(min, (float) arg.getValue());
			}
		}
		
		return new Property(propertyName, min, Property.PX);
	}
	
	private Property max(Context context) {
		String propertyName = "";
		float max = 1;
		
		if( this.requireArgumentMinCount(1, "max") ) {
			max = Float.MIN_VALUE;
			for( int i = 1; i < this.arguments.size(); i++ ) {
				Property arg = this.evaluateArgument(this.arguments.get(i), context);
				propertyName = arg.getName();
				
				if( !this.requireFloat("max() only accepts numeric values.", arg) ) {
					break;
				}
				
				max = Math.max(max, (float) arg.getValue());
			}
		}
		
		return new Property(propertyName, max, Property.PX);
	}
	
	private Property clamp(Context context) {
		String propertyName = "";
		
		if( this.requireExactArgumentCount(3, "clamp") ) {
			Property min = this.evaluateArgument(this.arguments.get(0), context);
			Property value = this.evaluateArgument(this.arguments.get(1), context);
			Property max = this.evaluateArgument(this.arguments.get(2), context);
			boolean areFloats = this.requireFloat(
				"clamp() only accepts numeric values.", min, value, max 
			);
			
			if( !areFloats ) {
				return new Property(propertyName, 1.0f, Property.PX);
			}
			
			float fMin = (float) min.getValue();
			float fValue = (float) max.getValue();
			float fMax = (float) max.getValue();
			return new Property(propertyName, Math.max(fMin, Math.min(fMax, fValue)), Property.PX);
		}
		
		return new Property(propertyName, 1.0f, Property.PX);
	}
	
	private Property rgb(Context context) {
		String propertyName = "";
		
		if( this.requireExactArgumentCount(3, "rgb") ) {
			Property r = this.evaluateArgument(this.arguments.get(0), context);
			Property g = this.evaluateArgument(this.arguments.get(1), context);
			Property b = this.evaluateArgument(this.arguments.get(2), context);
			boolean areFloats = this.requireFloat(
				"rgb() only accepts numeric values.", r, g, b
			);
			
			if( !areFloats ) {
				return new Property(propertyName, 1.0f, Property.COLOR);
			}
			
			float fRed = (float) r.getValue();
			float fGreen = (float) g.getValue();
			float fBlue = (float) b.getValue();
			return new Property(propertyName, new Vector4f(fRed, fGreen, fBlue, 0.0f), Property.COLOR);
		}
		
		return new Property(propertyName, new Vector4f(0.0f), Property.COLOR);
	}
	
	private Property rgba(Context context) {
		String propertyName = "";
		
		if( this.requireExactArgumentCount(4, "rgba") ) {
			Property r = this.evaluateArgument(this.arguments.get(0), context);
			Property g = this.evaluateArgument(this.arguments.get(1), context);
			Property b = this.evaluateArgument(this.arguments.get(2), context);
			Property a = this.evaluateArgument(this.arguments.get(3), context);
			boolean areFloats = this.requireFloat(
				"rgba() only accepts numeric values.", r, g, b, a 
			);
			
			if( !areFloats ) {
				return new Property(propertyName, 1.0f, Property.COLOR);
			}
			
			float fRed = (float) r.getValue();
			float fGreen = (float) g.getValue();
			float fBlue = (float) b.getValue();
			float fAlpha = (float) a.getValue();
			return new Property(propertyName, new Vector4f(fRed, fGreen, fBlue, fAlpha), Property.COLOR);
		}
		
		return new Property(propertyName, new Vector4f(0.0f), Property.COLOR);
	}
}

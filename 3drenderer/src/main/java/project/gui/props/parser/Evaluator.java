package project.gui.props.parser;

import project.gui.props.Property;
import project.shared.logger.Logger;

public class Evaluator extends AEvaluator {

	@Override
	public Property evaluate(IStyleCascade context) {
	
			// Handle non two-operand operations
		if( this.isOperator(Operator.OP_NEGATE) ) {
			Property arg1 = this.arguments.get(0).evaluate(context);
			Object o1 = arg1.getValue();
			String propertyName = arg1.getName();
			
			String errorMessage = "Negation is only possible with numeric values";
			if( !this.requireFloat(errorMessage, arg1) ) {
				return new Property(propertyName, 1.0f, Property.PX);
			}
			
			return new Property(propertyName, -((float) o1), Property.PX);
		}
		
		Property arg1 = this.arguments.get(0).evaluate(context);
		Property arg2 = this.arguments.get(1).evaluate(context);
		String propertyName = arg1.getName();	// Arbitrary getter, both arguments should have the same name
	
		switch( this.operator.id ) {
			case Operator.ID_MUL: 
			case Operator.ID_DIV: {
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
				
				if( this.isOperator(Operator.OP_DIV) ) {
					if( f2 == 0.0f ) {
						Logger.get().error(this, ExpressionParser.FAILED_TO_PARSE, "Division by 0.0");
						break;
					}
					
					return new Property(propertyName, f1 / f2, Property.PX);
				}
				
				return new Property(propertyName, f1 * f2, Property.PX);
			}
			case Operator.ID_ADD: 
			case Operator.ID_SUB: {
				Object o1 = arg1.getValue();
				Object o2 = arg2.getValue();
				
				boolean isStringConcatenation = (
					this.isOperator(Operator.OP_ADD) && 
					(o1 instanceof String || o2 instanceof String)
				);
				
				if( this.isOperator(Operator.OP_SUB) || !isStringConcatenation ) {
					String errorMessage = (
						"Addition is only possible with numeric values and strings, and "
						+ "subtraction is only possible with numerical values."
					);
					if( !this.requireFloat(errorMessage, arg1, arg2) ) {
						break;
					}
				}
				
				if( isStringConcatenation ) {
					return (
						new Property(propertyName, o1.toString() + o2.toString(), Property.STRING)
					);
				}
				
				float f1 = (float) o1;
				float f2 = (float) o2;
				
				if( this.isOperator(Operator.OP_ADD) ) {
					return new Property(propertyName, f1 + f2, Property.PX);
				}
				
				return new Property(propertyName, f1 - f2, Property.PX);
			}
			case Operator.ID_NONE: return null;
		}
	
		return new Property(propertyName, 0, Property.PX);
	}
	
	@Override
	protected AEvaluator createInstance() {
		return new Evaluator();
	}
}

package project.ui.props.parser.functions;

import project.ui.props.IStyleCascade;
import project.ui.props.Property;
import project.ui.props.parser.AEvaluator;
import project.ui.props.parser.ExpressionParser;
import project.utils.DebugUtils;

public class FunctionMin extends AEvaluator {
	public FunctionMin() {
		this.operator = ExpressionParser.OP_FUNCTION_CALL;
	}
	
	@Override
	public Property evaluate(IStyleCascade context) {
		if( this.arguments.size() == 0 ) {
			AEvaluator.logErrorInSignature(
				this, "min", "NUMERIC...", "at least one numeric argument", "no arguments"
			);
			return Property.NULL_PX;
		}
		
		String propertyName = null;
		float min = Float.MAX_VALUE;
		for( int i = 1; i < this.arguments.size(); i++ ) {
			Property arg = this.arguments.get(i).evaluate(context);
			propertyName = arg.getName();	// Extract property name (should be same for all arguments)
			
			if( !arg.isNumeric() ) {
				AEvaluator.logErrorInSignature(
					this, "min", "NUMERIC...", "at least one numeric arugment", Property.getType(arg)
				);
				DebugUtils.log(this, arg.getValue());
				return Property.NULL_PX;
			}
			
			min = Math.min(min, (float) arg.getValue());
		}
		
		return new Property(propertyName, min, Property.PX);
	}

	@Override
	public AEvaluator createInstance() {
		return new FunctionMin();
	}
}

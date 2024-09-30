package project.gui.props.parser.functions;

import project.gui.props.Property;
import project.gui.props.parser.AEvaluator;
import project.gui.props.parser.IStyleCascade;

public class FunctionMax extends AEvaluator {
	@Override
	public Property evaluate(IStyleCascade context) {
		if( this.arguments.size() == 0 ) {
			AEvaluator.logErrorInSignature(
				this, "max", "NUMERIC...", "at least one numeric argument", "no arguments"
			);
			return Property.NULL_PX;
		}
		
		String propertyName = null;
		float max = Float.MIN_VALUE;
		for( int i = 1; i < this.arguments.size(); i++ ) {
			Property arg = this.arguments.get(i).evaluate(context);
			propertyName = arg.getName();	// Extract property name (should be same for all arguments)
			
			if( !arg.isNumeric() ) {
				AEvaluator.logErrorInSignature(
					this, "max", "NUMERIC...", "at least one numeric arugment", Property.getType(arg)
				);
				return Property.NULL_PX;
			}
			
			max = Math.max(max, (float) arg.getValue());
		}
		
		return new Property(propertyName, max, Property.PX);
	}

	@Override
	protected AEvaluator createInstance() {
		return new FunctionMax();
	}
}

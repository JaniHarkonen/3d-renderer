package project.gui.props.parser.functions;

import project.gui.props.Property;
import project.gui.props.parser.AEvaluator;
import project.gui.props.parser.IStyleCascade;
import project.utils.DebugUtils;

public class FunctionMin extends AEvaluator {
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
	protected AEvaluator createInstance() {
		return new FunctionMin();
	}
}

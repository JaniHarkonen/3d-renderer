package project.gui.props.parser;

import project.gui.props.Property;
import project.shared.logger.Logger;

class FunctionRGBA extends Evaluator {
	@Override
	public Property evaluate(IStyleCascade context) {
		String propertyName = null;
		if( this.arguments.size() != 3 ) {
			Logger.get().error(
				this, ExpressionParser.FAILED_TO_PARSE, 
				"clamp() must have three numeric arguments.",
				"Received " + this.arguments.size() + " arguments."
			);
		} else {
			Evaluator aMin = this.getArgument(0);
			Evaluator aValue = this.getArgument(1);
			Evaluator aMax = this.getArgument(2);
			
			Float min = Evaluator.checkIfNumeric(aMin, context);
			Float value = Evaluator.checkIfNumeric(aValue, context);
			Float max = Evaluator.checkIfNumeric(aMax, context);
			
			if( min != null && value != null && max != null ) {
				return new Property(propertyName, Math.min(max, Math.max(min, value)), Property.PX);
			} else {
				Logger.get().error(this, "");
			}
		}
		
		return new Property(propertyName, 0.0f, Property.PX);
	}
}

package project.opengl.gui;

import project.gui.props.Property;
import project.gui.props.parser.IContext;
import project.shared.logger.Logger;

class FunctionMin extends Evaluator {
	@Override
	public Property evaluate(IContext context) {
		String propertyName = null;
		float min = 0;
		
		if( this.arguments.size() == 0 ) {
			Logger.get().error(
				this, ExpressionParser.FAILED_TO_PARSE, 
				"min() requires at least one numeric value.",
				"Received " + this.arguments.size() + " values."
			);
		} else {
			min = Float.MAX_VALUE;
			for( int i = 1; i < this.arguments.size(); i++ ) {
				Property arg = this.arguments.get(i).evaluate(context);
				propertyName = arg.getName();	// Extract property name (should be same for all arguments)
				
				if( !arg.isNumeric() ) {
					Logger.get().error(
						this, ExpressionParser.FAILED_TO_PARSE, 
						"min() only accepts numeric values, instead " 
						+ arg.getType() + " was encountered."
					);
					break;
				}
				
				min = Math.min(min, (float) arg.getValue());
			}
		}
		
		return new Property(propertyName, min, Property.PX);
	}
}

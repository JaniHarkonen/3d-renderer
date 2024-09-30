package project.opengl.gui;

import project.gui.props.Property;
import project.gui.tokenizer.IContext;
import project.shared.logger.Logger;

class FunctionMax extends Evaluator {
	@Override
	public Property evaluate(IContext context) {
		String propertyName = null;
		float max = 0;
		
		if( this.arguments.size() == 0 ) {
			Logger.get().error(
				this, 
				ExpressionParser.FAILED_TO_PARSE, 
				"max() requires at least one numeric argument.",
				"Received 0 arguments."
			);
		} else {
			max = Float.MIN_VALUE;
			for( int i = 1; i < this.arguments.size(); i++ ) {
				Property arg = this.arguments.get(i).evaluate(context);
				propertyName = arg.getName();	// Extract property name (should be same for all arguments)
				
				if( !arg.isNumeric() ) {
					Logger.get().error(
						this, ExpressionParser.FAILED_TO_PARSE, 
						"max() only accepts numeric arguments, instead " 
						+ arg.getType() + " was encountered."
					);
					break;
				}
				
				max = Math.max(max, (float) arg.getValue());
			}
		}
		
		return new Property(propertyName, max, Property.PX);
	}
}

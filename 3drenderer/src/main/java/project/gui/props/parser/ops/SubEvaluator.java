package project.gui.props.parser.ops;

import project.gui.jeemu.Operator;
import project.gui.props.IStyleCascade;
import project.gui.props.Property;
import project.gui.props.parser.AEvaluator;
import project.gui.props.parser.ExpressionParser;
import project.shared.logger.Logger;

public class SubEvaluator extends AEvaluator {
	public SubEvaluator() {
		this.operator = Operator.OP_SUB;
	}

	@Override
	public Property evaluate(IStyleCascade cascade) {
		Property arg1 = AEvaluator.evaluateArgument(this.getArgument(0), cascade);
		Property arg2 = AEvaluator.evaluateArgument(this.getArgument(1), cascade);
		
		if( !arg1.isNumeric() || !arg2.isNumeric() ) {
			Logger.get().error(
				this, 
				ExpressionParser.FAILED_TO_EVALUATE, 
				"Subtraction is only possible with numeric values."
			);
			return Property.NULL_PX;
		}
		
		float f1 = (float) arg1.getValue();
		float f2 = (float) arg2.getValue();
		return new Property(arg1.getName(), f1 - f2, Property.PX);
	}

	@Override
	public AEvaluator createInstance() {
		return new SubEvaluator();
	}
}

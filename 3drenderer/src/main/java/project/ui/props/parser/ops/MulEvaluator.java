package project.ui.props.parser.ops;

import project.shared.logger.Logger;
import project.ui.jeemu.Operator;
import project.ui.props.IStyleCascade;
import project.ui.props.Property;
import project.ui.props.parser.AEvaluator;
import project.ui.props.parser.ExpressionParser;

public class MulEvaluator extends AEvaluator {
	public MulEvaluator() {
		this.operator = Operator.OP_MUL;
	}

	@Override
	public Property evaluate(IStyleCascade cascade) {
		Property arg1 = AEvaluator.evaluateArgument(this.getArgument(0), cascade);
		Property arg2 = AEvaluator.evaluateArgument(this.getArgument(1), cascade);
		
		if( !arg1.isNumeric() || !arg2.isNumeric() ) {
			Logger.get().error(
				this, 
				ExpressionParser.FAILED_TO_EVALUATE, 
				"Multiplication is only possible with numeric values."
			);
			return Property.NULL_PX;
		}
		
		float f1 = (float) arg1.getValue();
		float f2 = (float) arg2.getValue();
		return new Property(arg1.getName(), f1 * f2, Property.PX);
	}

	@Override
	public AEvaluator createInstance() {
		return new MulEvaluator();
	}
}

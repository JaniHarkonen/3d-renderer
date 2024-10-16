package project.ui.props.parser.ops;

import project.shared.logger.Logger;
import project.ui.jeemu.Operator;
import project.ui.props.IStyleCascade;
import project.ui.props.Property;
import project.ui.props.parser.AEvaluator;
import project.ui.props.parser.ExpressionParser;

public class NegationEvaluator extends AEvaluator {
	public NegationEvaluator() {
		this.operator = Operator.OP_NEGATE;
	}

	@Override
	public Property evaluate(IStyleCascade cascade) {
		Property arg1 = AEvaluator.evaluateArgument(this.getArgument(0), cascade);
		String propertyName = arg1.getName();
		
		if( !arg1.isNumeric() ) {
			Logger.get().error(
				this, 
				ExpressionParser.FAILED_TO_EVALUATE, 
				"Negation is only possible with numeric values"
			);
			return Property.NULL_PX;
		}
		
		float value = (float) arg1.getValue();
		return new Property(propertyName, -value, Property.PX);
	}

	@Override
	public AEvaluator createInstance() {
		return new NegationEvaluator();
	}
}

package project.gui.props.parser.ops;

import project.gui.jeemu.Operator;
import project.gui.props.Property;
import project.gui.props.parser.AEvaluator;
import project.gui.props.parser.ExpressionParser;
import project.gui.props.parser.IStyleCascade;
import project.shared.logger.Logger;

public class AddEvaluator extends AEvaluator {
	public AddEvaluator() {
		this.operator = Operator.OP_ADD;
	}

	@Override
	public Property evaluate(IStyleCascade cascade) {
		Property arg1 = AEvaluator.evaluateArgument(this.getArgument(0), cascade);
		Property arg2 = AEvaluator.evaluateArgument(this.getArgument(1), cascade);
		
		boolean isStringConcatenation = (
			this.isOperator(Operator.OP_ADD) && 
			Property.typeOf(arg1, Property.STRING) || Property.typeOf(arg2, Property.STRING)
		);
		
		if( !isStringConcatenation && (!arg1.isNumeric() || !arg2.isNumeric()) ) {
			Logger.get().error(
				this, 
				ExpressionParser.FAILED_TO_EVALUATE,
				"Addition is only possible with numeric values and strings."
			);
			return Property.NULL_PX;
		}
		
		Object v1 = arg1.getValue();
		Object v2 = arg2.getValue();
		
		if( isStringConcatenation ) {
			return (
				new Property(arg1.getName(), v1.toString() + v2.toString(), Property.STRING)
			);
		}
		
		float f1 = (float) v1;
		float f2 = (float) v2;
		return new Property(arg1.getName(), f1 + f2, Property.PX);
	}

	@Override
	public AEvaluator createInstance() {
		return new AddEvaluator();
	}
}

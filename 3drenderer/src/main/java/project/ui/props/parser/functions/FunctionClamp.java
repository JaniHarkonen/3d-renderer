package project.ui.props.parser.functions;

import project.ui.props.IStyleCascade;
import project.ui.props.Property;
import project.ui.props.parser.AEvaluator;
import project.ui.props.parser.ExpressionParser;

public class FunctionClamp extends AEvaluator {
	public FunctionClamp() {
		this.operator = ExpressionParser.OP_FUNCTION_CALL;
	}

	@Override
	public Property evaluate(IStyleCascade context) {
		if( this.arguments.size() != 3 ) {
			AEvaluator.logArgumentCountMismatch(this, "clamp", 3, this.arguments.size());
			return Property.NULL_PX;
		}
		
		Property pMin = AEvaluator.evaluateArgument(this.getArgument(0), context);
		Property pValue = AEvaluator.evaluateArgument(this.getArgument(1), context);
		Property pMax = AEvaluator.evaluateArgument(this.getArgument(2), context);
		Property incorrect;
		
		if( 
			!Property.isNumeric(incorrect = pMin) || !Property.isNumeric(incorrect = pValue) || 
			!Property.isNumeric(incorrect = pMax)
		) {
			AEvaluator.logErrorInSignature(
				this, 
				"clamp", "NUMERIC, NUMERIC, NUMERIC", "3 numeric arguments", Property.getType(incorrect)
			);
			return Property.NULL_PX;
		}
		
		float min = (float) pMin.getValue();
		float value = (float) pValue.getValue();
		float max = (float) pMax.getValue();
		return new Property(pMin.getName(), Math.min(max, Math.max(min, value)), Property.PX);
	}

	@Override
	public AEvaluator createInstance() {
		return new FunctionClamp();
	}
}

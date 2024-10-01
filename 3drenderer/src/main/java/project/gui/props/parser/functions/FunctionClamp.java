package project.gui.props.parser.functions;

import project.gui.props.Property;
import project.gui.props.parser.AEvaluator;
import project.gui.props.parser.ExpressionParser;
import project.gui.props.parser.IStyleCascade;

public class FunctionClamp extends AEvaluator {
	public FunctionClamp() {
		this.operator = ExpressionParser.OP_FUNCTION_CALL;
	}

	@Override
	public Property evaluate(IStyleCascade context) {
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

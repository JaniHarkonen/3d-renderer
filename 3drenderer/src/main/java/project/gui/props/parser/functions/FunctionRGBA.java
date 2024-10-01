package project.gui.props.parser.functions;

import org.joml.Vector4f;

import project.gui.props.Property;
import project.gui.props.parser.AEvaluator;
import project.gui.props.parser.ExpressionParser;
import project.gui.props.parser.IStyleCascade;

public class FunctionRGBA extends AEvaluator {
	public FunctionRGBA() {
		this.operator = ExpressionParser.OP_FUNCTION_CALL;
	}
	
	@Override
	public Property evaluate(IStyleCascade context) {
		Property pRed = AEvaluator.evaluateArgument(this.getArgument(0), context);
		Property pGreen = AEvaluator.evaluateArgument(this.getArgument(1), context);
		Property pBlue = AEvaluator.evaluateArgument(this.getArgument(2), context);
		Property pAlpha = AEvaluator.evaluateArgument(this.getArgument(3), context);
		Property incorrect;
		
		if( 
			!Property.isNumeric(incorrect = pRed) || !Property.isNumeric(incorrect = pGreen) || 
			!Property.isNumeric(incorrect = pBlue) || !Property.isNumeric(incorrect = pAlpha) 
		) {
			AEvaluator.logErrorInSignature(
				this, 
				"rgba", "NUMERIC, NUMERIC, NUMERIC, NUMERIC", "4 numeric arguments", 
				Property.getType(incorrect)
			);
			return Property.NULL_COLOR;
		}
		
		float red = (float) pRed.getValue();
		float green = (float) pGreen.getValue();
		float blue = (float) pBlue.getValue();
		float alpha = (float) pAlpha.getValue();
		return new Property(pRed.getName(), new Vector4f(red, green, blue, alpha), Property.COLOR);
	}

	@Override
	public AEvaluator createInstance() {
		return new FunctionRGBA();
	}
}

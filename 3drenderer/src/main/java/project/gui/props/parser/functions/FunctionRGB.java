package project.gui.props.parser.functions;

import org.joml.Vector4f;

import project.gui.props.Property;
import project.gui.props.parser.AEvaluator;
import project.gui.props.parser.ExpressionParser;
import project.gui.props.parser.IStyleCascade;

public class FunctionRGB extends AEvaluator {
	public FunctionRGB() {
		this.operator = ExpressionParser.OP_FUNCTION_CALL;
	}
	
	@Override
	public Property evaluate(IStyleCascade context) {
		if( this.arguments.size() != 3 ) {
			AEvaluator.logArgumentCountMismatch(this, "rgb", 3, this.arguments.size());
			return Property.NULL_COLOR;
		}
		
		Property pRed = AEvaluator.evaluateArgument(this.getArgument(0), context);
		Property pGreen = AEvaluator.evaluateArgument(this.getArgument(1), context);
		Property pBlue = AEvaluator.evaluateArgument(this.getArgument(2), context);
		Property incorrect;
		
		if( 
			!Property.isNumeric(incorrect = pRed) || !Property.isNumeric(incorrect = pGreen) || 
			!Property.isNumeric(incorrect = pBlue) 
		) {
			AEvaluator.logErrorInSignature(
				this, 
				"rgb", "NUMERIC, NUMERIC, NUMERIC", "3 numeric arguments", 
				Property.getType(incorrect)
			);
			return Property.NULL_COLOR;
		}
		
		float red = (float) pRed.getValue() / 255f;
		float green = (float) pGreen.getValue() / 255f;
		float blue = (float) pBlue.getValue() / 255f;
		return new Property(pRed.getName(), new Vector4f(red, green, blue, 1.0f), Property.COLOR);
	}

	@Override
	public AEvaluator createInstance() {
		return new FunctionRGB();
	}
}

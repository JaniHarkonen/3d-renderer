package project.gui.props.parser.functions;

import org.joml.Vector4f;

import project.gui.props.Property;
import project.gui.props.parser.AEvaluator;
import project.gui.props.parser.IStyleCascade;

public class FunctionRGB extends AEvaluator {
	@Override
	public Property evaluate(IStyleCascade context) {
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
		
		float red = (float) pRed.getValue();
		float green = (float) pGreen.getValue();
		float blue = (float) pBlue.getValue();
		return new Property(pRed.getName(), new Vector4f(red, green, blue, 1.0f), Property.COLOR);
	}

	@Override
	protected AEvaluator createInstance() {
		return new FunctionRGB();
	}
}

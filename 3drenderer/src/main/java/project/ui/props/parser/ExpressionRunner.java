package project.ui.props.parser;

import project.ui.jeemu.Tokenizer;
import project.ui.props.IStyleCascade;
import project.ui.props.Property;
import project.utils.DebugUtils;

public class ExpressionRunner {

	public Property evaluateExpression(
		String propertyName, String expression, IStyleCascade cascade
	) {
		ExpressionTokenizer tokenizer = new ExpressionTokenizer();
		Tokenizer.Result tokenizerResult = tokenizer.tokenize(expression);
		
		if( !tokenizerResult.wasSuccessful ) {
			DebugUtils.log(this, "failed");
			return Property.NULL_PX;
		}
		
		//List<Token> tokens = tokenizer.tokenize(propertyName, expression);
		ExpressionParser parser = new ExpressionParser();
		//AEvaluator parse = parser.parse(tokens);
		AEvaluator parse = parser.parse(propertyName, tokenizerResult.tokens);
		return parse.evaluate(cascade);
	}
}

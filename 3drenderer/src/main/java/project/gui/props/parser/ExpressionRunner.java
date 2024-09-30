package project.gui.props.parser;

import java.util.List;

import project.gui.props.Property;

public class ExpressionRunner {

	public Property evaluateExpression(
		String propertyName, String expression, IStyleCascade cascade
	) {
		ExpressionTokenizer tokenizer = new ExpressionTokenizer();
		List<Token> tokens = tokenizer.tokenize(propertyName, expression);
		ExpressionParser parser = new ExpressionParser();
		return parser.parse(tokens).evaluate(cascade);
	}
}

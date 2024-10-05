package project.gui.props.parser;

import project.gui.jeemu.Tokenizer;
import project.shared.logger.Logger;

public class ExpressionTokenizer {
	public static final String FAILED_TO_TOKENIZE = "Failed to tokenize expression!";
	public static final String EXPRESSION_START = "expr";
	public static final String EXPRESSION_START_ABBR = "e";
	
	public Tokenizer.Result tokenize(String jeemuExpression) {
		Tokenizer tokenizer = new Tokenizer();
		int indexOfFirstParenthesis = jeemuExpression.indexOf('(');
		String expressionStart = null;
		
		if(
			indexOfFirstParenthesis == -1 || 
			!(expressionStart = jeemuExpression.substring(0, indexOfFirstParenthesis))
			.equals(EXPRESSION_START_ABBR) &&
			!expressionStart.equals(EXPRESSION_START)
		) {
			String errorMessage = 
				"Expression must begin with '" + EXPRESSION_START + "(' or '" 
				+ EXPRESSION_START_ABBR + "(' and its calculation must be confined "
				+ "between parenthesis.";
			this.tokenizerError(errorMessage, jeemuExpression);
			return tokenizer.error(errorMessage);
		}
		
			// Early exit if not ending in ')'
		if(jeemuExpression.charAt(jeemuExpression.length() - 1) != ')' ) {
			String errorMessage = "Expression must end with a closing parenthesis ')'.";
			this.tokenizerError(errorMessage, jeemuExpression);
			return tokenizer.error(errorMessage);
		}
		
		Tokenizer.Result result = 
			tokenizer.tokenize(jeemuExpression, expressionStart.length() + 1, 1);
		
		// No tokens, there is nothing to evaluate
		if( result.tokens.size() == 1 ) {
			String errorMessage = "Expression contains no calculation.";
			this.tokenizerError(errorMessage, jeemuExpression);
			return tokenizer.error(errorMessage);
		}
		
		if( !result.wasSuccessful ) {
			Logger.get().error(this, FAILED_TO_TOKENIZE, result.errorMessage);
		}
		
		return result;
	}
	
	private void tokenizerError(String errorMessage, String expression) {
		Logger.get().error(
			this, FAILED_TO_TOKENIZE, errorMessage, "EXPRESSION: " + expression
		);
	}
}

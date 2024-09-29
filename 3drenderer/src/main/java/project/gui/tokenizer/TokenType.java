package project.gui.tokenizer;

public enum TokenType {
	/**
	 * Token's value is a Property that is immediately evaluable.
	 */
	EVALUABLE,
	
	/**
	 * Token's value is a special character (char), and will never be evaluable.
	 */
	SPECIAL_CHARACTER,
	
	/**
	 * Token's value is an operator (Operator), and will never be evaluable.
	 */
	OPERATOR,
	
	
	/**
	 * Token's value is the name of a built-in function (String), and will never be evaluable.
	 */
	FUNCTION
}

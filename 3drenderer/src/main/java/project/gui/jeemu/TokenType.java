package project.gui.jeemu;

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
	FUNCTION,
	
	/**
	 * Token's value is used to identify a custom item (String), and will never be evaluable.
	 */
	IDENTIFIER,
	
	/**
	 * Token's value is a keyword (String), and will never be evaluable.
	 */
	KEYWORD,
	
	/**
	 * Token's value is a responsiveness query keyword (String), and will never be evaluable.
	 */
	RQUERY_KEYWORD,
	
	/**
	 * Token's value is a responsiveness query (String), and will never be evaluable.
	 */
	RQUERY,
	
	/**
	 * Token's value is a special character (char) that denotes the beginning of an expression,
	 * and will never be evaluable.
	 */
	EXPRESSION_START,
	
	/**
	 * Token's value is a special character (char) that denotes the end of an expression,
	 * and will never be evaluable.
	 */
	EXPRESSION_END,
	
	/**
	 * Token's value is a special character (char) that denotes the separation of two 
	 * expressions, and will never be evaluable.
	 */
	EXPRESSION_SEPARATOR,
	
	/**
	 * Token's value is a special character (char) that denotes the seaparation of two 
	 * property fields, and will never be evaluable.
	 */
	FIELD_SEPARATOR,
	
	/**
	 * Token's value is a special character (char) that denotes the beginning of a block,
	 * and will never be evaluable.
	 */
	BLOCK_START,

	/**
	 * Token's value is a special character (char) that denotes the end of a block,
	 * and will never be evaluable.
	 */
	BLOCK_END
}

package project.gui.tokenizer;

public enum TokenType {
	/**
	 * Token's value is a Property that is immediately evaluable.
	 */
	EVALUABLE,
	
	/**
	 * Token's value is a percentage (float), however, is not yet evaluable as it's orientation
	 * (width, height, element dimension) cannot be determined yet.
	 */
	PERCENTAGE,
	
	/**
	 * Token's value is a special character (char), and will never be evaluable.
	 */
	SPECIAL_CHARACTER,
	
	/**
	 * Token's value is the name of a built-in function (String), and will never be evaluable.
	 */
	FUNCTION
}

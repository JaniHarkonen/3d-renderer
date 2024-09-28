package project.gui.tokenizer;

import java.util.ArrayList;
import java.util.List;

import project.gui.props.Property;
import project.shared.logger.Logger;

public class ExpressionTokenizer {

	public List<Token> tokenize(String propertyName, String expression) {
		final String expressionStart = Property.EXPRESSION + "(";
		final int length = expression.length();
		
		if( expression.length() < expressionStart.length() ) {
			Logger.get().error(
				this, 
				"Failed to tokenize expression: " + expression, 
				"Expression must begin with 'expr' and its calculation must be confined "
				+ "between parenthesis."
			);
			return null;
		}
		
		if( expression.charAt(expression.length() - 1) != ')' ) {
			Logger.get().error(
				this, 
				"Failed to tokenize expression: " + expression, 
				"Expression must end with a closing parenthesis ')'."
			);
			return null;
		}
		
		List<Token> tokens = new ArrayList<>();
		
		int cursor = expressionStart.length();
		while( cursor < length ) {
			char charAt = expression.charAt(cursor);
			
			if( this.isIdentifier(charAt) ) {
					// Must be a function call
				char callChar = charAt;
				int end = cursor;
				while( this.isIdentifier(callChar) ) {
					callChar = expression.charAt(++end);
				}
				
					// Check if function is valid
				String functionName = expression.substring(cursor, end);
				if( !Property.functionExists(functionName) ) {
					Logger.get().error(
						this, 
						"Failed to tokenize expression: " + expression, 
						"Built-in function '" + functionName + "' does not exist."
					);
					return null;
				}
				
				tokens.add(new Token(TokenType.FUNCTION, functionName));
				cursor = end - 1;
			} else if( this.isDigit(charAt) ) {
					// Must be a numeric value
				boolean isDecimalFound = false;
				float value = 0;
				float factor = 10;
				String type = "";
				
				while( cursor < length ) {
					char numberChar = expression.charAt(cursor);
					
						// Handle decimal point
					if( numberChar == '.' ) {
						if( isDecimalFound ) {
							Logger.get().error(
								this, 
								"Failed to tokenize expression: " + expression, 
								"Expression contains a value with multiple decimal points."
							);
							return null;
						}
						
						factor = 0.1f;
						isDecimalFound = true;
					} else if( this.isDigit(numberChar) ) {
							// Handle digits
						float digit = numberChar - '0';
						if( factor < 1 ) {
							value += digit * factor;
							factor /= 10;
						} else {
							value = value * factor + digit;
						}
					} else if( this.isLetter(numberChar) ) {
							// Handle property type
						type += Character.toString(numberChar).toLowerCase();
					} else if( numberChar == '%' ) {
							// Handle percent (not evaluable as of yet)
						tokens.add(new Token(TokenType.PERCENTAGE, value));
						break;
					} else {
						// Validate property type
						switch( type ) {
							case Property.PX:
							case Property.C:
							case Property.R: {
								tokens.add(new Token(TokenType.EVALUABLE, new Property(null, value, type)));
							} break;
							case "": {
								tokens.add(
									new Token(TokenType.EVALUABLE, new Property(null, value, Property.NUMBER))
								);
							} break;
							
							default: {
								Logger.get().error(
									this, 
									"Failed to tokenize expression: " + expression, 
									"Invalid property type '" + type + "'."
								);
								return null;
							}
						}
						
						cursor--;
						break;
					}
					
					cursor++;
				}
			} else if( charAt == '"' || charAt == '\'' || charAt == '`' ) {
					// Must be a string
				int end = cursor + 1;
				boolean ignoreNext = false;
				char endChar = 0;
				while( end < length ) {
					endChar = expression.charAt(end);
					
					if( endChar == charAt && !ignoreNext ) {
						break;
					} else if( endChar == '\\' ) {
						ignoreNext = !ignoreNext;
						end++;
						continue;
					}
					
					ignoreNext = false;
					end++;
				}
				
				if( ignoreNext || endChar != charAt ) {
					Logger.get().error(
						this, 
						"Failed to tokenize expression: " + expression, 
						"Expression contains a non-closed string."
					);
					return null;
				}
				
				Property evaluable = 
					new Property(null, expression.substring(cursor + 1, end), Property.STRING);
				tokens.add(new Token(TokenType.EVALUABLE, evaluable));
				cursor = end;
			} else if( this.isSpecialCharacter(charAt) ) {
					// Allowed special characters
				tokens.add(new Token(TokenType.SPECIAL_CHARACTER, charAt));
			}
			
			cursor++;
		}
		
		if( tokens.size() == 0 ) {
			Logger.get().error(
				this, 
				"Failed to tokenize expression: " + expression, 
				"Expression contains no calculation."
			);
			return null;
		}
		
		return tokens;	// NOTICE: This result will contain the final parenthesis
	}
	
	private boolean isLetter(char c) {
		return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
	}
	
	private boolean isDigit(char c) {
		return (c >= '0' && c <= '9');
	}
	
	private boolean isSpecialCharacter(char c) {
		return (
			c == '(' || c == ')' || c == '+' || c == '-' || c == '*' || c == '/' 
			|| c == '%' || c == ','
		);
	}
	
	private boolean isIdentifier(char c) {
		return this.isLetter(c) || c == '_';
	}
}

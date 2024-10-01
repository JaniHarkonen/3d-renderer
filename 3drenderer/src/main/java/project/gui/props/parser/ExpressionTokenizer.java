package project.gui.props.parser;

import java.util.ArrayList;
import java.util.List;

import project.gui.props.Properties;
import project.gui.props.Property;
import project.shared.logger.Logger;

public class ExpressionTokenizer {
	public static final String FAILED_TO_TOKENIZE = "Failed to tokenize expression!";
	public static final String EXPRESSION_START = "expr";
	public static final String EXPRESSION_START_ABBR = "e";
	
	private static void tokenizerError(
		Object me, String errorMessage, String expression
	) {
		Logger.get().error(
			me, FAILED_TO_TOKENIZE, errorMessage, "EXPRESSION: " + expression
		);
	}
	
	
	private List<Token> tokens;
	private int cursor;
	private String expression;
	private String propertyName;
	private int parenthesisCount;
	
	public ExpressionTokenizer() {
		this.tokens = null;
		this.cursor = 0;
		this.expression = null;
		this.propertyName = null;
		this.parenthesisCount = 0;
	}
	

	public List<Token> tokenize(String propertyName, String ex) {
		this.tokens = new ArrayList<>();
		this.cursor = 0;
		this.expression = ex;
		this.propertyName = propertyName;
		this.parenthesisCount = 1;
		
		final int length = this.expression.length();
		
			// Check expression start
		int indexOfFirstParenthesis = this.expression.indexOf('(');
		String expressionStart = null;
		if(
			indexOfFirstParenthesis == -1 || 
			!(expressionStart = this.expression.substring(0, indexOfFirstParenthesis))
			.equals(EXPRESSION_START_ABBR) &&
			!expressionStart.equals(EXPRESSION_START)
		) {
			tokenizerError(
				this,
				"Expression must begin with '" + EXPRESSION_START + "(' or '" 
				+ EXPRESSION_START_ABBR + "(' and its calculation must be confined "
				+ "between parenthesis.",
				this.expression
			);
			return null;
		}
		
			// Early exit if not ending in ')'
		if(this. expression.charAt(this.expression.length() - 1) != ')' ) {
			tokenizerError(
				this,
				"Expression must end with a closing parenthesis ')'.",
				this.expression
			);
			return null;
		}
		
		this.cursor = expressionStart.length() + 1;
		while( this.cursor < length ) {
			char charAt = this.expression.charAt(this.cursor);
			boolean wasSuccessful = false;
			
			if( this.isIdentifier(charAt) ) {
				wasSuccessful = this.function(charAt);
			} else if( this.isDigit(charAt) ) {
				wasSuccessful = this.numeric(charAt);
			} else if( charAt == '"' || charAt == '\'' || charAt == '`' ) {
				wasSuccessful = this.string(charAt);
			} else if( this.isOperator(charAt) ) {
				this.token(new Token(TokenType.OPERATOR, Operator.getOperator(charAt)));
				wasSuccessful = true;
			} else if( this.isSpecialCharacter(charAt) ) {
				wasSuccessful = this.specialCharacter(charAt);
			}
			
			if( !wasSuccessful ) {
				return null;
			}
			
			this.cursor++;
		}
		
		if( tokens.size() == 0 ) {
			tokenizerError(
				this,
				"Expression contains no calculation.",
				this.expression
			);
			return null;
		}
		
		if( parenthesisCount > 0 ) {
			tokenizerError(
				this,
				"Expected " + this.parenthesisCount + " more closing parenthesis.",
				this.expression
			);
			return null;
		}
		
		return this.tokens;	// NOTICE: This result will contain the final parenthesis
	}
	
	private boolean function(char charAt) {
		char callChar = charAt;
		int end = this.cursor;
		while( this.isIdentifier(callChar) ) {
			callChar = this.charAt(++end);
		}
		
			// Check if function is valid
		String functionName = this.expression.substring(this.cursor, end);
		if( !Property.functionExists(functionName) ) {
			tokenizerError(
				this,
				"Built-in function '" + functionName + "' does not exist.",
				this.expression
			);
			return false;
		}
		
		this.token(new Token(TokenType.FUNCTION, functionName));
		this.cursor = end - 1;
		return true;
	}
	
	private boolean numeric(char charAt) {
		boolean isDecimalFound = false;
		float value = 0;
		float factor = 10;
		String type = "";
		
		while( this.cursor < this.expression.length() ) {
			char numberChar = this.charAtCursor();
			
				// Handle decimal point
			if( numberChar == '.' ) {
				if( isDecimalFound ) {
					tokenizerError(
						this,
						"Expression contains a value with multiple decimal points.",
						this.expression
					);
					return false;
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
				Properties.Orientation orientation = Properties.getOrientation(this.propertyName);
				boolean isHorizontal = (orientation == Properties.Orientation.HORIZONTAL);
				type = isHorizontal ? Property.WPERCENT : Property.HPERCENT;
				Property property = new Property(this.propertyName, value / 100f, type);
				
				this.token(new Token(TokenType.EVALUABLE, property));
				break;
			} else {
					// Validate property type
				switch( type ) {
						// Numeric values
					case Property.PX:
					case Property.C:
					case Property.R: {
						this.token(
							new Token(TokenType.EVALUABLE, new Property(this.propertyName, value, type))
						);
					} break;
						// Ambiguous numeric value
					case "": {
						Token token = new Token(
							TokenType.EVALUABLE, 
							new Property(this.propertyName, value, Property.NUMBER)
						);
						this.token(token);
					} break;
					
						// Invalid property type
					default: {
						tokenizerError(
							this,
							"Invalid property type '" + type + "'.",
							this.expression
						);
						return false;
					}
				}
				
				this.cursor--;
				break;
			}
			
			this.cursor++;
		}
		
		return true;
	}
	
	private boolean string(char charAt) {
		int end = this.cursor + 1;
		boolean ignoreNext = false;
		char endChar = 0;
		while( end < this.expression.length() ) {
			endChar = this.charAt(end);
			
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
		
			// String didn't close
		if( ignoreNext || endChar != charAt ) {
			tokenizerError(
				this,
				"String beginning with character " + charAt 
				+ ", must also end with that character.",
				this.expression
			);
			return false;
		}
		
		String value = this.expression.substring(this.cursor + 1, end);
		Property evaluable = new Property(this.propertyName, value, Property.STRING);
		this.token(new Token(TokenType.EVALUABLE, evaluable));
		this.cursor = end;
		
		return true;
	}
	
	private boolean specialCharacter(char charAt) {
		if( charAt == '(' ) {
			this.parenthesisCount++;
		} else if( charAt == ')' ) {
			this.parenthesisCount--;
		}
		
			// Count below zero indicates extra closing parenthesis
		if( this.parenthesisCount < 0 ) {
			tokenizerError(
				this,
				"Encountered unexpected closing parenthesis.",
				this.expression
			);
			return false;
		}
		
		this.token(new Token(TokenType.SPECIAL_CHARACTER, charAt));
		return true;
	}
	
	private char charAt(int index) {
		return this.expression.charAt(index);
	}
	
	private char charAtCursor() {
		return this.charAt(this.cursor);
	}
	
	private void token(Token token) {
		this.tokens.add(token);
	}
	
	private boolean isLetter(char c) {
		return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
	}
	
	private boolean isDigit(char c) {
		return (c >= '0' && c <= '9');
	}
	
	private boolean isSpecialCharacter(char c) {
		return (
			c == '(' || c == ')' || c == ','
		);
	}
	
	private boolean isOperator(char c) {
		return (
			c == '+' || c == '-' || c == '*' || c == '/'
		);
	}
	
	private boolean isIdentifier(char c) {
		return this.isLetter(c) || c == '_';
	}
}

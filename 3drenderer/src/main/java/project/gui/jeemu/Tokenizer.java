package project.gui.jeemu;

import java.util.ArrayList;
import java.util.List;

import project.gui.props.Property;
import project.gui.props.parser.Operator;
import project.gui.props.parser.PercentageBuilder;
import project.gui.props.parser.PropertyBuilder;
import project.gui.props.parser.Token;
import project.gui.props.parser.TokenType;

public class Tokenizer {
	public class Result {
		public boolean wasSuccessful;
		public String errorMessage;
		public int errorAt;
		public List<Token> tokens;
		
		public Result(String errorMessage, int errorAt) {
			this.wasSuccessful = false;
			this.errorMessage = errorMessage;
			this.errorAt = errorAt;
		}
		
		public Result(List<Token> tokens) {
			this.wasSuccessful = true;
			this.errorMessage = "";
			this.errorAt = -1;
			this.tokens = tokens;
		}
	}
	
	private List<Token> tokens;
	private String jeemuString;
	private int cursor;
	private int parenthesisCount;
	private int currentLine;
	private int positionInLine;
	
	public Tokenizer() {
		this.tokens = new ArrayList<>();
		this.jeemuString = null;
		this.cursor = 0;
		this.parenthesisCount = 0;
		this.currentLine = 0;
		this.positionInLine = 0;
	}
	
	
	public Result tokenize(String jeemuString) {
		return this.tokenize(jeemuString, 0, 0);
	}
	
	public Result tokenize(String jeemuString, int startPosition, int parenthesisCount) {
		this.tokens = new ArrayList<>();
		this.jeemuString = jeemuString;
		this.cursor = startPosition;
		this.parenthesisCount = parenthesisCount;
		this.currentLine = 1;
		this.positionInLine = 0;
		
		while( this.cursor < this.jeemuString.length() ) {
			char charAt = this.jeemuString.charAt(this.cursor);
			
			if( charAt == '\n' ) {
				this.newLine();
				this.advance();
				continue;
			}
			
			Result error = null;
			
			if( this.isIdentifier(charAt) ) {
				error = this.function(charAt);
			} else if( this.isDigit(charAt) ) {
				error = this.numeric(charAt);
			} else if( charAt == '"' || charAt == '\'' || charAt == '`' ) {
				error = this.string(charAt);
			} else if( this.isOperator(charAt) ) {
				this.token(new Token(TokenType.OPERATOR, Operator.getOperator(charAt)));
				error = null;
			} else if( this.isSpecialCharacter(charAt) ) {
				error = this.specialCharacter(charAt);
			} else if( this.charAtCursor() == '.' ) {
				return this.tokenizerError(
					"Numeric values starting with decimal point are not allowed.", this.cursor
				);
			} else if( !this.isEmpty(charAt) ) {
				return this.tokenizerError(
					"Unexpected character " + charAt + " encountered.", this.cursor
				);
			}
			
			if( error != null ) {
				return error;
			}
			
			this.advance();
		}
		
			// Make sure all parenthesis were closed
		if( this.parenthesisCount > 0 ) {
			return tokenizerError(
				"Expected " + this.parenthesisCount + " more closing parenthesis.", 
				this.cursor
			);
		}
		
		return new Result(this.tokens);
	}
	
	private Result function(char charAt) {
		int start = this.cursor;
		while( this.isIdentifier(this.charAtCursor()) ) {
			this.advance();
		}
		
			// Check if function is valid
		String functionName = this.jeemuString.substring(start, this.cursor);
		if( !Property.functionExists(functionName) ) {
			return this.tokenizerError(
				"Built-in function '" + functionName + "' does not exist.", this.cursor
			);
		}
		
		this.token(new Token(TokenType.FUNCTION, functionName));
		this.backtrack();
		return null;
	}
	
	private Result numeric(char charAt) {
		boolean isDecimalFound = false;
		float value = 0;
		float factor = 10;
		String type = "";
		
		while( this.cursor < this.jeemuString.length() ) {
			char numberChar = this.charAtCursor();
			
				// Handle decimal point
			if( numberChar == '.' ) {
				if( isDecimalFound ) {
					return this.tokenizerError(
						"Expression contains a value with multiple decimal points.", this.cursor
					);
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
					// Handle percent
				PercentageBuilder builder = new PercentageBuilder(value / 100f);
				this.token(new Token(TokenType.EVALUABLE, builder));
				break;
			} else {
					// Validate property type
				switch( type ) {
						// Numeric values
					case Property.PX:
					case Property.C:
					case Property.R: {
						PropertyBuilder builder = new PropertyBuilder(value, type);
						this.token(
							new Token(TokenType.EVALUABLE, builder)
						);
					} break;
						// Ambiguous numeric value
					case "": {
						PropertyBuilder builder = new PropertyBuilder(value, Property.NUMBER);
						Token token = new Token(TokenType.EVALUABLE, builder);
						this.token(token);
					} break;
					
						// Invalid property type
					default:
						return this.tokenizerError("Invalid property type '" + type + "'.", this.cursor);
				}
				
				this.backtrack();
				break;
			}
			
			this.advance();
		}
		
		return null;
	}
	
	private Result string(char charAt) {
		int start = this.cursor + 1;
		boolean ignoreNext = false;
		char endChar = 0;
		
		while( (endChar = this.charAtCursor()) != 0 ) {
			if( endChar == charAt && !ignoreNext ) {
				break;
			} else if( endChar == '\\' ) {
				ignoreNext = !ignoreNext;
				this.advance();
				continue;
			}
			
			ignoreNext = false;
			this.advance();
		}
		
			// String didn't close
		if( ignoreNext || endChar != charAt ) {
			return this.tokenizerError(
				"String beginning with character " + charAt + " must also end with that character.", 
				this.cursor
			);
		}
		
		String value = this.jeemuString.substring(start, this.cursor + 1);
		PropertyBuilder builder = new PropertyBuilder(value, Property.STRING);
		this.token(new Token(TokenType.EVALUABLE, builder));
		
		return null;
	}
	
	private Result specialCharacter(char charAt) {
		if( charAt == '(' ) {
			this.parenthesisCount++;
		} else if( charAt == ')' ) {
			this.parenthesisCount--;
		}
		
			// Count below zero indicates extra closing parenthesis
		if( this.parenthesisCount < 0 ) {
			return this.tokenizerError(
				"Encountered unexpected closing parenthesis.", this.cursor
			);
		} 
			// Expression shouldn't close before the last character in expression
		else if( this.parenthesisCount == 0 && this.cursor < this.jeemuString.length() - 1 ) {
			return this.tokenizerError(
				"Expression has content outside its parenthesis.", this.cursor
			);
		}
		
		this.token(new Token(TokenType.SPECIAL_CHARACTER, charAt));
		return null;
	}
	
	private char charAt(int index) {
		if( index < 0 || index >= this.jeemuString.length() ) {
			return 0;
		}
		return this.jeemuString.charAt(index);
	}
	
	private char charAtCursor() {
		return this.charAt(this.cursor);
	}
	
	private void token(Token token) {
		this.tokens.add(token);
	}
	
	private void backtrack() {
		this.cursor--;
		this.positionInLine--;
	}
	
	private void advance() {
		this.cursor++;
		this.positionInLine++;
	}
	
	private void newLine() {
		this.currentLine++;
		this.positionInLine = 0;
	}
	
	private Result tokenizerError(String error, int at) {
		String message = error + " Line: " + this.currentLine + ", pos: " + this.positionInLine;
		return new Result(message, at);
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
	
	private boolean isEmpty(char c) {
		return c == ' ' || c == '\n' || c == '\t';
	}
	
	public Result error(String error) {
		return new Result(error, -1);
	}
}

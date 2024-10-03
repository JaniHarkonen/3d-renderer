package project.gui.jeemu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import project.gui.props.PercentageBuilder;
import project.gui.props.Property;
import project.gui.props.PropertyBuilder;

public class Tokenizer {
	public static final String KEYWORD_BODY = "body";
	public static final String KEYWORD_DIV = "div";
	public static final String KEYWORD_BUTTON = "button";
	public static final String KEYWORD_IMAGE = "image";
	public static final String KEYWORD_TEXT = "text";
	public static final String KEYWORD_COLLECTION = "collection";
	public static final String KEYWORD_AS = "as";
	public static final String KEYWORD_THEME = "theme";
	
	public static final String RQUERY_RESPONSIVE = "responsive";
	public static final String RQUERY_WINDOW = "window";
	public static final String RQUERY_RATIO = "ratio";
	
	private static final Set<String> KEYWORDS;
	private static final Set<String> RQUERY_WORDS;
	static {
		KEYWORDS = new HashSet<>();
		KEYWORDS.add(KEYWORD_BODY);
		KEYWORDS.add(KEYWORD_DIV);
		KEYWORDS.add(KEYWORD_BUTTON);
		KEYWORDS.add(KEYWORD_IMAGE);
		KEYWORDS.add(KEYWORD_TEXT);
		KEYWORDS.add(KEYWORD_COLLECTION);
		KEYWORDS.add(KEYWORD_AS);
		KEYWORDS.add(KEYWORD_THEME);
		
		RQUERY_WORDS = new HashSet<>();
		RQUERY_WORDS.add(RQUERY_RESPONSIVE);
		RQUERY_WORDS.add(RQUERY_WINDOW);
		RQUERY_WORDS.add(RQUERY_RATIO);
	}
	
	private static boolean isKeyword(String literal) {
		return KEYWORDS.contains(literal);
	}
	
	private static boolean isRQuery(String literal) {
		return RQUERY_WORDS.contains(literal);
	}
	
	private static Map<Character, TokenType> specialCharacterToTokenType;
	static {
		specialCharacterToTokenType = new HashMap<>();
		specialCharacterToTokenType.put('(', TokenType.EXPRESSION_START);
		specialCharacterToTokenType.put(')', TokenType.EXPRESSION_END);
		specialCharacterToTokenType.put('{', TokenType.BLOCK_START);
		specialCharacterToTokenType.put('}', TokenType.BLOCK_END);
		specialCharacterToTokenType.put(',', TokenType.EXPRESSION_SEPARATOR);
		specialCharacterToTokenType.put(':', TokenType.FIELD_SEPARATOR);
	}
	
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
		this.tokens = null;
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
			Result error = null;
			error = this.comment();
			
			if( error != null ) {
				return error;
			}
			
			error = null;
			
			char charAt = this.jeemuString.charAt(this.cursor);
			
			if( charAt == '\n' ) {
				this.newLine();
				this.advance();
				continue;
			}
			
			if( this.isLiteral(charAt) ) {
				error = this.literal();
			} else if( this.isDigit(charAt) ) {
				error = this.numeric();
			} else if( this.isString(charAt) ) {
				error = this.string();
			} else if( this.isOperator(charAt) ) {
				this.token(new Token(TokenType.OPERATOR, Operator.getOperator(charAt)));
				error = null;
			} else if( this.isSpecialCharacter(charAt) ) {
				error = this.specialCharacter();
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
	
	private Result literal() {
		int start = this.cursor;
		while( this.isLiteral(this.charAtCursor()) ) {
			this.advance();
		}
		
			// Determine if the identifier stands for a function, or if it's a reference
			// to a custom item
		String literal = this.jeemuString.substring(start, this.cursor);
		TokenType type;
		if( Property.functionExists(literal) ) {
			type = TokenType.FUNCTION;
		} else if( isKeyword(literal) ){
			type = TokenType.KEYWORD;
		} else if( isRQuery(literal) ) {
			type = TokenType.RQUERY;
		} else {
			type = TokenType.IDENTIFIER;
		}
		
		this.token(new Token(type, literal));
		this.backtrack();
		return null;
	}
	
	private Result numeric() {
		boolean isDecimalFound = false;
		float value = 0;
		float firstValue = Float.POSITIVE_INFINITY; // This will be used in case an r-query value is read
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
					
					// Handle responsiveness query types (e.g. '800x600')
				if( 
					type.length() == 0 && 
					(this.isResolution(numberChar) || this.isAspectRatio(numberChar))
				) {
					if( firstValue != Float.POSITIVE_INFINITY ) {
						return this.tokenizerError("Invalid r-query encountered.", this.cursor);
					}
					
					firstValue = value;
					value = 0;
					factor = 10;
					isDecimalFound = false;
				}
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
					
					case Property.RQUERY_DIMENSION_SEPARATOR:
					case Property.RQUERY_ASPECT_RATIO_SEPARATOR:
						this.token(new Token(TokenType.RQUERY, firstValue + type + value));
					
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
	
	private Result string() {
		char charAt = this.charAtCursor();
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
	
	private Result specialCharacter() {
		char charAt = this.charAtCursor();
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
		else if( this.parenthesisCount == 0 && this.cursor < this.jeemuString.length() - 1 ) {
				// Expression shouldn't close before the last character in expression
			return this.tokenizerError(
				"Expression has content outside its parenthesis.", this.cursor
			);
		}
		
		TokenType type = 
			specialCharacterToTokenType.getOrDefault(charAt, TokenType.SPECIAL_CHARACTER);
		this.token(new Token(type, charAt));
		return null;
	}
	
	private Result comment() {
		char nextChar = this.charAt(this.cursor + 1);
		
		if( this.charAtCursor() != '/' ) {
			return null;
		}
		
			// Single line comment, skip to end of the line
		if( nextChar == '/' ) {
			this.cursor = this.jeemuString.indexOf('\n', this.cursor + 1);
			if( this.cursor == -1 ) {
				this.cursor = this.jeemuString.length();
			}
		} else if( nextChar == '*' ) {
				// Multi-line comment, skip to end of the comment
			this.advance();
			
			int asteriskCount = 0;
			char charAt;
			while( (charAt = this.charAtCursor()) != 0 && charAt != '/' ) {
				if( charAt == '*' ) {
					asteriskCount++;
				}
			}
			if( asteriskCount <= 1 ) {
				return this.tokenizerError("Unexpected end of comment encountered.", this.cursor);
			}
		}
		
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
	
	private Token lastToken() {
		return this.tokens.get(this.tokens.size() - 1);
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
			c == '(' || c == ')' || c == ',' || c == '{' || c == '}' || c == ':'
		);
	}
	
	private boolean isOperator(char c) {
		return (
			c == '+' || c == '-' || c == '*' || c == '/'
		);
	}
	
	private boolean isLiteral(char c) {
		return (this.isLetter(c) || c == '_');
	}
	
	private boolean isString(char c) {
		return (c == '"' || c == '\'' || c == '`');
	}
	
	private boolean isEmpty(char c) {
		return (c == ' ' || c == '\n' || c == '\t');
	}
	
	private boolean isResolution(char c) {
		return (c == 'x' || c == 'X');
	}
	
	private boolean isAspectRatio(char c) {
		return (c == ':');
	}
	
	public Result error(String error) {
		return new Result(error, -1);
	}
}

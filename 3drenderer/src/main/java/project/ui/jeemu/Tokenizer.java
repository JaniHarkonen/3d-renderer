package project.ui.jeemu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joml.Vector4f;

import project.ui.props.NumericBuilder;
import project.ui.props.PercentageBuilder;
import project.ui.props.Properties;
import project.ui.props.Property;
import project.ui.props.PropertyBuilder;

public class Tokenizer {
	public static final String KEYWORD_BODY = "body";
	public static final String KEYWORD_DIV = "div";
	public static final String KEYWORD_BUTTON = "button";
	public static final String KEYWORD_IMAGE = "image";
	public static final String KEYWORD_TEXT = "text";
	public static final String KEYWORD_COLLECTION = "collection";
	public static final String KEYWORD_AS = "as";
	public static final String KEYWORD_THEME = "theme";
	
	public static final String RESERVED_ID = "ID";
	
	public static final String RQUERY_RESPONSIVE = "responsive";
	public static final String RQUERY_WINDOW = "window";
	public static final String RQUERY_RATIO = "ratio";
	
	private static final int[] hexToInt = new int[] {
		0, 1, 2, 3, 4, 5, 6, 7, 8, 9, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15
	};
	private static final Set<String> KEYWORDS;
	private static final Set<String> RQUERY_WORDS;
	private static final Map<Character, TokenType> CHARACTER_TO_TOKEN;
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
		
		CHARACTER_TO_TOKEN = new HashMap<>();
		CHARACTER_TO_TOKEN.put('(', TokenType.EXPRESSION_START);
		CHARACTER_TO_TOKEN.put(')', TokenType.EXPRESSION_END);
		CHARACTER_TO_TOKEN.put('{', TokenType.BLOCK_START);
		CHARACTER_TO_TOKEN.put('}', TokenType.BLOCK_END);
		CHARACTER_TO_TOKEN.put(',', TokenType.EXPRESSION_SEPARATOR);
		CHARACTER_TO_TOKEN.put(':', TokenType.FIELD_SEPARATOR);
		CHARACTER_TO_TOKEN.put(';', TokenType.FIELD_END);
	}
	
	private static boolean isKeyword(String literal) {
		return KEYWORDS.contains(literal);
	}
	
	private static boolean isRQuery(String literal) {
		return RQUERY_WORDS.contains(literal);
	}
	
	private static TokenType getSpecialCharacterToken(char c, TokenType defaultReturn) {
		return CHARACTER_TO_TOKEN.getOrDefault(c, defaultReturn);
	}
	
	private static int hexToInt(int hex) {
		if( hex < 0 || hex >= hexToInt.length ) {
			return -1;
		}
		return hexToInt[hex];
	}
	
	public class Result {
		public boolean wasSuccessful;
		public String errorMessage;
		public int cursor;
		public int atLine;
		public int atPosition;
		public List<Token> tokens;
		
		public Result(
			boolean wasSuccessful, 
			String errorMessage, 
			int cursor, 
			int atLine, 
			int atPosition, 
			List<Token> tokens
		) {
			this.wasSuccessful = wasSuccessful;
			this.errorMessage = errorMessage;
			this.cursor = cursor;
			this.atLine = atLine;
			this.atPosition = atPosition;
			this.tokens = tokens;
		}
		
			// Error
		public Result(String errorMessage) {
			this(
				false,
				errorMessage, 
				Tokenizer.this.cursor, 
				Tokenizer.this.currentLine, 
				Tokenizer.this.positionInLine,
				new ArrayList<>(0)
			);
		}
		
			// Success
		public Result(List<Token> tokens) {
			this(true, "", -1, -1, -1, tokens);
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
			
			char charAt = this.charAtCursor();
			char nextChar = this.charAt(this.cursor + 1);
			boolean isWindowsNewLine = (charAt == '\r' && nextChar == '\n');
			
			if( charAt == '\n' || isWindowsNewLine ) {
				this.newLine();
				this.advance();
				
				if( isWindowsNewLine ) {
					this.advance();
				}
				
				continue;
			}
			
			if( this.isLiteral(charAt) ) {
				error = this.literal();
			} else if( this.isDigit(charAt) ) {
				error = this.numeric();
			} else if( this.isString(charAt) ) {
				error = this.string();
			} else if( this.isOperator(charAt) ) {
				this.token(TokenType.OPERATOR, Operator.getOperator(charAt));
				error = null;
			} else if( this.isSpecialCharacter(charAt) ) {
				error = this.specialCharacter();
			} else if( this.isHexColor(charAt) ) {
				error = this.hexColor();
			} else if( this.charAtCursor() == '.' ) {
				return this.tokenizerError(
					"Numeric values starting with decimal point are not allowed."
				);
			} else if( !this.isEmpty(charAt) ) {
				return this.tokenizerError(
					"Unexpected character " + charAt + "(" + (int) charAt + ") encountered."
				);
			}
			
			if( error != null ) {
				return error;
			}
			
			this.advance();
		}
		
			// Make sure all parenthesis were closed
		if( this.parenthesisCount > 0 ) {
			return tokenizerError("Expected " + this.parenthesisCount + " more closing parenthesis.");
		}
		
		return new Result(this.tokens);
	}
	
	private Result literal() {
		int start = this.cursor;
		char charAt;
		while( this.isLiteral(charAt = this.charAtCursor()) || this.isDigit(charAt) ) {
			this.advance();
		}
		
			// Determine if the identifier stands for a function, or if it's a reference
			// to a custom item
		TokenType type;
		String literal = this.jeemuString.substring(start, this.cursor);
		if( Property.functionExists(literal) ) {
			type = TokenType.FUNCTION;
		} else if( isKeyword(literal) ){
			type = TokenType.KEYWORD;
		} else if( isRQuery(literal) ) {
			type = TokenType.RQUERY;
		} else if( Properties.isProperty(literal) ) {
			type = TokenType.PROPERTY;
		} else if( literal.equals(RESERVED_ID) ) {
			type = TokenType.RESERVED;
		} else {
			type = TokenType.IDENTIFIER;
		}
		
		this.token(type, literal);
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
						"Expression contains a value with multiple decimal points."
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
						return this.tokenizerError("Invalid r-query encountered.");
					}
					
					firstValue = value;
					value = 0;
					factor = 10;
					isDecimalFound = false;
				}
			} else if( numberChar == '%' ) {
					// Handle percent
				PercentageBuilder builder = new PercentageBuilder(value);
				this.token(TokenType.EVALUABLE, builder);
				break;
			} else {
					// Validate property type
				switch( type ) {
						// Numeric values
					case Property.PX:
					case Property.C:
					case Property.R: {
						NumericBuilder builder = new NumericBuilder(value, type);
						this.token(TokenType.EVALUABLE, builder);
					} break;
						// Ambiguous numeric value
					case "": {
						PropertyBuilder builder = new PropertyBuilder(value, Property.NUMBER);
						this.token(TokenType.EVALUABLE, builder);
					} break;
						// R-queries
					case Property.RQUERY_ASPECT_RATIO_SEPARATOR:
					case Property.RQUERY_DIMENSION_SEPARATOR:
						this.token(TokenType.RQUERY, new float[] { firstValue, value });
						break;
					
						// Invalid property type
					default:
						return this.tokenizerError("Invalid property type '" + type + "'.");
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
		this.advance();
		
		int start = this.cursor;
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
				"String beginning with character " + charAt + " must also end with that character."
			);
		}
		
		String value = this.jeemuString.substring(start, this.cursor);
		PropertyBuilder builder = new PropertyBuilder(value, Property.STRING);
		this.token(TokenType.EVALUABLE, builder);
		
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
			return this.tokenizerError("Encountered unexpected closing parenthesis.");
		}/* else if( this.parenthesisCount == 0 && this.cursor < this.jeemuString.length() - 1 ) {
				// Expression shouldn't close before the last character in expression
			return this.tokenizerError(
				"Expression has content outside its parenthesis.", this.cursor
			);
		}*/
		
		Token previousToken = this.lastToken();
		TokenType type = getSpecialCharacterToken(charAt, TokenType.SPECIAL_CHARACTER);
		this.token(type, charAt);
		
			// Text elements are unique in that their text content is input between { }
		if( 
			previousToken != null && 
			previousToken.type == TokenType.KEYWORD && 
			((String) previousToken.value).equals(KEYWORD_TEXT) 
		) {
			this.advance();
			this.readText();
			this.backtrack();
		}
		
		return null;
	}
	
	private void readText() {
		char charAt;
		int start = this.cursor;
		while( (charAt = this.charAtCursor()) != 0 && charAt != '}' ) {
			this.advance();
		}
		
		String text = this.jeemuString.substring(start, this.cursor);
		PropertyBuilder builder = new PropertyBuilder(text, Property.STRING);
		this.token(TokenType.EVALUABLE, builder);
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
				return this.tokenizerError("Unexpected end of comment encountered.");
			}
		}
		
		return null;
	}
	
	private Result hexColor() {
		float red;
		float green;
		float blue;
		
			// Read RGB values
		if( 
			(red = this.hexColorValue()) == -1 || 
			(green = this.hexColorValue()) == -1 || 
			(blue = this.hexColorValue()) == -1 
		) {
			char charAt = this.charAtCursor();
			return this.tokenizerError(
				"Unexpected character '" + charAt + "' (" + (int) charAt + ") encountered "
				+ "in hexadecimal color notation."
			);
		}
		
			// Read possible alpha value
		float alpha = this.hexColorValue();
		if( alpha == -1 ) {
			alpha = 255f;
		}
		
		PropertyBuilder builder = 
			new PropertyBuilder(new Vector4f(red, green, blue, alpha), Property.COLOR);
		this.token(TokenType.EVALUABLE, builder);
		
		return null;
	}
	
	private float hexColorValue() {
		char c = this.charAt(this.advance());
		float hex1 = hexToInt(c - '0');
		if( hex1 == -1 ) {
			this.backtrack();
			return -1;
		}
		
		float hex2 = hexToInt(this.charAt(this.advance()) - '0');
		if( hex2 == -1 ) {
			this.backtrack(2);
			return -1;
		}
		
		return hex1 * 16 + hex2;
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
	
	private void token(TokenType type, Object value) {
		this.tokens.add(new Token(type, value, this.currentLine, this.positionInLine));
	}
	
	private Token lastToken() {
		int index = this.tokens.size() - 1;
		if( index < 0 || this.tokens.size() == 0 ) {
			return null;
		}
		return this.tokens.get(index);
	}
	
	private void backtrack(int count) {
		this.cursor -= count;
		this.positionInLine -= count;
	}
	
	private void backtrack() {
		this.backtrack(1);
	}
	
	private int advance() {
		this.cursor++;
		this.positionInLine++;
		return this.cursor;
	}
	
	private void newLine() {
		this.currentLine++;
		this.positionInLine = 0;
	}
	
	private Result tokenizerError(String error) {
		String message = error + " Line: " + this.currentLine + ", pos: " + this.positionInLine;
		return new Result(message);
	}
	
	private boolean isLetter(char c) {
		return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
	}
	
	private boolean isDigit(char c) {
		return (c >= '0' && c <= '9');
	}
	
	private boolean isSpecialCharacter(char c) {
		return (
			c == '(' || c == ')' || c == ',' || c == '{' || c == '}' || c == ':' || c == ';'
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
	
	private boolean isHexColor(char c) {
		return (c == '#');
	}
	
	public Result error(String error) {
		return new Result(error);
	}
}

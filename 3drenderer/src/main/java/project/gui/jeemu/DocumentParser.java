package project.gui.jeemu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.gui.AGUIElement;
import project.gui.GUI;
import project.gui.Theme;
import project.gui.props.Property;
import project.gui.props.PropertyBuilder;

public class DocumentParser {
	public class Result {
		public boolean wasSuccessful;
		public String errorMessage;
		public Token errorToken;
		public int cursor;
		
		public Result(boolean wasSuccessful, String errorMessage, Token errorToken, int cursor) {
			this.wasSuccessful = wasSuccessful;
			this.errorMessage = errorMessage;
			this.errorToken = errorToken;
			this.cursor = cursor;
		}
		
		public Result(String errorMessage, Token errorToken) {
			this(false, errorMessage, errorToken, DocumentParser.this.cursor);
		}
		
		public Result() {
			this(true, "", null, -1);
		}
	}

	private GUI targetUI;
	private List<Token> tokens;
	private int cursor;
	private Map<String, AGUIElement> collections;
	
	public DocumentParser() {
		this.targetUI = null;
		this.tokens = null;
		this.cursor = -1;
		this.collections = new HashMap<>();
	}
	
	
	public Result parse(GUI targetUI, List<Token> tokens) {
		Result error;
		this.targetUI = targetUI;
		this.tokens = tokens;
		this.cursor = -1;
		this.collections = new HashMap<>();
		error = this.document();
		
		if( error != null ) {
			return error;
		}
		
		return new Result();
	}
	
	private Result document() {
		Result error = this.extractCustomStructures();
		return error;
		/*if( error != null ) {
			return error;
		}*/
		
		
		
		//return this.targetUI.setBody(this.body());
	}
	
	private Result extractCustomStructures() {
		Token nextToken = this.next();
		while( (nextToken = this.next()) != null ) {
			if( nextToken.type != TokenType.IDENTIFIER ) {
				return null;
			}
			
			String identifier = (String) nextToken.value;
			this.advance();
			nextToken = this.next();
			if( nextToken != null && nextToken.type == TokenType.KEYWORD ) {
				if( nextToken.value.equals(Tokenizer.KEYWORD_THEME) ) {
					Theme theme = new Theme();
					this.advance(2); // Skip also the first {
					Result error = this.theme(theme);
					
					if( error != null ) {
						return error;
					}
					
					this.targetUI.addTheme(identifier, theme);
					
					return null;
				} /*else if( nextToken.value.equals(Tokenizer.KEYWORD_COLLECTION) ) {
					this.advance();
					
					
					return null;
				}*/
			}
			
			this.advance();
		}
		
		return this.parserError("Unexpectedly out of tokens.");
	}
	
	private Result theme(Theme parent) {
		while( true ) {
			Token nextToken = this.next();
			Token specifierToken;
			
				// Unexpected ending
			if( nextToken == null ) {
				return this.parserError("Unexpected end to theme definition.");
			}
			
				// Identifier expected
			if( 
				!this.checkToken(nextToken, TokenType.IDENTIFIER) || 
				(specifierToken = this.tokenAt(this.cursor + 2)) == null
			) {
				return this.parserError("Theme property or section identifier expected. ");
			}
			
			String identifier = (String) nextToken.value;
			this.advance(2);
			
				// Section
			if( specifierToken.type == TokenType.BLOCK_START ) {
				Theme section = new Theme();
				Result error = this.theme(section);
				if( error != null ) {
					return error;
				}
				parent.setSection(identifier, section);
			} else if( specifierToken.type == TokenType.FIELD_SEPARATOR ) {
				
					// Theme property
				nextToken = this.next();
				
				if( nextToken == null ) {
					return this.parserError("Theme property value expected.");
				}
				
				if( this.checkToken(nextToken, TokenType.EVALUABLE) ) {
					parent.setProperty(identifier, (PropertyBuilder) nextToken.value);
					this.advance();
					
					if( !this.checkToken(this.next(), TokenType.FIELD_END) ) {
						return this.parserError("Semicolon ; expected after theme property.");
					}
				} else if( 
					this.checkToken(nextToken, TokenType.FUNCTION) && 
					(nextToken.value.equals(Property.FUNCTION_EXPR_ABBR) ||
					nextToken.value.equals(Property.FUNCTION_EXPR))
				) {
						// Expression
					PropertyBuilder builder = new PropertyBuilder();
					Result error = this.expression(builder);
					if( error != null ) {
						return error;
					}
					parent.setProperty(identifier, builder);
				} else {
					return this.parserError("Expected a value after property name.");
				}
			} else {
				return this.parserError("Can't distinguish between a section and a property in theme.");
			}
			
			this.advance();
			
				// End of theme
			nextToken = this.next();
			if( this.checkToken(nextToken, TokenType.BLOCK_END) ) {
				return null;
			}
		}
	}
	
	private Result expression(PropertyBuilder builder) {
		Token nextToken;
		StringBuilder stringBuilder = new StringBuilder();
		
		while( (nextToken = this.next()) != null ) {
			if( this.checkToken(nextToken, TokenType.FIELD_END) ) {
				builder.value = stringBuilder.toString();
				builder.dataType = Property.EXPRESSION;
				return null;
			}
			
			stringBuilder.append(nextToken.value);
			this.advance();
		}
		
		return this.parserError("Ran out of tokens while reading an expression.");
	}
	
	/*private AGUIElement collection() {
		
	}
	
	private void addCollection(String name, AGUIElement collection) {
		this.collections.put(name, collection);
	}
	
	private Result body() {
		
	}
	
	private Div div() {
		
	}
	
	private Image image() {
		
	}
	
	private Text text() {
		
	}*/
	
	private int advance(int index) {
		this.cursor += index;
		return this.cursor;
	}
	
	private int advance() {
		return this.advance(1);
	}
	
	private Token tokenAt(int index) {
		if( index < 0 || index >= this.tokens.size() ) {
			return null;
		}
		return this.tokens.get(index);
	}
	
	private Token next() {
		return this.tokenAt(this.cursor + 1);
	}
	
	private boolean checkToken(Token token, TokenType type, Object value) {
		if( token == null || token.type != type ) {
			return false;
		}
		return token.value.equals(value);
	}
	
	private boolean checkToken(Token token, TokenType type) {
		return (token != null && token.type == type);
	}
	
	private boolean orCheckToken(Token token, TokenType type, Object... values) {
		if( token == null || token.type == type ) {
			return false;
		}
		
		for( Object value : values ) {
			if( token.value.equals(value) ) {
				return true;
			}
		}
		
		return false;
	}
	
	private Result parserError(String errorMessage) {
		Token errorToken = this.tokenAt(this.cursor);
		if( errorToken == null ) {
			errorToken = Token.NULL_TOKEN;
		}
		return new Result(
			errorMessage + " Line: " + errorToken.line + ", pos: " + errorToken.position, errorToken
		);
	}
}

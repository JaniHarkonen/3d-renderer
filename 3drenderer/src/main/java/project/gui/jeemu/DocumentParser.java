package project.gui.jeemu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.gui.AGUIElement;
import project.gui.Body;
import project.gui.Div;
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
	
	private class BuilderHolder {
		private PropertyBuilder builder;
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
		this.targetUI = targetUI;
		this.tokens = tokens;
		this.cursor = -1;
		this.collections = new HashMap<>();
		
		Result error = this.document();
		if( error != null ) {
			return error;
		}
		
		return new Result();
	}
	
	private Result document() {
		Result error = this.extractCustomStructures();
		if( error != null ) {
			return error;
		}
		
		return this.body();
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
				
				BuilderHolder holder = new BuilderHolder();
				Result error = this.propertyValue(holder);
				
				if( error != null ) {
					return error;
				}
				
				parent.setProperty(identifier, holder.builder);
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
	
	private Result propertyValue(BuilderHolder holder) {
		Token next = this.next();
		if( this.checkToken(next, TokenType.EVALUABLE) ) {
			this.advance();
			if( !this.checkToken(this.next(), TokenType.FIELD_END) ) {
				return this.parserError("Semicolon ; expected after property value.");
			}
			
			holder.builder = (PropertyBuilder) next.value;
		} else if( 
			this.checkToken(next, TokenType.FUNCTION) && 
			(next.value.equals(Property.FUNCTION_EXPR_ABBR) ||
			next.value.equals(Property.FUNCTION_EXPR))
		) {
				// Expression
			Result error = this.expression(holder);
			if( error != null ) {
				return error;
			}
		} else if( this.checkToken(next, TokenType.FUNCTION, Property.FUNCTION_THEME_ABBR) ) {
			holder.builder = new PropertyBuilder(next.value, Property.THEME);
		} else {
			return this.parserError("Property value expected.");
		}
		
		return null;
	}
	
	private Result expression(BuilderHolder holder) {
		Token nextToken;
		StringBuilder stringBuilder = new StringBuilder();
		
		while( (nextToken = this.next()) != null ) {
			if( this.checkToken(nextToken, TokenType.FIELD_END) ) {
				holder.builder = new PropertyBuilder(stringBuilder.toString(), Property.EXPRESSION);
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
	}*/
	
	private Result body() {
		Token nextToken = this.next();
		if( !this.checkToken(nextToken, TokenType.KEYWORD, Tokenizer.KEYWORD_BODY) ) {
			return this.parserError("UI body expected.");
		}
		
		this.advance();
		nextToken = this.next();
		if( !this.checkToken(nextToken, TokenType.BLOCK_START) ) {
			return this.parserError("Body block expected.");
		}
		
		Body body = new Body(this.targetUI);
		this.advance();
		
		Result error = this.children(body);
		if( error != null ) {
			return error;
		}
		
		return null;
	}
	
	private Result children(AGUIElement parent) {
		Token nextToken = this.next();
		
		if( this.checkToken(nextToken, TokenType.BLOCK_END) ) {
			return null;
		}
		
		while( (nextToken = this.next()) != null ) {
			if( nextToken.type == TokenType.PROPERTY ) {
					// Handle property
				String propertyName = nextToken.value.toString();
				this.advance();
				if( !this.checkToken(this.next(), TokenType.FIELD_SEPARATOR) ) {
					return this.parserError("Element body expected.");
				}
				
				this.advance();
				BuilderHolder holder = new BuilderHolder();
				Result error = this.propertyValue(holder);
				
				if( error != null ) {
					return error;
				}
				
				parent.getProperties().setProperty(propertyName, holder.builder.build(propertyName));
			} else if( nextToken.type == TokenType.KEYWORD ) {
					// Handle children
				this.advance();
				
				if( !this.checkToken(this.next(), TokenType.BLOCK_START) ) {
					return this.parserError("Element body expected.");
				}
				
				AGUIElement child;
				this.advance();
				
					// Determine the ID of the child, so that it can be instantiated as IDs are 
					// final (ID should be the first property)
				String childID = this.readID();
				if( childID == null ) {
					return this.parserError(
						"Element ID must be the first property of the element"
						+ "and it must be unique."
					);
				}
				
				this.advance();
				switch( (String) nextToken.value ) {
					case Tokenizer.KEYWORD_DIV: child = new Div(this.targetUI, childID); break;
					//case Tokenizer.KEYWORD_IMAGE: new Image(this.targetUI, childID); break;
					//case Tokenizer.KEYWORD_TEXT: new Text(this.targetUI, childID, ""); break; // Special case
					default: return this.parserError("Element declaration expected.");
				}
				
				Result error = this.children(child);
				if( error != null ) {
					return error;
				}
				
				boolean wasAdded = this.targetUI.addChildTo(parent, child);
				if( !wasAdded ) {
					return this.parserError("An element with ID '" + childID + "' already exists.");
				}
			}
			
			this.advance();
			if( this.checkToken(this.next(), TokenType.BLOCK_END) ) {
				return null;
			}
		}
		
		return this.parserError("Ran out of tokens while parsing an element.");
	}
	
	private String readID() {
		if( !this.checkToken(this.next(), TokenType.RESERVED, Tokenizer.RESERVED_ID) ) {
			return null;
		}
		
		this.advance();
		if( !this.checkToken(this.next(), TokenType.FIELD_SEPARATOR) ) {
			return null;
		}
		
		this.advance();
		Token idToken = this.next();
		
		if( !this.checkToken(idToken, TokenType.EVALUABLE) ) {
			return null;
		}
		
		PropertyBuilder idBuilder = (PropertyBuilder) idToken.value;
		if( !idBuilder.dataType.equals(Property.STRING) ) {
			return null;
		}
		
		this.advance();
		if( !this.checkToken(this.next(), TokenType.FIELD_END) ) {
			return null;
		}
		
		return idBuilder.toString();
	}
	
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

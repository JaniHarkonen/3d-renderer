package project.gui.jeemu;

import java.util.HashSet;
import java.util.Set;

import project.gui.Body;
import project.gui.GUI;
import project.shared.logger.Logger;

public class Parser {
	public static final String FAILED_TO_PARSE = "Failed to parse Jeemu!";
	
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
	
	public static final Set<String> KEYWORDS;
	public static final Set<String> RQUERIES;
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
		
		RQUERIES = new HashSet<>();
		RQUERIES.add(RQUERY_RESPONSIVE);
		RQUERIES.add(RQUERY_WINDOW);
		RQUERIES.add(RQUERY_RATIO);
	}
	
	private static void logError(Object me, Object... objects) {
		Logger.get().error(me, FAILED_TO_PARSE, objects);
	}
	
	private GUI targetGUI;
	private String jeemuString;
	private int cursor;
	
	public Parser() {
		this.targetGUI = null;
		this.jeemuString = null;
		this.cursor = 0;
	}

	
	public boolean parse(GUI targetGUI, String jeemuString) {
		this.targetGUI = targetGUI;
		this.jeemuString = jeemuString;
		this.cursor = 0;
		
		Body body = this.document();
		if( body != null ) {
			targetGUI.setBody(body);
			return true;
		}
		
		return false;
	}
	
	private Body document() {
		this.skipEmpty();	// Skip space, newline, tab
		
		Body body = null;
		char charAt;
		int stash = this.cursor;
		while( (charAt = this.charAtCursor()) != 0 ) {
				// After body has been parsed, nothing should remain except for empty
				// characters
			if( body != null ) {
				logError(
					this, 
					"Body must be the last element in a Jeemu document.", 
					"Extraneous code found."
				);
				return null;
			}
			
			String literal = this.literal();
			
			if( literal == null ) {
				logError(
					this, 
					"Expected: Theme, collection or body declaration.", 
					"Encountered: " + charAt
				);
				return null;
			}
			
				// Read literal must either be a theme, collection or a body declaration
			this.cursor++;
			
			if( literal.equals(KEYWORD_BODY) ) {
					// Must be the body
				body = this.body();
			} else if( !RQUERIES.contains(literal) ) {
					// Must be a theme or a collection
				String nextLiteral = this.literal();
				
				if( nextLiteral == null ) {
					logError(
						this,
						"Expected: Theme or collection declaration expected after identifier."
					);
					return null;
				}
				
				switch( nextLiteral ) {
					case KEYWORD_THEME: this.theme(); break;
					case KEYWORD_COLLECTION: this.collection(); break;
				}
			} else {
				logError(
					this,
					"Expected: Theme, collection or body declaration.", 
					"Encountered: " + literal
				);
				return null;
			}
			
			this.skipEmpty();
			this.cursor++;
		}
		
		return body;
	}
	
	private Body body() {
		
	}
	
	private String literal() {
		char charAt;
		int stash = this.cursor;
		while( (charAt = this.charAtCursor()) != 0 && this.isLiteralCharacter(charAt) ) {
			this.cursor++;
		}
		
		if( stash == this.cursor ) {
			return null;
		}
		
		return this.jeemuString.substring(stash, this.cursor);
	}
	
	private void theme() {
		
	}
	
	private void collection() {
		
	}
	
	private String text() {
		char charAt;
		int stash = this.cursor; // Stashed
		while( (charAt = this.charAtCursor()) != 0 ) {
			if( charAt == '}' ) {
				return this.jeemuString.substring(stash, this.cursor);
			}
			this.cursor++;
		}
		
		logError(
			this, "Text elements must be closed with a curly brace }."
		);
		
		this.cursor = stash;
		return null;
	}
	
	private char charAtCursor() {
		if( this.cursor < this.jeemuString.length() ) {
			return this.jeemuString.charAt(this.cursor);
		}
		
		return 0;
	}
	
	private void skipEmpty() {
		char charAt;
		while( (charAt = this.charAtCursor()) != 0 && this.isEmpty(charAt) ) {
			this.cursor++;
		}
	}
	
	private boolean isLiteralCharacter(char c) {
		return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || c == '_';
	}
	
	private boolean isEmpty(char c) {
		return (c == ' ' || c == '\n' || c == '\t');
	}
}

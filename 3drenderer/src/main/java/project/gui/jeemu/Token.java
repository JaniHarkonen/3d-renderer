package project.gui.jeemu;

public class Token {
	
	/**
	 * Type of value stored in the token.
	 */
	public final TokenType type;
	
	/**
	 * Token's value.
	 */
	public final Object value;
	
	public Token(TokenType type, Object value) {
		this.type = type;
		this.value = value;
	}
}

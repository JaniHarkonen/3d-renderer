package project.gui.jeemu;

/**
 * Tokens are the smallest grammatical elements of the Jeemu language that are produced
 * by the Jeemu Tokenizer. Tokens hold a varying set of values making it necessary for 
 * the type of the token to be assigned upon creation. Token types can be found in the 
 * TokenType-enum. Each token holds the type of the stored value along with the value 
 * itself. The type is used by other classes to determine, how the underlying value 
 * should be handled.
 * 
 * The token also holds the line as well as the position in the line where the token 
 * ends.
 * 
 * @author Jani Härkönen
 *
 */
public class Token {
	
	/**
	 * Type of value stored in the token.
	 */
	public final TokenType type;
	
	/**
	 * Token's value.
	 */
	public final Object value;
	
	/**
	 * Number of the line where the token ends.
	 */
	public final int line;
	
	/**
	 * Token's ending position in the line.
	 */
	public final int position;
	
	/**
	 * Creates an instance of a Token with a given type and value.
	 * 
	 * @param type Type of the value stored in the token.
	 * @param value Stored value.
	 * @param line Line number where the end of the token is found.
	 * @param position Position in line where the token ends.
	 */
	public Token(TokenType type, Object value, int line, int position) {
		this.type = type;
		this.value = value;
		this.line = line;
		this.position = position;
	}
}

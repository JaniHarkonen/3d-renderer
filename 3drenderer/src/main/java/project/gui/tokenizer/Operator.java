package project.gui.tokenizer;

public class Operator {
	public static final int ID_NONE = 0;
	public static final int ID_MUL = 1;
	public static final int ID_DIV = 2;
	public static final int ID_ADD = 3;
	public static final int ID_SUB = 4;
	public static final int FINAL_ID = ID_SUB;
	
	public static final Operator OP_NONE = new Operator(ID_NONE);
	public static final Operator OP_MUL = new Operator(ID_MUL, '*', 1);
	public static final Operator OP_DIV = new Operator(ID_DIV, '/', 1);
	public static final Operator OP_ADD = new Operator(ID_ADD, '+', 2);
	public static final Operator OP_SUB = new Operator(ID_SUB, '-', 2);
	
	private static final Operator[] charToOperator = new Operator[] {
		OP_MUL, OP_ADD, OP_NONE, OP_SUB, OP_NONE, OP_DIV
	};
	
	public static Operator getOperator(char c) {
		c -= '*';
		
		if( c < 0 || c > charToOperator.length ) {
			return OP_NONE;
		}
		
		return charToOperator[c];
	}

	
	public final int id;
	public final char character;
	public final int precedence;
	
	public Operator(int id, char character, int precedence) {
		this.id = id;
		this.character = character;
		this.precedence = precedence;
	}
	
	public Operator(int id) {
		this(id, (char) 0, Integer.MAX_VALUE);
	}
}

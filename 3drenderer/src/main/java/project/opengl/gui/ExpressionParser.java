package project.opengl.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.gui.tokenizer.Token;
import project.gui.tokenizer.TokenType;
import project.shared.logger.Logger;

public class ExpressionParser {
	private static final Map<Character, Integer> precedenceTable;
	static {
		precedenceTable = new HashMap<>();
		precedenceTable.put('*', 1);
		precedenceTable.put('/', 1);
		precedenceTable.put('%', 1);
		precedenceTable.put('+', 2);
		precedenceTable.put('-', 2);
		precedenceTable.put(null, Integer.MAX_VALUE);
	}
	
	
	private enum OpCode {
		MULTIPLY,
		DIVIDE,
		MODULO,
		ADD,
		SUBTRACT,
		CALL,
		AST_NODE
	}
	
	private class ASTNode {
		private ASTNode parent;
		private OpCode opCode;
		private List<Object> arguments;
		
		private ASTNode(OpCode opCode) {
			this.opCode = opCode;
			this.arguments = new ArrayList<>();
		}
		
		private ASTNode() {
			this(null);
		}
		
		/*protected void resolve(Context context) {
			Object currentValue = this.arguments.get(0);
			for( int i = 1; i < this.arguments.size(); i++ ) {
				currentValue = this.calculation(currentValue, this.arguments.get(i));
			}
		}
		
		protected abstract Object calculation(Object currentValue, Object nextValue);*/
	}
	
	/*private class StringAdd extends ASTNode {

		protected StringAdd(OpCode opCode) {
			super(opCode);
		}

		@Override
		protected Object calculation(Object currentValue, Object nextValue) {
			return currentValue.toString() + nextValue.toString();
		}
	}*/
	
	private class Sub extends ASTNode {

		protected Sub() {
			super(OpCode.SUBTRACT);
			// TODO Auto-generated constructor stub
		}
		
	}
	
	private class Frame {
		private int position = 0;
		private ASTNode ast = null;
		private ASTNode currentNode = null;
		private Character previousOperator = null;
	}
	
	private List<Token> tokens;
	private Frame frame;
	
	public ExpressionParser() {
		this.tokens = new ArrayList<>();
		this.frame = new Frame();
	}
	
	
	public void parse(List<Token> tokens) {
		if( tokens.size() <= 1 ) {
			Logger.get().error(this, "Failed to parse expression tokens!", "No parsable tokens.");
			return;
		}
		
		ASTNode node;
		Character previousOperator = null;
		Token firstToken = tokens.get(0);
		int cursor = 0;
		
			// Handle special case where the first value is negated
		if( firstToken.value.equals('-') ) {
			node = new ASTNode(OpCode.SUBTRACT);
			node.arguments.add(null);
			
			previousOperator = '-';
			cursor++;
		} else {
			node = new ASTNode();
		}
		
		while( cursor < tokens.size() ) {
			Token token = tokens.get(cursor);
			
			if( token.type == TokenType.EVALUABLE ) {
				node.arguments.add(token);
			}
			
			
			
			cursor++;
		}
	}
	
	private void parse(List<Token> tokens, int startPosition) {
		
	}
	
	private boolean hasTokens() {
		return (this.frame.position < this.tokens.size());
	}
	
	/*private ASTNode expression() {
		if( this.hasTokens() ) {
			return null;
		}
		
		Token token = this.tokens.get(this.frame.position);
		ASTNode result = null;
		
		if( token.type == TokenType.SPECIAL_CHARACTER ) {
			if( token.value.equals('(') ) {
				ASTNode current = this.currentNode;
				result = this.expression();
				this.currentNode = current;
			} else if( token.value.equals('-') ) {
				result = new Sub();
				result.arguments.add(null);
				
				result.arguments.add(e);
				this.currentNode = result;
				this.position++;
			}
		} else if(
			(result = this.evaluable()) != null ||
			(result = this.percentage()) != null ||
			(result = this.function()) != null
		) {
			this.currentNode = result;
			return result;
		}
		
		return result;
	}
	
	private ASTNode evaluable() {
		Token token = this.tokens.get(this.position);
		ASTNode result = null;
		
		if( token.type != TokenType.EVALUABLE ) {
			return null;
		}
		
		this.position++;
		
		if( !this.hasTokens() ) {
			this.position--;
			return null;
		}
		
		result = this.operator();
		
		
		return result;
	}
	
	private ASTNode percentage() {
		return null;
	}
	
	private ASTNode function() {
		return null;
	}
	
	private ASTNode operator() {
		
	}*/
}

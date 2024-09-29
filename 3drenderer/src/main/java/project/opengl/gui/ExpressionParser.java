package project.opengl.gui;

import java.util.List;

import project.gui.tokenizer.Operator;
import project.gui.tokenizer.Token;
import project.gui.tokenizer.TokenType;

public class ExpressionParser {
	public static final int ID_NEGATE = Operator.FINAL_ID + 1;
	public static final int ID_FUNCTION_CALL = Operator.FINAL_ID + 1;
	
	public static final Operator OP_NEGATE = new Operator(ID_NEGATE);
	public static final Operator OP_FUNCTION_CALL = new Operator(ID_FUNCTION_CALL);
	
	public static final String FAILED_TO_PARSE = "Failed to parse expression!";

	private List<Token> tokens;
	private int cursor;
	
	public ExpressionParser() {
		this.tokens = null;
		this.cursor = 0;
	}
	
	
	public Evaluator parse(List<Token> tokens) {
		this.tokens = tokens;
		return this.expression();
	}
	
	private Evaluator expression() {
		Evaluator root = null;
		Evaluator current = root;
		Token token;
		Operator previousOperator = Operator.OP_NONE;
		
		while( (token = this.lookupToken(this.cursor)) != null ) {
			if( token.type != TokenType.EVALUABLE ) {
				break;
			}
			
			Token nextToken = this.lookupToken(this.cursor + 1);
			
			if( 
				nextToken == null || nextToken.type != TokenType.OPERATOR
			) {
				current.addArgument(token.value);
				break;
			}
			
			Operator nextOperator = (Operator) nextToken.value;
			if( previousOperator.precedence <= nextOperator.precedence ) {
				current.addArgument(token.value);
				
				Evaluator node = current;
				Evaluator lastValid = current;
				while( node != null && node.operator.precedence <= nextOperator.precedence ) {
					lastValid = node;
					node = node.parent;
				}
				
				Evaluator next = new Evaluator();
				next = new Evaluator();
				next.operator = nextOperator;
				
				if( lastValid.parent == null ) {
					root = next;
				} else {
					lastValid.parent.setArgument(1, next);
				}
				
				next.addArgument(lastValid);
				current = next;
			} else {
				Evaluator next = new Evaluator();
				
					// Handle first node
				if( current == null ) {
					root = next;
				} else {
					current.addArgument(next);
				}
				
				next.operator = nextOperator;
				next.addArgument(token.value);
				current = next;
			}
			
			previousOperator = nextOperator;
			this.cursor += 2;
		}
		
		return root;
	}
	
	private void function() {
		
	}
	
	private Token lookupToken(int position) {
		if( position >= this.tokens.size() ) {
			return null;
		}
		
		return this.tokens.get(position);
	}
}

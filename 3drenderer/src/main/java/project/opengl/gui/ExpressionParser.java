package project.opengl.gui;

import java.util.List;

import project.gui.props.Property;
import project.gui.tokenizer.Operator;
import project.gui.tokenizer.Token;
import project.gui.tokenizer.TokenType;

public class ExpressionParser {
	public static final int ID_NEGATE = Operator.FINAL_ID + 1;
	public static final int ID_FUNCTION_CALL = ID_NEGATE + 1;
	//public static final int ID_VALUE = ID_FUNCTION_CALL + 1;
	
	public static final Operator OP_NEGATE = new Operator(ID_NEGATE);
	public static final Operator OP_FUNCTION_CALL = new Operator(ID_FUNCTION_CALL);
	//public static final Operator OP_VALUE = new Operator(ID_VALUE);
	
	public static final String FAILED_TO_PARSE = "Failed to parse expression!";

	private List<Token> tokens;
	private int cursor;
	
	public ExpressionParser() {
		this.tokens = null;
		this.cursor = 0;
	}
	
	
	public IEvaluator parse(List<Token> tokens) {
		this.tokens = tokens;
		return this.expression();
	}
	
	private IEvaluator expression() {
		Evaluator root = null;
		Evaluator current = root;
		Token token;
		Operator previousOperator = Operator.OP_NONE;
		
		while( (token = this.lookupToken(this.cursor)) != null ) {
			ValueProvider valueProvider = new ValueProvider();
			IEvaluator provider = valueProvider;	// Use this to avoid casting
			
				// Handle parenthesis (sub-expressions)
			if( this.checkToken(token, TokenType.SPECIAL_CHARACTER, '(') ) {
				this.cursor++;
				provider = this.expression();
				this.cursor++;
			} else {
					// Constant values will be provided by a value provider
				valueProvider.value = (Property) token.value;
			}
			
			Token nextToken = this.lookupToken(this.cursor + 1);
			if( !this.checkToken(nextToken, TokenType.OPERATOR) ) {
					// Simply return a value provider, if the expression being evaluated
					// contains no operations
				if( previousOperator == Operator.OP_NONE ) {
					return provider;
				}
					// Otherwise, add provider to root and return it
				current.addArgument(provider);
				break;
			}
			
			Operator nextOperator = (Operator) nextToken.value;
			if( previousOperator.precedence <= nextOperator.precedence ) {
					// Higher or equal precedence compared to previous operator:
					// Backtrack AST until lower precedence node (evaluator) or root is
					// found, and make this node the root or the second argument of the
					// lower precedence node
				current.addArgument(provider);
				
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
					// Lower precedence compared to previous operator:
					// Add this node as the second argument to the previous node
				Evaluator next = new Evaluator();
				
					// Handle first node
				if( current == null ) {
					root = next;
				} else {
					current.addArgument(next);
				}
				
				next.operator = nextOperator;
				next.addArgument(provider);
				current = next;
			}
			
			previousOperator = nextOperator;
			this.cursor += 2;
		}
		
		return root;
	}
	
	private Operator ifUnaryOperator(Token token) {
		if( token.type != TokenType.OPERATOR || !token.value.equals(Operator.OP_SUB) ) {
			return null;
		}
		
		return (Operator) token.value;
	}
	
	private void function() {
		
	}
	
	private boolean checkToken(Token token, TokenType type) {
		return (token != null && token.type == type);
	}
	
	private boolean checkToken(Token token, TokenType type, Object value) {
		return (this.checkToken(token, type) && token.value.equals(value));
	}
	
	private Token lookupToken(int position) {
		if( position >= this.tokens.size() ) {
			return null;
		}
		
		return this.tokens.get(position);
	}
}

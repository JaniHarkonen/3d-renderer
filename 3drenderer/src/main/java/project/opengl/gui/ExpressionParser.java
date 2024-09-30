package project.opengl.gui;

import java.util.List;

import project.gui.props.Property;
import project.gui.props.parser.Operator;
import project.gui.props.parser.Token;
import project.gui.props.parser.TokenType;
import project.shared.logger.Logger;

public class ExpressionParser {
	public static final int ID_FUNCTION_CALL = Operator.FINAL_ID + 1;
	public static final Operator OP_FUNCTION_CALL = new Operator(ID_FUNCTION_CALL);
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
		Token currentToken;
		Operator previousOperator = Operator.OP_NONE;
		
		while( (currentToken = this.lookupToken(this.cursor)) != null ) {
			IEvaluator evaluator;
			Evaluator unary = null;	// This will contain an evaluator for a unary operation, if one is detected
			
				// Handle negation
			if( this.checkToken(currentToken, TokenType.OPERATOR, Operator.OP_SUB) ) {
				unary = new Evaluator();
				unary.operator = Operator.OP_NEGATE;
				this.cursor++;
				currentToken = this.lookupToken(this.cursor);
			}
			
			if( this.checkToken(currentToken, TokenType.EVALUABLE) ) {
					// Constant values will be provided by a value provider
				evaluator = new ValueProvider((Property) currentToken.value);
			} else if( this.checkToken(currentToken, TokenType.SPECIAL_CHARACTER, '(') ) {
					// Handle parenthesis (sub-expressions)
				this.cursor++;
				evaluator = this.expression();
				this.cursor++;
			}  else {
					// Handle function call
				IEvaluator function = this.functionCall();
				if( function != null ) {
					evaluator = function;
				} else {
					return null;	// This is not an expression
				}
			}
			
				// Current evaluator is added as an argument to the unary evaluator, and
				// the unary evaluator becomes the current evaluator
			if( unary != null ) {
				unary.addArgument(evaluator);
				previousOperator = unary.operator;
				evaluator = unary;
			}
			
			Token nextToken = this.lookupToken(this.cursor + 1);
			if( !this.checkToken(nextToken, TokenType.OPERATOR) ) {
					// Simply return a value provider, if the expression being evaluated
					// contains no operations, OR the expression is a unary operation
				if( previousOperator == Operator.OP_NONE || unary != null ) {
					return evaluator;
				}
				
				current.addArgument(evaluator);
				break;
			}
			
			Operator nextOperator = (Operator) nextToken.value;
			if( previousOperator.precedence <= nextOperator.precedence ) {
					// Higher or equal precedence compared to previous operator:
					// Backtrack AST until lower precedence node (evaluator) or root is
					// found, and make this node the root or the second argument of the
					// lower precedence node
				current.addArgument(evaluator);
				
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
				next.addArgument(evaluator);
				current = next;
			}
			
			previousOperator = nextOperator;
			this.cursor += 2;
		}
		
		return root;
	}
	
	private IEvaluator functionCall() {
			// Determine start of a function call
		Token functionName = this.lookupToken(this.cursor);
		if( !this.checkToken(functionName, TokenType.FUNCTION) ) {
			return null;
		}
		
		this.cursor++;
		Token argumentsStart = this.lookupToken(this.cursor);
		
		if( !this.checkToken(argumentsStart, TokenType.SPECIAL_CHARACTER, '(') ) {
			Logger.get().error(this, FAILED_TO_PARSE, "Arguments expected after function call.");
			return null;
		}
		
		Evaluator functionCall = null;
		if( functionName.value.equals(Property.FUNCTION_MIN) ) {
			functionCall = new FunctionMin();
		}
		
		functionCall.operator = OP_FUNCTION_CALL;
		functionCall.addArgument(
			new ValueProvider(new Property(null, (String) functionName.value, Property.STRING))
		);
		
		this.cursor++;
		
			// Extract arguments, if any
		Token currentToken;
		while( (currentToken = this.lookupToken(this.cursor)) != null ) {
			if( this.checkToken(currentToken, TokenType.SPECIAL_CHARACTER, ',') ) {
				this.cursor++;
				continue;
			}
			
			if( this.checkToken(currentToken, TokenType.SPECIAL_CHARACTER, ')') ) {
				break;
			}
			
			functionCall.addArgument(this.expression());
			this.cursor++;
		}
		
		return functionCall;
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

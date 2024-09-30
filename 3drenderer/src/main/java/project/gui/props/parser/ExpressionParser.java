package project.gui.props.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.gui.props.Property;
import project.gui.props.parser.functions.FunctionClamp;
import project.gui.props.parser.functions.FunctionMax;
import project.gui.props.parser.functions.FunctionMin;
import project.gui.props.parser.functions.FunctionRGB;
import project.gui.props.parser.functions.FunctionRGBA;
import project.gui.props.parser.ops.AddEvaluator;
import project.gui.props.parser.ops.DivEvaluator;
import project.gui.props.parser.ops.MulEvaluator;
import project.gui.props.parser.ops.NegationEvaluator;
import project.gui.props.parser.ops.SubEvaluator;
import project.shared.logger.Logger;

public class ExpressionParser {
	public static final int ID_FUNCTION_CALL = Operator.FINAL_ID + 1;
	public static final Operator OP_FUNCTION_CALL = new Operator(ID_FUNCTION_CALL);
	
	public static final String FAILED_TO_PARSE = "Failed to parse expression!";
	public static final String FAILED_TO_EVALUATE = "Failed to evaluate expression!";
	
	private static final Map<String, AEvaluator> functionEvaluatorByName;
	static {
		functionEvaluatorByName = new HashMap<>();
		functionEvaluatorByName.put(Property.FUNCTION_MIN, new FunctionMin());
		functionEvaluatorByName.put(Property.FUNCTION_MAX, new FunctionMax());
		functionEvaluatorByName.put(Property.FUNCTION_CLAMP, new FunctionClamp());
		functionEvaluatorByName.put(Property.FUNCTION_RGB, new FunctionRGB());
		functionEvaluatorByName.put(Property.FUNCTION_RGBA, new FunctionRGBA());
	}
	
	private static final AEvaluator[] operatorIdToEvaluator = new AEvaluator[] {
		null, 
		new MulEvaluator(), 
		new DivEvaluator(), 
		new AddEvaluator(), 
		new SubEvaluator(), 
		new NegationEvaluator()
	};
	
	private static AEvaluator getOperatorEvaluator(Operator operator) {
		return operatorIdToEvaluator[operator.id].createInstance();
	}

	private List<Token> tokens;
	private int cursor;
	
	public ExpressionParser() {
		this.tokens = null;
		this.cursor = 0;
	}
	
	
	public AEvaluator parse(List<Token> tokens) {
		this.tokens = tokens;
		return this.expression();
	}
	
	private AEvaluator expression() {
		AEvaluator root = null;
		AEvaluator current = root;
		Token currentToken;
		Operator previousOperator = Operator.OP_NONE;
		
		while( (currentToken = this.lookupToken(this.cursor)) != null ) {
			AEvaluator evaluator;
			AEvaluator unary = null;	// This will contain an evaluator for a unary operation, if one is detected
			
				// Handle negation, unary operator detected
			if( this.checkToken(currentToken, TokenType.OPERATOR, Operator.OP_SUB) ) {
				unary = getOperatorEvaluator(Operator.OP_NEGATE);
				this.cursor++;
				currentToken = this.lookupToken(this.cursor);
			}
			
				// Constant values will be provided by a value provider
			if( this.checkToken(currentToken, TokenType.EVALUABLE) ) {
				evaluator = new ValueProvider((Property) currentToken.value);
			} else if( this.checkToken(currentToken, TokenType.SPECIAL_CHARACTER, '(') ) {
					// Handle parenthesis (sub-expressions)
				this.cursor++;
				evaluator = this.expression();
				this.cursor++;
			}  else {
					// Handle function call
				AEvaluator function = this.functionCall();
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
					// 'evaluator' holds either a value provider or an unary evaluator at
					// this point
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
					// found, and make this node the root OR the second argument of the
					// lower precedence node
				current.addArgument(evaluator);
				
				AEvaluator node = current;
				AEvaluator lastValid = current;
				while( node != null && node.operator.precedence <= nextOperator.precedence ) {
					lastValid = node;
					node = node.parent;
				}
				
				AEvaluator next = getOperatorEvaluator(nextOperator);
				
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
				AEvaluator next = getOperatorEvaluator(nextOperator);
				
					// Handle first node
				if( current == null ) {
					root = next;
				} else {
					current.addArgument(next);
				}
				
				//next.operator = nextOperator;
				next.addArgument(evaluator);
				current = next;
			}
			
			previousOperator = nextOperator;
			this.cursor += 2;
		}
		
		return root;
	}
	
	private AEvaluator functionCall() {
			// Determine start of a function call
		Token functionNameToken = this.lookupToken(this.cursor);
		if( !this.checkToken(functionNameToken, TokenType.FUNCTION) ) {
			return null;
		}
		
		this.cursor++;
		Token argumentsStart = this.lookupToken(this.cursor);
		
		if( !this.checkToken(argumentsStart, TokenType.SPECIAL_CHARACTER, '(') ) {
			Logger.get().error(this, FAILED_TO_PARSE, "Arguments expected after function call.");
			return null;
		}
		
		String functionName = (String) functionNameToken.value;
		AEvaluator functionCall = functionEvaluatorByName.get(functionName);
		functionCall.operator = OP_FUNCTION_CALL;
		this.cursor++;
		
			// Extract arguments, if any
		Token currentToken;
		while( (currentToken = this.lookupToken(this.cursor)) != null ) {
			if( this.checkToken(currentToken, TokenType.SPECIAL_CHARACTER, ')') ) {
				break;
			}
			
			if( this.checkToken(currentToken, TokenType.SPECIAL_CHARACTER, ',') ) {
				this.cursor++;
				continue;
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

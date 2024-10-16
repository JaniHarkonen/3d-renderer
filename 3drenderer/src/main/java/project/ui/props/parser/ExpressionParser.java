package project.ui.props.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.shared.logger.Logger;
import project.ui.jeemu.Operator;
import project.ui.jeemu.Token;
import project.ui.jeemu.TokenType;
import project.ui.props.Property;
import project.ui.props.PropertyBuilder;
import project.ui.props.parser.functions.FunctionClamp;
import project.ui.props.parser.functions.FunctionMax;
import project.ui.props.parser.functions.FunctionMin;
import project.ui.props.parser.functions.FunctionRGB;
import project.ui.props.parser.functions.FunctionRGBA;
import project.ui.props.parser.ops.AddEvaluator;
import project.ui.props.parser.ops.DivEvaluator;
import project.ui.props.parser.ops.MulEvaluator;
import project.ui.props.parser.ops.NegationEvaluator;
import project.ui.props.parser.ops.SubEvaluator;

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
	private String propertyName;
	
	public ExpressionParser() {
		this.tokens = null;
		this.cursor = 0;
		this.propertyName = null;
	}
	
	
	//public AEvaluator parse(List<Token> tokens) {
	public AEvaluator parse(String propertyName, List<Token> tokens) {
		this.tokens = tokens;
		this.propertyName = propertyName;
		return this.expression();
	}
	
	private AEvaluator expression() {
		AEvaluator root = null;
		AEvaluator current = root;
		Token currentToken;
		Operator previousOperator = Operator.OP_NONE;
		
		while( (currentToken = this.lookupToken(this.cursor)) != null ) {
			AEvaluator evaluator;	// Evaluator for the next value
			AEvaluator unary = null;	// This will contain an evaluator for a unary operation, if one is detected
			
				// Handle negation, unary operator detected
			if( this.checkToken(currentToken, TokenType.OPERATOR, Operator.OP_SUB) ) {
				unary = getOperatorEvaluator(Operator.OP_NEGATE);
				this.cursor++;
				currentToken = this.lookupToken(this.cursor);
			}
			
				// Constant values will be provided by a value provider
			if( this.checkToken(currentToken, TokenType.EVALUABLE) ) {
				PropertyBuilder builder = (PropertyBuilder) currentToken.value;
				evaluator = new ValueProvider(builder.build(this.propertyName));
			} else if( this.checkToken(currentToken, TokenType.EXPRESSION_START) ) {
					// Handle parenthesis (sub-expressions)
				this.cursor++;
				evaluator = this.expression();
				this.cursor++;
			} else {
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
				evaluator = unary;
			}
			
			Token nextToken = this.lookupToken(this.cursor + 1);
			if( !this.checkToken(nextToken, TokenType.OPERATOR) ) {
					// 'evaluator' holds either a value provider or a unary evaluator at
					// this point
				if( current == null ) {
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
		
		if( !this.checkToken(argumentsStart, TokenType.EXPRESSION_START) ) {
			Logger.get().error(this, FAILED_TO_PARSE, "Arguments expected after function call.");
			return null;
		}
		
		String functionName = (String) functionNameToken.value;
		AEvaluator functionCall = functionEvaluatorByName.get(functionName).createInstance();
		functionCall.operator = OP_FUNCTION_CALL;
		this.cursor++;
		
			// Extract arguments, if any
		Token currentToken;
		while( (currentToken = this.lookupToken(this.cursor)) != null ) {
			if( this.checkToken(currentToken, TokenType.EXPRESSION_END) ) {
				break;
			}
			
			if( this.checkToken(currentToken, TokenType.EXPRESSION_SEPARATOR) ) {
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

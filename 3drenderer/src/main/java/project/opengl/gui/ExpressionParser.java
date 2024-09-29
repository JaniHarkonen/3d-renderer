package project.opengl.gui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.gui.tokenizer.Token;
import project.gui.tokenizer.TokenType;

public class ExpressionParser {
	public static enum OpCode {
		MUL,
		DIV,
		ADD,
		SUB,
		NEGATE,
		FUNCTION_CALL,
		NONE
	}
	
	public static final String FAILED_TO_PARSE = "Failed to parse expression!";
	
	private static final Map<OpCode, Character> opCodeToChar;
	private static final Map<Character, Integer> charToPrecedence;
	private static final Map<Character, OpCode> charToOpCode;
	static {
		charToPrecedence = new HashMap<>();
		charToPrecedence.put('*', 1);
		charToPrecedence.put('/', 1);
		charToPrecedence.put('+', 2);
		charToPrecedence.put('-', 2);
		charToPrecedence.put(null, Integer.MAX_VALUE);
		
		opCodeToChar = new HashMap<>();
		opCodeToChar.put(OpCode.MUL, '*');
		opCodeToChar.put(OpCode.DIV, '/');
		opCodeToChar.put(OpCode.ADD, '+');
		opCodeToChar.put(OpCode.SUB, '-');
		
		charToOpCode = new HashMap<>();
		charToOpCode.put('*', OpCode.MUL);
		charToOpCode.put('/', OpCode.DIV);
		charToOpCode.put('+', OpCode.ADD);
		charToOpCode.put('-', OpCode.SUB);
	}

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
		Token tNextOperator = this.lookupToken(this.cursor + 1);
		Character nextOperatorCharacter;
		
		if( 
			tNextOperator == null || 
			(nextOperatorCharacter = this.isArithmeticOperator(tNextOperator)) == null 
		) {
			return null;
		}
		
		Evaluator root = new Evaluator();
		root.addArgument(this.lookupToken(this.cursor).value);
		root.opCode = charToOpCode.get(nextOperatorCharacter);
		
		this.cursor += 2;
		
		Evaluator current = root;
		Token token;
		while( (token = this.lookupToken(this.cursor)) != null ) {
			if( token.type != TokenType.EVALUABLE ) {
				break;
			}
			
			tNextOperator = this.lookupToken(this.cursor + 1);
			
			if( 
				tNextOperator == null || 
				(nextOperatorCharacter = this.isArithmeticOperator(tNextOperator)) == null 
			) {
				current.addArgument(token.value);
				break;
			}
			
			int currentOperatorPrecedence = charToPrecedence.get(opCodeToChar.get(current.opCode));
			int nextOperatorPrecedence = charToPrecedence.get(nextOperatorCharacter);
			
			if( currentOperatorPrecedence <= nextOperatorPrecedence ) {
				current.addArgument(token.value);
				
				Evaluator node = current;
				Evaluator lastValid = current;
				while( node != null && charToPrecedence.get(opCodeToChar.get(node.opCode)) <= nextOperatorPrecedence) {
					lastValid = node;
					node = node.parent;
				}
				
				current = new Evaluator();
				current.opCode = charToOpCode.get(nextOperatorCharacter);
				
				if( lastValid.parent == null ) {
					root = current;
				} else {
					for( int i = 0; i < lastValid.parent.arguments.size(); i++ ) {
						if( lastValid.parent.arguments.get(i) == lastValid ) {
							lastValid.parent.arguments.set(i, current);
							current.parent = lastValid.parent;
						}
					}
				}
				
				current.addArgument(lastValid);
			} else {
				current.addArgument(current = new Evaluator());	// 'current' swap
				current.opCode = charToOpCode.get(nextOperatorCharacter);
				current.addArgument(token.value);
			}
			
			this.cursor += 2;
		}
		
		return root;
	}
	
	private Object getEvaluable(Token token) {
		if( token != null && token.type == TokenType.EVALUABLE ) {
			return token.value;
		}
		
		return null;
	}
	
	private Character isArithmeticOperator(Token token) {
		if( token == null || token.type != TokenType.SPECIAL_CHARACTER ) {
			return null;
		}
		
		if (
			token.value.equals('-') || token.value.equals('+') || token.value.equals('*') || 
			token.value.equals('/')
		) {
			return (Character) token.value;
		}
		
		return null;
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

package project.opengl.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.gui.tokenizer.Token;
import project.gui.tokenizer.TokenType;
import project.utils.DebugUtils;

public class EP {
	public static enum OpCode {
		MUL,
		DIV,
		MOD,
		ADD,
		SUB,
		NEGATE,
		NONE
	}
	
	private static final Map<OpCode, Character> opCodeToChar;
	private static final Map<Character, Integer> charToPrecedence;
	private static final Map<Character, OpCode> charToOpCode;
	static {
		charToPrecedence = new HashMap<>();
		charToPrecedence.put('*', 1);
		charToPrecedence.put('/', 1);
		charToPrecedence.put('%', 1);
		charToPrecedence.put('+', 2);
		charToPrecedence.put('-', 2);
		charToPrecedence.put(null, Integer.MAX_VALUE);
		
		opCodeToChar = new HashMap<>();
		opCodeToChar.put(OpCode.MUL, '*');
		opCodeToChar.put(OpCode.DIV, '/');
		opCodeToChar.put(OpCode.MOD, '%');
		opCodeToChar.put(OpCode.ADD, '+');
		opCodeToChar.put(OpCode.SUB, '-');
		
		charToOpCode = new HashMap<>();
		charToOpCode.put('*', OpCode.MUL);
		charToOpCode.put('/', OpCode.DIV);
		charToOpCode.put('%', OpCode.MOD);
		charToOpCode.put('+', OpCode.ADD);
		charToOpCode.put('-', OpCode.SUB);
	}
	
	public class ASTNode {
		public ASTNode parent;
		public OpCode opCode;
		public List<Object> arguments;
		
		private ASTNode() {
			this.parent = null;
			this.opCode = OpCode.NONE;
			this.arguments = new ArrayList<>();
		}
		
		private void addArgument(ASTNode node) {
			this.arguments.add(node);
			node.parent = this;
		}
		
		private void addArgument(Object ambiguous) {
			this.arguments.add(ambiguous);
		}
	}

	private ASTNode currentNode;
	private List<Token> tokens;
	private int cursor;
	private Character previousOperator;
	
	public EP() {
		this.currentNode = null;
		this.tokens = null;
		this.cursor = 0;
		this.previousOperator = null;
	}
	
	
	public ASTNode parse(List<Token> tokens) {
		this.tokens = tokens;
		this.currentNode = new ASTNode();
		return this.expression();
	}
	
	private ASTNode expression() {
		Token tNextOperator = this.lookupToken(this.cursor + 1);
		Character nextOperatorCharacter;
		
		if( 
			tNextOperator == null || 
			(nextOperatorCharacter = this.isArithmeticOperator(tNextOperator)) == null 
		) {
			return null;
		}
		
		ASTNode root = new ASTNode();
		root.addArgument(this.lookupToken(this.cursor).value);
		root.opCode = charToOpCode.get(nextOperatorCharacter);
		
		this.cursor += 2;
		
		ASTNode current = root;
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
				
				ASTNode node = current;
				ASTNode lastValid = current;
				while( node != null && charToPrecedence.get(opCodeToChar.get(node.opCode)) <= nextOperatorPrecedence) {
					lastValid = node;
					node = node.parent;
				}
				
				current = new ASTNode();
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
				current.addArgument(current = new ASTNode());	// 'current' swap
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
			token.value.equals('/') || token.value.equals('%')
		) {
			return (Character) token.value;
		}
		
		return null;
	}
	
	/*private ASTNode negation() {
		Token token = this.lookupToken(this.position);
		
		if( token.type != TokenType.SPECIAL_CHARACTER || !token.value.equals('-') ) {
			return null;
		}
		
		
	}*/
	
	/*private void isPercentage(Token token) {
		if( token != null && token.type == TokenType.PERCENTAGE ) {
			return (Property) token.value;
		}
		
		return null;
	}*/
	
	private void function() {
		
	}
	
	private Token lookupToken(int position) {
		if( position >= this.tokens.size() ) {
			return null;
		}
		
		return this.tokens.get(position);
	}
}

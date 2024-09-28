package project.opengl.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.gui.props.Property;
import project.gui.tokenizer.Token;
import project.gui.tokenizer.TokenType;
import project.shared.logger.Logger;

public class EP {
	public static enum OpCode {
		MUL,
		DIV,
		ADD,
		SUB,
		NEGATE,
		CALL,
		NONE
	}
	
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
	
	
	public class ASTNode {
		private ASTNode parent;
		public OpCode opCode;
		public List<Object> arguments;
		
		private ASTNode() {
			this.parent = null;
			this.opCode = OpCode.NONE;
			this.arguments = new ArrayList<>();
		}
		
		@SuppressWarnings("incomplete-switch")
		public Property evaluate(Context context) {
			
				// Handle non two-operand operations
			if( this.opCode == OpCode.CALL ) {
				
			} else if( this.opCode == OpCode.NEGATE ) {
				Property arg1 = this.evaluateArgument(this.arguments.get(0), context);
				Object o1 = arg1.getValue();
				String propertyName = arg1.getName();
				
				if( !(o1 instanceof Float) ) {
					Logger.get().error(
						this, 
						"Failed to parse expression!", 
						"Negation is only possible with numeric values."
					);
					return new Property(propertyName, 1.0f, Property.PX);
				}
				
				return new Property(propertyName, -((float) o1), Property.PX);
			}
			
			Property arg1 = this.evaluateArgument(this.arguments.get(0), context);
			Property arg2 = this.evaluateArgument(this.arguments.get(1), context);
			String propertyName = arg1.getName();
			
			switch( this.opCode ) {
				case MUL: 
				case DIV: {
						// Allow multiplication and division only for floats
					if( !(arg1.getValue() instanceof Float) || !(arg2.getValue() instanceof Float) ) {
						Logger.get().error(
							this, 
							"Failed to parse expression!", 
							"Multiplication, division and are only possible with numeric "
							+ "values, such as pixels, percentages, rows, columns or numbers"
						);
						break;
					}
					
					float f1 = (float) arg1.getValue();
					float f2 = (float) arg2.getValue();
					
					if( this.opCode == OpCode.DIV ) {
						if( f2 == 0.0f ) {
							Logger.get().error(this, "Failed to parse expression!", "Division by 0.0");
							break;
						}
						
						f2 = 1.0f / f2; // Flip to divide through multiplication
					}
					
					return new Property(propertyName, f1 * f2, Property.PX);
				}
				case ADD: 
				case SUB: {
					Object o1 = arg1.getValue();
					Object o2 = arg2.getValue();
					boolean isStringConcatenation = (
						this.opCode == OpCode.ADD && (o1 instanceof String || o2 instanceof String)
					);
					
					if( this.opCode == OpCode.SUB || !isStringConcatenation ) {
						if( (!(o1 instanceof Float) || !(o2 instanceof Float)) ) {
							Logger.get().error(
								this, 
								"Failed to parse expression!", 
								"Addition is only possible with numeric values and strings, and "
								+ "subtraction is only possible with numerical values."
							);
							break;
						}
					}
					
					if( isStringConcatenation ) {
						return (
							new Property(propertyName, o1.toString() + o2.toString(), Property.STRING)
						);
					}
					
					float f1 = (float) o1;
					float f2 = (this.opCode == OpCode.ADD) ? (float) o2 : -(float) o2; // Flip
					return new Property(propertyName, f1 + f2, Property.PX);
				}
				case NONE: return null;
			}
			
			return new Property(propertyName, 1.0f, Property.PX);
		}
		
		private Property evaluateArgument(Object argument, Context context) {
			if( argument instanceof ASTNode ) {
				return ((ASTNode) argument).evaluate(context);
			}
			
			Property initial = (Property) argument;
			
			if( initial.getType().equals(Property.STRING) ) {
				return initial;
			}
			
			return new Property(initial.getName(), context.evaluate(initial), Property.PX);
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
			token.value.equals('/')
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
	
	private void function() {
		
	}
	
	private Token lookupToken(int position) {
		if( position >= this.tokens.size() ) {
			return null;
		}
		
		return this.tokens.get(position);
	}
}

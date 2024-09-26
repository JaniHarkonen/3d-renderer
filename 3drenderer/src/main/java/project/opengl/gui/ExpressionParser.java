package project.opengl.gui;

import java.util.ArrayList;
import java.util.List;

import project.gui.props.Property;
import project.shared.logger.Logger;
import project.utils.DebugUtils;

public class ExpressionParser {

	private static final Operator OP_MUL = new Operator("*", 1);
	private static final Operator OP_DIV = new Operator("/", 1);
	private static final Operator OP_MOD = new Operator("%", 1);
	private static final Operator OP_ADD = new Operator("+", 2);
	private static final Operator OP_SUB = new Operator("-", 2);
	
	private static class Operator {
		private final String code;
		private final int precedence;
		
		private Operator(String code, int precedence) {
			this.code = code;
			this.precedence = precedence;
		}
	}
	
	public class Operation {
		private static final int MAX_ARGUMENT_COUNT = 8;
		private final List<Object> arguments;
		private Operator operator;
		
		private Operation(Operator operator) {
			this.arguments = new ArrayList<>();
			this.arguments.add(null);
			this.operator = operator;
		}
		
		
		private void setArgument(int argumentIndex, Object argument) {
			this.arguments.set(argumentIndex, argument);
		}
		
		private void addArgument(Object argument) {
			this.arguments.add(argument);
		}
		
		/*public Property execute(Context context) {
			Property result = new Property((String) null);
			
		}*/
	}
	
	class Token {
		private static final String SPECIAL_CHARACTER = "special";
		private static final String EVALUABLE = "evaluable";
		private static final String PERCENTAGE = "percentage";
		
		private final String type;
		private final Object value;
		
		private Token(String type, Object value) {
			this.type = type;
			this.value = value;
		}
		
		public Object getValue() {
			return this.value;
		}
		
		public String getType() {
			return this.type;
		}
	}
	
	
	public ExpressionParser() {
		
	}
	
	
	public List<Token> tokenize(String expression) {
		final String expressionStart = Property.EXPRESSION + "(";
		final int length = expression.length();
		
		if( 
			expression.length() < expressionStart.length()
		) {
			Logger.get().error(
				this, 
				"Failed to parse expression: " + expression, 
				"Expression must begin with 'expr' and its calculation must be confined "
				+ "between parenthesis."
			);
			return null;
		}
		
		if( expression.charAt(expression.length() - 1) != ')' ) {
			Logger.get().error(
				this, 
				"Failed to parse expression: " + expression, 
				"Expression must end with a closing parenthesis ')'."
			);
			return null;
		}
		
		List<Token> tokens = new ArrayList<>();
		
		int cursor = expressionStart.length();
		while( cursor < length ) {
			char charAt = expression.charAt(cursor);
			//DebugUtils.log(this, "'" + charAt + "'");
			
			if( 
				(charAt >= 'A' && charAt <= 'Z') || 
				(charAt >= 'a' && charAt <= 'z') ||
				charAt == '_'
			) {
					// Must be a function call
				/*while( cursor < length ) {
					
				}*/
			} else if( charAt >= '0' && charAt <= '9' ) {
					// Must be a numeric value
				boolean isDecimalFound = false;
				float value = 0;
				float factor = 10;
				String type = "";
				
				while( cursor < length ) {
					char numberChar = expression.charAt(cursor);
					
						// Handle decimal point
					if( numberChar == '.' ) {
						if( isDecimalFound ) {
							Logger.get().error(
								this, 
								"Failed to parse expression: " + expression, 
								"Expression contains a value with multiple decimal points."
							);
							return null;
						}
						
						factor = 0.1f;
						isDecimalFound = true;
					} else if( numberChar >= '0' && numberChar <= '9' ) {
							// Handle digits
						float digit = numberChar - '0';
						if( factor < 1 ) {
							value += digit * factor;
							factor /= 10;
						} else {
							value = value * factor + digit;
						}
					} else if(
						(numberChar >= 'A' && numberChar <= 'Z') || 
						(numberChar >= 'a' && numberChar <= 'z') 
					) {
							// Handle property type
						type += Character.toString(numberChar).toLowerCase();
					} else if( numberChar =='%' ) {
							// Handle percent (not evaluable as of yet)
						tokens.add(new Token(Token.PERCENTAGE, value));
						break;
					} else {
						// Validate property type
						switch( type ) {
							case Property.PX:
							case Property.C:
							case Property.R: {
								tokens.add(new Token(Token.EVALUABLE, new Property(null, value, type)));
							} break;
							case "": {
								tokens.add(new Token(Token.EVALUABLE, new Property(null, value, Property.NUMBER)));
							} break;
							
							default: {
								Logger.get().error(
									this, 
									"Failed to parse expression: " + expression, 
									"Invalid property type '" + type + "'."
								);
								return null;
							}
						}
						
						cursor--;
						break;
					}
					
					cursor++;
				}
			} else if( charAt == '"' || charAt == '\'' || charAt == '`' ) {
					// Must be a string
				int end = cursor + 1;
				boolean ignoreNext = false;
				char endChar = 0;
				while( end < length ) {
					endChar = expression.charAt(end);
					
					if( endChar == charAt && !ignoreNext ) {
						break;
					} else if( endChar == '\\' ) {
						ignoreNext = !ignoreNext;
						end++;
						continue;
					}
					
					ignoreNext = false;
					end++;
				}
				
				if( ignoreNext || endChar != charAt ) {
					Logger.get().error(
						this, 
						"Failed to parse expression: " + expression, 
						"Expression contains a non-closed string."
					);
					return null;
				}
				
				Property evaluable = new Property(
					null, expression.substring(cursor + 1, end), Property.STRING
				);
				tokens.add(new Token(Token.EVALUABLE, evaluable));
				cursor = end;
			} else if(
				charAt == '(' || charAt == ')' || charAt == '+' || charAt == '-' || 
				charAt == '*' || charAt == '/' || charAt == '%'
			) {
					// Allowed special characters
				tokens.add(new Token(Token.SPECIAL_CHARACTER, charAt));
			}
			
			cursor++;
		}
		
		if( tokens.size() == 0 ) {
			Logger.get().error(
				this, 
				"Failed to parse expression: " + expression, 
				"Expression contains no calculation."
			);
			return null;
		}
		
		tokens.remove(tokens.size() - 1);	// Remove expr closing parenthesis
		return tokens;
	}
	
	public Property parse(String expression) {
		Property result = new Property((String) null);
		return result;
	}
	
	/*private Operation generateAST() {
		
	}*/
}

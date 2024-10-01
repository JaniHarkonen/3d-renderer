package project.gui.props.parser;

import java.util.ArrayList;
import java.util.List;

import project.gui.props.Property;
import project.shared.logger.Logger;

public abstract class AEvaluator {
	public static Property evaluateArgument(AEvaluator argument, IStyleCascade cascade) {
		return (argument != null) ? argument.evaluate(cascade) : null;
	}
	
	public static void logErrorInSignature(
		Object me, String functionName, String signature, String expects, String incorrect
	) {
		Logger.get().error(
			me, 
			ExpressionParser.FAILED_TO_EVALUATE, 
			functionName + "(" + signature + ") expects " + expects + ".",
			"Received " + incorrect + "."
		);
	}
	
	public AEvaluator parent;
	public Operator operator;
	protected List<AEvaluator> arguments;
	
	public AEvaluator() {
		this.parent = null;
		this.operator = Operator.OP_NONE;
		this.arguments = new ArrayList<>();
	}

	public abstract Property evaluate(IStyleCascade cascade);
	
	public abstract AEvaluator createInstance();
	
	void addArgument(AEvaluator evaluator) {
		this.arguments.add(evaluator);
		evaluator.setParent(this);
	}
	
	void setArgument(int index, AEvaluator node) {
		this.arguments.set(index, node);
		node.parent = this;
	}
	
	public void setParent(AEvaluator parent) {
		this.parent = parent;
	}
	
	public AEvaluator getArgument(int index) {
		if( index < 0 || index >= this.arguments.size() ) {
			return null;
		}
		
		return this.arguments.get(index);
	}
	
	protected boolean requireFloat(String errorMessage, Property... properties) {
		for( Property property : properties ) {
			if( !property.isNumeric() ) {
				Logger.get().error(
					this, ExpressionParser.FAILED_TO_PARSE, errorMessage
				);
				return false;
			}
		}
		return true;
	}
	
	protected boolean requireArgumentMinCount(int count, String functionName) {
		if( this.arguments.size() >= count ) {
			Logger.get().error(
				this, 
				ExpressionParser.FAILED_TO_PARSE, 
				functionName + "() requires at least one value."
			);
			return false;
		}
		return true;
	}
	
	protected boolean requireExactArgumentCount(int count, String functionName) {
		if( this.arguments.size() == count ) {
			Logger.get().error(
				this, 
				ExpressionParser.FAILED_TO_PARSE, 
				functionName + "() requires " + count + " values."
			);
			return false;
		}
		return true;
	}
	
	public boolean isOperator(Operator operator) {
		return (this.operator == operator);
	}
}

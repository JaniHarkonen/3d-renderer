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
	
	public static void logArgumentCountMismatch(
		Object me, String function, int expected, int received
	) {
		Logger.get().error(
			me, 
			ExpressionParser.FAILED_TO_EVALUATE, 
			function + " expects " + expected + " arguments.", 
			"Received " + received + "arguments."
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
	
	public boolean isOperator(Operator operator) {
		return (this.operator == operator);
	}
}

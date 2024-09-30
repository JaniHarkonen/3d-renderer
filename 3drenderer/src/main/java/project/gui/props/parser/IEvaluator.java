package project.gui.props.parser;

import project.gui.props.Property;

public interface IEvaluator {
	public Property evaluate(IStyleCascade context);
	public void setParent(Evaluator parent);
}

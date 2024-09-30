package project.opengl.gui;

import project.gui.props.Property;
import project.gui.tokenizer.IContext;

public interface IEvaluator {
	public Property evaluate(IContext context);
	public void setParent(Evaluator parent);
}

package project.opengl.gui;

import project.gui.props.Property;

public interface IEvaluator {
	public Property evaluate(Context context);
	public void setParent(Evaluator parent);
}

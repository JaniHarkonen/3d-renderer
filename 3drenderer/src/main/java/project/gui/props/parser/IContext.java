package project.gui.props.parser;

import org.joml.Vector4f;

import project.gui.props.Properties;
import project.gui.props.Property;

public interface IContext {

	public void evaluateProperties(Properties properties);
	
	public float evaluateFloat(Property property);
	
	public String evaluateString(Property property);
	
	public Vector4f evaluateColor(Property property);
	
	public Object evaluate(Property property);
}

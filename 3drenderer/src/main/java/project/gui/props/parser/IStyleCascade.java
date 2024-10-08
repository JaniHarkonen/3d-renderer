package project.gui.props.parser;

import org.joml.Vector4f;

import project.gui.props.Properties;
import project.gui.props.Property;

public interface IStyleCascade {

	public void evaluateProperties(Properties properties);
	
	public float evaluateFloat(Property property, float defaultValue);
	
	public String evaluateString(Property property, String defaultValue);
	
	public Vector4f evaluateColor(Property property, Vector4f defaultValue);
	
	public Object evaluate(Property property, Object defaultValue);
}

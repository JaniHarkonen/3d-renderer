package project.gui.props;

import org.joml.Vector4f;

public interface IStyleCascade {

	public Properties.Statistics evaluateProperties(Properties properties);
	
	public float evaluateNumeric(Property property, float defaultValue);
	
	public String evaluateString(Property property, String defaultValue);
	
	public Vector4f evaluateColor(Property property, Vector4f defaultValue);
	
	public Object evaluate(Property property, Object defaultValue);
	
	/**
	 * Creates a new instance of this style cascade copying each evaluated property to the new 
	 * instance.
	 * @return New cascade instance.
	 */
	public IStyleCascade newCascade();
}

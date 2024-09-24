package project.gui;

import project.gui.props.Properties;
import project.gui.props.Property;

public class Div extends AGUIElement {

	public Div(GUI gui, String id) {
		super(gui, id);
	}
	
	private Div(AGUIElement src) {
		super(src);
	}

	
		// DEBUG CODE
	@Override
	public void tick(float deltaTime) {
		//float left = (float) this.properties.getProperty(Properties.LEFT).getValue();
		//this.properties.getProperty(Properties.LEFT).set(left += deltaTime, Property.PX);
	}
	
	@Override
	public AGUIElement rendererCopy() {
		return new Div(this);
	}

	@Override
	public boolean rendererEquals(AGUIElement previous) {
		if( !(previous instanceof Div) ) {
			return false;
		}
		return this.properties.equals(((Div) previous).properties);
	}
}

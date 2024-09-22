package project.gui;

import project.Application;
import project.Window;
import project.gui.props.Properties;
import project.gui.props.Property;

public class Body extends AGUIElement {

	public Body(GUI gui) {
		super(gui, "BODY");
	}
	
	private Body(Body src) {
		super(src);
	}

	
	@Override
	public void tick(float deltaTime) {
			// Update width and height based on window dimensions
		Window window = Application.getApp().getWindow();
		this.properties.getProperty(Properties.WIDTH).set(window.getWidth(), Property.PX);
		this.properties.getProperty(Properties.HEIGHT).set(window.getHeight(), Property.PX);
		super.tick(deltaTime);
	}
	
	@Override
	public AGUIElement rendererCopy() {
		return new Body(this);
	}

	@Override
	public boolean rendererEquals(AGUIElement previous) {
		if( !(previous instanceof Body) ) {
			return false;
		}
		return this.properties.equals(((Body) previous).properties);
	}
}

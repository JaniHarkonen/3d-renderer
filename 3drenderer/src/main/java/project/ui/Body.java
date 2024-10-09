package project.ui;

import project.Application;
import project.Window;
import project.ui.props.Properties;
import project.ui.props.Property;

public class Body extends AUIElement {
	public static final String BODY_ID = "BODY";

	public Body(UI gui) {
		super(gui, BODY_ID);
		this.properties.setProperty(
			Properties.WIDTH, new Property(Properties.WIDTH, 0.0f, Property.PX)
		);
		this.properties.setProperty(
			Properties.HEIGHT, new Property(Properties.HEIGHT, 0.0f, Property.PX)
		);
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
	public AUIElement rendererCopy() {
		return new Body(this);
	}

	@Override
	public boolean rendererEquals(AUIElement previous) {
		if( !(previous instanceof Body) ) {
			return false;
		}
		return this.statistics.equals(((Body) previous).statistics);
	}
	
	@Override
	public Body createInstance(UI ui, String id) {
		return new Body(ui);
	}
}

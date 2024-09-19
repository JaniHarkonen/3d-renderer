package project.gui;

import org.joml.Vector4f;

import project.Application;
import project.component.Transform;
import project.core.IRenderable;

public abstract class AGUIElement implements IRenderable {
	protected final GUI gui;
	protected final String id;
	
	protected Transform transform;
	protected Vector4f primaryColor;
	protected Vector4f secondaryColor;
	
	public AGUIElement(GUI gui, String id) {
		this.id = id;
		this.gui = gui;
		this.transform = new Transform();
		this.primaryColor = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);
		this.secondaryColor = new Vector4f(1.0f, 1.0f, 1.0f, 0.0f);
	}
	
	protected AGUIElement(AGUIElement src) {
		this.gui = null;
		this.id = src.id;
		this.transform = new Transform(src.transform);
		this.primaryColor = new Vector4f(src.primaryColor);
		this.secondaryColor = new Vector4f(src.secondaryColor);
	}
	
	
	@Override
	public void submitToRenderer() {
		Application.getApp().getRenderer().getBackGameState().listGUIElement(this);
	}
	
	public abstract AGUIElement rendererCopy();
	
	public abstract boolean rendererEquals(AGUIElement previous);
	
	
	public GUI getGUI() {
		return this.gui;
	}
	
	public String getID() {
		return this.id;
	}
	
	public Transform getTransform() {
		return this.transform;
	}
	
	public Vector4f getPrimaryColor() {
		return this.primaryColor;
	}
	
	public Vector4f getSecondaryColor() {
		return this.secondaryColor;
	}
}

package project.gui;

import org.joml.Vector4f;

import project.Application;
import project.component.Transform;
import project.scene.ASceneObject;

public abstract class AGUIElement extends ASceneObject {
	protected final GUI gui;
	
	protected Vector4f primaryColor;
	protected Vector4f secondaryColor;
	
	public AGUIElement(GUI gui) {
		super(null);
		
		this.gui = gui;
		this.primaryColor = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);
		this.secondaryColor = new Vector4f(1.0f, 1.0f, 1.0f, 0.0f);
	}
	
	protected AGUIElement(AGUIElement src) {
		super(null, src.id);
		
		this.gui = null;
		src.transform.updateTransformMatrix();
		this.transform = new Transform(src.transform);
		this.primaryColor = new Vector4f(src.primaryColor);
		this.secondaryColor = new Vector4f(src.secondaryColor);
	}
	
	
	@Override
	public void submitToRenderer() {
		Application.getApp().getRenderer().getBackGameState().listGUIElement(this);
	}
	
	public GUI getGUI() {
		return this.gui;
	}
	
	public Vector4f getPrimaryColor() {
		return this.primaryColor;
	}
	
	public Vector4f getSecondaryColor() {
		return this.secondaryColor;
	}
}

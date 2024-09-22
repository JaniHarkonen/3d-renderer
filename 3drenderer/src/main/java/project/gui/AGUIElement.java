package project.gui;

import java.util.ArrayList;
import java.util.List;

import project.Application;
import project.core.IRenderable;
import project.gui.props.Properties;

public abstract class AGUIElement implements IRenderable {
	protected final GUI gui;
	protected final String id;
	
	//protected Transform transform;
	protected Properties properties;
	//protected Vector4f primaryColor;
	//protected Vector4f secondaryColor;
	protected List<AGUIElement> children;
	
	public AGUIElement(GUI gui, String id) {
		this.id = id;
		this.gui = gui;
		this.properties = new Properties();
		this.children = new ArrayList<>();
		//this.transform = new Transform();
		//this.primaryColor = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);
		//this.secondaryColor = new Vector4f(1.0f, 1.0f, 1.0f, 0.0f);
	}
	
	protected AGUIElement(AGUIElement src) {
		this.gui = null;
		this.id = src.id;
		this.properties = new Properties(src.properties);
		
		this.children = new ArrayList<>(src.children.size());
		for( AGUIElement child : src.children ) {
			this.addChild(child.rendererCopy());
		}
		//this.properties = new Properties(src.properties);
		//this.transform = new Transform(src.transform);
		//this.primaryColor = new Vector4f(src.primaryColor);
		//this.secondaryColor = new Vector4f(src.secondaryColor);
	}
	
	
	public void tick(float deltaTime) {
		for( AGUIElement child : this.children ) {
			child.tick(deltaTime);
		}
	}
	
	@Override
	public void submitToRenderer() {
		//Application.getApp().getRenderer().getBackGameState().listGUIElement(this);
		Application.getApp().getRenderer().getBackGameState().listGUIRoot(this);
	}
	
	public abstract AGUIElement rendererCopy();
	
	public abstract boolean rendererEquals(AGUIElement previous);
	
	public void addChild(AGUIElement... children) {
		for( AGUIElement child : children ) {
			this.children.add(child);
		}
	}
	
	
	public GUI getGUI() {
		return this.gui;
	}
	
	public String getID() {
		return this.id;
	}
	
	/*public Transform getTransform() {
		return this.transform;
	}*/
	
	public Properties getProperties() {
		return this.properties;
	}
	
	public List<AGUIElement> getChildren() {
		return this.children;
	}
	
	/*public Vector4f getPrimaryColor() {
		return this.primaryColor;
	}
	
	public Vector4f getSecondaryColor() {
		return this.secondaryColor;
	}*/
}

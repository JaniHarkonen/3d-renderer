package project.gui;

import java.util.ArrayList;
import java.util.List;

import project.Application;
import project.core.IRenderable;
import project.gui.props.Properties;

public abstract class AGUIElement implements IRenderable {
	protected final GUI gui;
	protected final String id;
	
	protected Properties properties;
	protected List<AGUIElement> children;
	
	public AGUIElement(GUI gui, String id) {
		this.id = id;
		this.gui = gui;
		this.properties = new Properties(this);
		this.children = new ArrayList<>();
	}
	
	protected AGUIElement(AGUIElement src) {
		this.gui = null;
		this.id = src.id;
		this.properties = new Properties(src.properties);
		
		this.children = new ArrayList<>(src.children.size());
		for( AGUIElement child : src.children ) {
			this.addChild(child.rendererCopy());
		}
	}
	
	
	public void tick(float deltaTime) {
		for( AGUIElement child : this.children ) {
			child.tick(deltaTime);
		}
	}
	
	@Override
	public void submitToRenderer() {
		Application.getApp().getRenderer().getBackGameState().listGUIRoot(this);
	}
	
	public abstract AGUIElement rendererCopy();
	
	public abstract boolean rendererEquals(AGUIElement previous);
	
	void addChild(AGUIElement... children) {
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
	
	public Properties getProperties() {
		return this.properties;
	}
	
	public List<AGUIElement> getChildren() {
		return this.children;
	}
}

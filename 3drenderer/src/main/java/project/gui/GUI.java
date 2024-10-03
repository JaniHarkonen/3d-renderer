package project.gui;

import java.util.HashMap;
import java.util.Map;

import project.Application;
import project.core.IRenderable;
import project.core.ITickable;
import project.shared.logger.Logger;

public class GUI implements ITickable, IRenderable {
	private Body body;
	private Map<String, AGUIElement> elementTable;
	private Map<String, Theme> themes;
	private Map<String, AGUIElement> elementCollections;
	private Theme activeTheme;
	
	public GUI() {
		this.body = null;
		this.elementTable = new HashMap<>();
		this.themes = new HashMap<>();
		this.elementCollections = new HashMap<>();
		this.activeTheme = Theme.NULL_THEME;
	}
	
	
	public void initialize() {
		this.body = new Body(this);
	}
	
	@Override
	public void tick(float deltaTime) {
		this.body.tick(deltaTime);
	}
	
	@Override
	public void submitToRenderer() {
		Application.getApp().getRenderer().getBackGameState().listGUI(this);
	}
	
	public boolean addChildTo(AGUIElement parent, AGUIElement child) {
		if( this.getElementByID(child.getID()) != null ) {
			Logger.get().warn(
				this, 
				"Failed to add element with ID '" + child.getID() + "'!", 
				"Such an element already exists in the GUI."
			);
			return false;
		}
		
		this.elementTable.put(child.getID(), child);
		parent.addChild(child);
		return true;
	}
	
	public void setBody(Body body) {
		this.body = body;
	}
	
	public void addTheme(String name, Theme theme) {
		this.themes.put(name, theme);
	}
	
	public void setActiveTheme(String name) {
		this.activeTheme = this.themes.get(name);
	}
	
	public Body getBody() {
		return this.body;
	}
	
	public AGUIElement getElementByID(String id) {
		return this.elementTable.get(id);
	}
	
	public Theme getActiveTheme() {
		return this.activeTheme;
	}
}

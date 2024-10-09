package project.gui;

import java.util.HashMap;
import java.util.Map;

import project.Application;
import project.core.IRenderable;
import project.core.ITickable;
import project.gui.props.IStyleCascade;
import project.shared.logger.Logger;

public class GUI implements ITickable, IRenderable {
	private Body body;
	private Map<String, AGUIElement> elementTable;
	private Map<String, Theme> themes;
	private Theme activeTheme;
	
	public GUI() {
		this.body = null;
		this.elementTable = new HashMap<>();
		this.themes = new HashMap<>();
		this.activeTheme = null;
	}
	
	
	@Override
	public void tick(float deltaTime) {
		this.body.tick(deltaTime);
	}
	
	@Override
	public void submitToRenderer() {
		Application.getApp().getRenderer().getBackGameState().listGUI(this);
	}
	
	public void evaluateElementProperties(IStyleCascade cascade) {
		if( this.body != null ) {
			this.evaluateElementProperties(this.body, cascade);
		}
	}
	
	private void evaluateElementProperties(AGUIElement element, IStyleCascade cascade) {
		element.evaluateStatistics(cascade);
		IStyleCascade inheritedCascade = cascade.newCascade();
		
		if( element.hasText() ) {
			element.getText().evaluateStatistics(cascade);
		}
		
		for( AGUIElement child : element.children ) {
			this.evaluateElementProperties(child, inheritedCascade);
		}
	}
	
	public boolean addChildTo(String parentID, AGUIElement child) {
		AGUIElement parent = this.getElementByID(parentID);
		if( parent == null ) {
			this.errorNonExistingParent(parentID, child.getID());
			return false;
		}
		
		AGUIElement error = this.canAddElement(child);
		
		if( error != null ) {
			this.errorAlreadyExists(error.getID());
			return false;
		}
		
		this.registerElement(child);
		parent.addChild(child);
		return true;
	}
	
	public boolean addCollectionTo(String parentID, String childID, Collection childCollection) {
		AGUIElement parent = this.getElementByID(parentID);
		if( parent == null ) {
			this.errorNonExistingParent(parentID, childID);
			return false;
		}
		
		AGUIElement childRoot = childCollection.buildNode(this, childID);
		AGUIElement error = this.canAddElement(childRoot);
		
		if( error != null ) {
			this.errorAlreadyExists(error.getID());
			return false;
		}
		
		
		this.registerElement(childRoot);
		parent.addChild(childRoot);
		return true;
	}
	
	private boolean registerElement(AGUIElement element) {
		if( this.getElementByID(element.getID()) != null ) {
			Logger.get().warn(
				this, 
				"An element with an overlapping ID '" + element.getID() + "' was added.", 
				"The previous element was replaced, which may lead to unexpected behavior."
			);
			return false;
		}
		
		this.elementTable.put(element.getID(), element);
		for( AGUIElement child : element.getChildren() ) {
			if( !this.registerElement(child) ) {
				return false;
			}
		}
		
		return true;
	}
	
	public void bodyFromCollection(Collection collection) {
		if( !(collection.getRoot() instanceof Body) ) {
			Logger.get().error(
				this, 
				"Unable to create a body from a collection.", 
				"Collection root node is not a body."
			);
		}
		
		this.elementTable = new HashMap<>();
		AGUIElement body = collection.buildNode(this, null);
		this.registerElement(body);
		this.body = (Body) body;
	}
	
	private AGUIElement canAddElement(AGUIElement element) {
		if( this.getElementByID(element.getID()) != null ) {
			return element;
		}
		
		for( AGUIElement child : element.getChildren() ) {
			AGUIElement error = this.canAddElement(child);
			if( error != null ) {
				return error;
			}
		}
		
		return null;
	}
	
	private void errorNonExistingParent(String parentID, String childID) {
		Logger.get().error(
			this, 
			"Can't add a child '" + childID + "' to parent element '" + parentID + "'!", 
			"Parent doesn't exist."
		);
	}
	
	private void errorAlreadyExists(String id) {
		Logger.get().error(
			this, 
			"Failed to add element with ID '" + id + "'!", 
			"Such an element already exists in the GUI."
		);
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
	
	public Theme getTheme(String name) {
		return this.themes.get(name);
	}
}

package project.gui;

import java.util.HashMap;
import java.util.Map;

import project.core.ITickable;
import project.shared.logger.Logger;

public class GUI implements ITickable {
	private Body body;
	private Map<String, AGUIElement> elementTable;
	
	public GUI() {
		this.body = null;
		this.elementTable = new HashMap<>();
	}
	
	
	public void initialize() {
		this.body = new Body(this);
	}
	
	@Override
	public void tick(float deltaTime) {
		this.body.tick(deltaTime);
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
	
	public Body getBody() {
		return this.body;
	}
	
	public AGUIElement getElementByID(String id) {
		return this.elementTable.get(id);
	}
}

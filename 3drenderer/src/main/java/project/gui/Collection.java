package project.gui;

import project.gui.props.Properties;

public class Collection {

	private AGUIElement model;
	
	public Collection(AGUIElement model) {
		this.model = model;
	}
	
	
	public AGUIElement buildNode(GUI ui, String containerID) {
		return this.buildNode(ui, containerID, this.model);
	}
	
	private AGUIElement buildNode(GUI ui, String containerID, AGUIElement node) {
			// For the base case, we are assuming that 'containerID' is ""
		String id;
		if( containerID == null ) {
			id = node.getID();
		} else if( node.getID() == null ) {
			id = containerID;
		} else {
			id = containerID + "." + node.getID();
		}
		
		AGUIElement root = node.createInstance(ui, id);
		root.setProperties(new Properties(node.getProperties()));
		
		for( AGUIElement child : node.getChildren() ) {
			root.addChild(this.buildNode(ui, containerID, child));
		}
		
		return root;
	}
	
	public void addChildTo(AGUIElement parent, AGUIElement child) {
		parent.addChild(child);
	}
	
	public void setModel(AGUIElement model) {
		this.model = model;
	}
	
	public AGUIElement getRoot() {
		return this.model;
	}
}

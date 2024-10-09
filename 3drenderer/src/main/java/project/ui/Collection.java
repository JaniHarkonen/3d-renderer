package project.ui;

import project.ui.props.Properties;

public class Collection {

	private AUIElement model;
	
	public Collection(AUIElement model) {
		this.model = model;
	}
	
	
	public AUIElement buildNode(GUI ui, String containerID) {
		return this.buildNode(ui, containerID, this.model);
	}
	
	private AUIElement buildNode(GUI ui, String containerID, AUIElement node) {
			// For the base case, we are assuming that 'containerID' is ""
		String id;
		if( containerID == null ) {
			id = node.getID();
		} else if( node.getID() == null ) {
			id = containerID;
		} else {
			id = containerID + "." + node.getID();
		}
		
		AUIElement root = node.createInstance(ui, id);
		root.setProperties(new Properties(node.getProperties()));
		
		if( node.getText() != null ) {
			root.setText(new Text(node.getText()));
		}
		
		for( AUIElement child : node.getChildren() ) {
			root.addChild(this.buildNode(ui, containerID, child));
		}
		
		return root;
	}
	
	public void addChildTo(AUIElement parent, AUIElement child) {
		parent.addChild(child);
	}
	
	public void setModel(AUIElement model) {
		this.model = model;
	}
	
	public AUIElement getRoot() {
		return this.model;
	}
}

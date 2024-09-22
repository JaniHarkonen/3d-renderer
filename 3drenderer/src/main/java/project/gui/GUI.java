package project.gui;

public class GUI {
	private Body body;
	
	public GUI() {
		this.body = null;
	}
	
	
	public void initialize() {
		this.body = new Body(this);
	}
	
	public Body getBody() {
		return this.body;
	}
}

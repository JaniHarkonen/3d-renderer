package project.scene;

import project.geometry.Projection;

public class Camera extends SceneObject {

	private Projection projection;
	
	public Camera(Projection projection) {
		super();
		this.projection = projection;
	}
	
	public Projection getProjection() {
		return this.projection;
	}
}

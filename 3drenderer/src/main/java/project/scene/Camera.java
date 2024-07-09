package project.scene;

import project.geometry.Projection;

public class Camera {

	private Projection projection;
	
	public Camera(Projection projection) {
		this.projection = projection;
	}
	
	public Projection getProjection() {
		return this.projection;
	}
}

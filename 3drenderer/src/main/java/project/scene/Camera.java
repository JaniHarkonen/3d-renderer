package project.scene;

import project.component.Projection;

public class Camera extends ASceneObject {

	private Projection projection;
	
	public Camera(Scene scene, Projection projection) {
		super(scene);
		this.projection = projection;
	}
	
	
	@Override
	public void updateTransformMatrix() {
		this.transformMatrix.identity()
		.rotate(this.rotationComponent.getAsQuaternion())
		.translate(-this.position.x, -this.position.y, -this.position.z);
	}
	
	public Projection getProjection() {
		return this.projection;
	}
}

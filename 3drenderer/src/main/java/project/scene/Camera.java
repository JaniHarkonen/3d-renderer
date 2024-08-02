package project.scene;

import org.joml.Matrix4f;

import project.component.Projection;

public class Camera extends ASceneObject {

	private Projection projection;
	
	public Camera(Scene scene, Projection projection) {
		super(scene);
		this.projection = projection;
	}
	
	private Camera(Camera camera) {
		super(null);
		this.transformMatrix = new Matrix4f(camera.transformMatrix);
		this.projection = new Projection(camera.projection);
	}
	
	
	@Override
	protected Camera rendererCopy() {
		return new Camera(this);
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

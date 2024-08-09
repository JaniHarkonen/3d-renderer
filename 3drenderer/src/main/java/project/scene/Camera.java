package project.scene;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import project.component.Projection;
import project.component.Rotation;

public class Camera extends ASceneObject {

	private Projection projection;
	
	public Camera(Scene scene, Projection projection) {
		super(scene);
		this.projection = projection;
	}
	
	private Camera(Camera src) {
		super(null);
		this.position = new Vector3f(src.position);
		this.rotationComponent = new Rotation(src.rotationComponent);
		src.updateTransformMatrix();
		this.transformMatrix = new Matrix4f(src.transformMatrix);
		this.projection = new Projection(src.projection);
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

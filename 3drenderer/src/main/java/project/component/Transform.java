package project.component;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transform {
	
	private Vector3f position;
	private Rotation rotationComponent;
	private Vector3f scale;
	private Matrix4f transformMatrix;
	
	public Transform() {
		this.position = new Vector3f(0.0f);
		this.rotationComponent = new Rotation();
		this.scale = new Vector3f(1.0f);
		this.transformMatrix = new Matrix4f();
	}
	
	public Transform(Transform src) {
		this.position = new Vector3f(src.position);
		this.rotationComponent = new Rotation(src.rotationComponent);
		this.scale = new Vector3f(src.scale);
		src.updateTransformMatrix();
		this.transformMatrix = new Matrix4f(src.transformMatrix);
	}
	
	
	public void updateTransformMatrix() {
		this.transformMatrix.identity()
		.rotate(this.rotationComponent.getAsQuaternion())
		.translate(-this.position.x, -this.position.y, -this.position.z);
	}
	
	public void setPosition(float x, float y, float z) {
		this.position.set(x, y, z);
	}
	
	public void setScale(float x, float y, float z) {
		this.scale.set(x, y, z);
	}
	
	public Vector3f getPosition() {
		return this.position;
	}
	
	public Rotation getRotationComponent() {
		return this.rotationComponent;
	}
	
	public Vector3f getScale() {
		return this.scale;
	}
	
	public Matrix4f getAsMatrix() {
		return this.transformMatrix;
	}
}

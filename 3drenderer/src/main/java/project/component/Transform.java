package project.component;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transform {
	protected Vector3f position;
	protected Rotator rotator;
	protected Vector3f scale;
	protected Matrix4f transformMatrix;
	
	public Transform() {
		this.position = new Vector3f(0.0f);
		this.rotator = new Rotator();
		this.scale = new Vector3f(1.0f);
		this.transformMatrix = new Matrix4f();
	}
	
	public Transform(Transform src) {
		this.position = new Vector3f(src.position);
		this.rotator = new Rotator(src.rotator);
		this.scale = new Vector3f(src.scale);
		src.updateTransformMatrix();
		this.transformMatrix = new Matrix4f(src.transformMatrix);
	}
	
	
	public void updateTransformMatrix() {
		this.transformMatrix.translationRotateScale(
			this.position, this.rotator.getAsQuaternion(), this.scale
		);
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
	
	public Rotator getRotator() {
		return this.rotator;
	}
	
	public Vector3f getScale() {
		return this.scale;
	}
	
	public Matrix4f getAsMatrix() {
		return this.transformMatrix;
	}
}

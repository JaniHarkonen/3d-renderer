package project.scene;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public abstract class SceneObject {

	private Vector3f position;
	private Quaternionf rotation;
	private Vector3f scale;
	private Matrix4f transformMatrix;
	
	
	public SceneObject() {
		this.position = new Vector3f(0.0f);
		this.rotation = new Quaternionf();
		this.scale = new Vector3f(1.0f);
		this.transformMatrix = new Matrix4f();
	}
	
	
	public void tick(float deltaTime) {
		this.position.add(0, 0, -1.0f * deltaTime);
		this.updateTransformMatrix();
	}
	
	public void updateTransformMatrix() {
		this.transformMatrix.translationRotateScale(
			this.position, this.rotation, this.scale
		);
	}
	
	public void setPosition(float x, float y, float z) {
		this.position.set(x, y, z);
	}
	
	public void setRotation(float x, float y, float z, float angle) {
		this.rotation.fromAxisAngleRad(x, y, z, angle);
	}
	
	public void setScale(float x, float y, float z) {
		this.scale.set(x, y, z);
	}
	
	public Vector3f getPosition() {
		return this.position;
	}
	
	public Quaternionf getRotation() {
		return this.rotation;
	}
	
	public Vector3f getScale() {
		return this.scale;
	}
	
	public Matrix4f getTransformMatrix() {
		return this.transformMatrix;
	}
}

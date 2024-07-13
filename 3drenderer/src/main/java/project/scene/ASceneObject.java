package project.scene;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public abstract class ASceneObject {

	protected final Scene scene;
	
	protected Vector3f position;
	protected Quaternionf rotation;
	protected Vector3f scale;
	protected Matrix4f transformMatrix;
	
	public ASceneObject(Scene scene) {
		this.position = new Vector3f(0.0f);
		this.rotation = new Quaternionf();
		this.scale = new Vector3f(0.01f);
		this.transformMatrix = new Matrix4f();
		this.scene = scene;
	}
	
	
	public abstract void tick(float deltaTime);
	
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
	
	public Scene getScene() {
		return this.scene;
	}
}

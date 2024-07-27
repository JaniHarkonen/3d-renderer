package project.scene;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public abstract class ASceneObject {

	protected final Scene scene;
	protected final List<ASceneObject> children;
	
	protected Vector3f position;
	protected Quaternionf rotation;
	protected Vector3f scale;
	protected Matrix4f transformMatrix;
	
	public ASceneObject(Scene scene) {
		this.children = new ArrayList<>();
		this.position = new Vector3f(0.0f);
		this.rotation = new Quaternionf();
		this.scale = new Vector3f(1.0f);
		this.transformMatrix = new Matrix4f();
		this.scene = scene;
	}
	
	
	public void tick(float deltaTime) {
		
	}
	
	public void updateTransformMatrix() {
		this.transformMatrix.translationRotateScale(
			this.position, this.rotation, this.scale
		);
	}
	
	public void addChild(ASceneObject child) {
		this.children.add(child);
	}
	
	public void setPosition(float x, float y, float z) {
		this.position.set(x, y, z);
	}
	
	public void setRotationXYZW(float x, float y, float z, float w) {
		this.rotation.set(x, y, z, w);
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

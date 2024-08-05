package project.scene;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import project.Application;
import project.component.Rotation;

public abstract class ASceneObject {

	protected final Scene scene;
	protected final List<ASceneObject> children;
	
	protected Vector3f position;
	protected Rotation rotationComponent;
	protected Vector3f scale;
	protected Matrix4f transformMatrix;
	
	public ASceneObject(Scene scene) {
		this.children = new ArrayList<>();
		this.position = new Vector3f(0.0f);
		this.rotationComponent = new Rotation();
		this.scale = new Vector3f(1.0f);
		this.transformMatrix = new Matrix4f();
		this.scene = scene;
	}
	
	
	public void tick(float deltaTime) {
		
	}
	
	public void submitState() {
		Application.getApp().getRenderer().submitRenderable(this.rendererCopy());
	}
	
	protected abstract ASceneObject rendererCopy();
	
	public void updateTransformMatrix() {
		this.transformMatrix.translationRotateScale(
			this.position, this.rotationComponent.getAsQuaternion(), this.scale
		);
	}
	
	public void addChild(ASceneObject child) {
		this.children.add(child);
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
	
	public Matrix4f getTransformMatrix() {
		return this.transformMatrix;
	}
	
	public Scene getScene() {
		return this.scene;
	}
	
	public List<ASceneObject> getChildren() {
		return this.children;
	}
}

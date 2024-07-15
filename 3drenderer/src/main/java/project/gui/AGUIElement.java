package project.gui;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public abstract class AGUIElement {
	protected final GUI gui;
	
	protected Vector3f position;
	protected Quaternionf rotation;
	protected Vector3f scale;
	protected Matrix4f transformMatrix;
	
	public AGUIElement(GUI gui) {
		this.gui = gui;
		
		this.position = new Vector3f(0.0f);
		this.rotation = new Quaternionf();
		this.scale = new Vector3f(1.0f);
		this.transformMatrix = new Matrix4f();
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
	
	public GUI getGUI() {
		return this.gui;
	}
}

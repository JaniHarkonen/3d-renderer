package project.component;

import org.joml.Matrix4f;

public class Projection {

	private Matrix4f projectionMatrix;
	private float fieldOfView;
	private float zNear;
	private float zFar;
	
	public Projection(float fieldOfViewDegrees, float zNear, float zFar) {
		this.projectionMatrix = new Matrix4f();
		this.fieldOfView = (float) Math.toRadians(fieldOfViewDegrees);
		this.zNear = zNear;
		this.zFar = zFar;
	}
	
	public Projection(Projection src) {
		this.projectionMatrix = new Matrix4f(src.projectionMatrix);
		this.fieldOfView = src.fieldOfView;
		this.zNear = src.zNear;
		this.zFar = src.zFar;
	}
	
	
	public void update(int width, int height) {
		this.projectionMatrix.setPerspective(
			this.fieldOfView, width / (float) height, this.zNear, this.zFar
		);
	}
	
	public Matrix4f getMatrix() {
		return this.projectionMatrix;
	}
}

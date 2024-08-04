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
	
	public Projection(Projection projection) {
		this.projectionMatrix = new Matrix4f(projection.projectionMatrix);
		this.fieldOfView = projection.fieldOfView;
		this.zNear = projection.zNear;
		this.zFar = projection.zFar;
	}
	
	
	public void update(int width, int height) {
		this.projectionMatrix.setPerspective(
			this.fieldOfView, (float) (width / height), this.zNear, this.zFar
		);
	}
	
	public Matrix4f getMatrix() {
		return this.projectionMatrix;
	}
}

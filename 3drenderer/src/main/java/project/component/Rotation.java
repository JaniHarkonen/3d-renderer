package project.component;

import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Rotation {
	public static final Vector3f X_AXIS = new Vector3f(1.0f, 0.0f, 0.0f);
	public static final Vector3f Y_AXIS = new Vector3f(0.0f, 1.0f, 0.0f);
	public static final Vector3f Z_AXIS = new Vector3f(0.0f, 0.0f, 1.0f);

	private Vector3f angles;
	private Quaternionf rotationQuaternion;
	private Quaternionf rotationQuaternionTempY;
	private Quaternionf rotationQuaternionTempZ;
	
	public Rotation() {
		this.angles = new Vector3f(0.0f);
		this.rotationQuaternion = new Quaternionf();
		this.rotationQuaternionTempY = new Quaternionf();
		this.rotationQuaternionTempZ = new Quaternionf();
	}
	
	
	private void recalculate() {
		this.rotationQuaternion.fromAxisAngleRad(X_AXIS, this.angles.x)
		.mul(
			this.rotationQuaternionTempY.fromAxisAngleRad(Y_AXIS, this.angles.y)
		).mul(
			this.rotationQuaternionTempZ.fromAxisAngleRad(Z_AXIS, this.angles.z)
		);
	}
	
	public void rotate(float xAngle, float yAngle, float zAngle) {
		this.setEulerAngles(
			this.angles.x + xAngle, this.angles.y + yAngle, this.angles.z + zAngle
		);
	}
	
	public void rotateX(float xAngle) {
		this.rotate(xAngle, 0, 0);
	}
	
	public void rotateY(float yAngle) {
		this.rotate(0, yAngle, 0);
	}
	
	public void rotateZ(float zAngle) {
		this.rotate(0, 0, zAngle);
	}
	
	public void setEulerAngles(float xAngle, float yAngle, float zAngle) {
		this.angles.set(xAngle, yAngle, zAngle);
		this.recalculate();
	}
	
	public void setXAngle(float xAngle) {
		this.setEulerAngles(xAngle, this.angles.y, this.angles.z);
	}
	
	public void setYAngle(float yAngle) {
		this.setEulerAngles(this.angles.x, yAngle, this.angles.z);
	}
	
	public void setZAngle(float zAngle) {
		this.setEulerAngles(this.angles.x, this.angles.y, zAngle);
	}
	
	public void setQuaternion(Quaternionf quaternion) {
		this.rotationQuaternion = quaternion;
	}
	
	public void setQuaternion(float x, float y, float z, float w) {
		this.rotationQuaternion.set(x, y, z, w);
	}
	
	public float getXAngle() {
		return this.angles.x;
	}
	
	public float getYAngle() {
		return this.angles.y;
	}
	
	public float getZAngle() {
		return this.angles.z;
	}
	
	public Vector3f getAsEulerAngles() {
		return this.angles;
	}
	
	public Quaternionf getAsQuaternion() {
		return this.rotationQuaternion;
	}
	
	public Vector3f getForwardVector(Vector3f result) {
		this.getBackwardVector(result);
		result.negate();
		return result;
	}
	
	public Vector3f getBackwardVector(Vector3f result) {
		this.rotationQuaternion.positiveZ(result);
		return result;
	}
	
	public Vector3f getLeftVector(Vector3f result) {
		this.getRightVector(result);
		result.negate();
		return result;
	}
	
	public Vector3f getRightVector(Vector3f result) {
		this.rotationQuaternion.positiveX(result);
		return result;
	}
	
	public Vector3f getDownVector(Vector3f result) {
		this.rotationQuaternion.positiveY(result);
		return result;
	}
	
	public Vector3f getUpVector(Vector3f result) {
		this.getDownVector(result);
		result.negate();
		return result;
	}
}

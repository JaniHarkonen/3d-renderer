package project.scene;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import project.component.Projection;
import project.component.Rotation;

public class Camera extends ASceneObject {

	private Projection projection;
	private Vector3f direction;
	private Vector3f right;
	private Vector3f up;
	private Vector3f rotation2D;
	private Matrix4f cameraTransform;
	private Rotation DEBUGrotation = new Rotation();
	
	public Camera(Scene scene, Projection projection) {
		super(scene);
		this.projection = projection;
		this.direction = new Vector3f(0.0f);
		this.right = new Vector3f(0.0f);
		this.up = new Vector3f(0.0f);
		this.rotation2D = new Vector3f(0.0f);
		this.cameraTransform = new Matrix4f();
	}
	
	
	public void rotate2D(float x, float y) {
		//this.rotation2D.add(x, y, y);
		//this.rotation.rotateAxis(x, new Vector3f(1,0,0));
		//this.rotation.rotateAxis(y, new Vector3f(0,1,0));
		//this.rotation.fromAxisAngleRad(new Vector3f(1,0,0), DEBUGx)
		//.mul((new Quaternionf()).fromAxisAngleRad(new Vector3f(0,1,0), DEBUGy));
		//this.rotation.fromAxisAngleRad(new Vector3f(1,0,0), x);
		//this.rotation.fromAxisAngleRad(new Vector3f(0,1,0), y);
		this.DEBUGrotation.rotate(x, y, 0);
		this.updateCameraTransformMatrix();
	}
	
	public void moveForward(float amount) {
		//this.cameraTransform.positiveZ(this.direction).negate().mul(amount);
		this.DEBUGrotation.getForwardVector(this.direction);
		this.position.add(this.direction.mul(amount));
		this.updateCameraTransformMatrix();
	}
	
	public void moveBackwards(float amount) {
		this.cameraTransform.positiveZ(this.direction).negate().mul(amount);
		this.position.sub(this.direction);
		this.updateCameraTransformMatrix();
	}
	
	public void moveLeft(float amount) {
		this.cameraTransform.positiveX(this.right).mul(amount);
		this.position.sub(this.right);
		this.updateCameraTransformMatrix();
	}
	
	public void moveRight(float amount) {
		this.cameraTransform.positiveX(this.right).mul(amount);
		this.position.add(this.right);
		this.updateCameraTransformMatrix();
	}
	
	public void moveUp(float amount) {
		this.cameraTransform.positiveY(this.up).mul(amount);
		this.position.add(this.up);
		this.updateCameraTransformMatrix();
	}
	
	public void moveDown(float amount) {
		this.cameraTransform.positiveY(this.up).mul(amount);
		this.position.sub(this.up);
		this.updateCameraTransformMatrix();
	}
	
	private void updateCameraTransformMatrix() {
		this.cameraTransform.identity()
		//.rotate(this.rotation)
		.rotate(this.DEBUGrotation.getAsQuaternion())
		//.rotateX(this.rotation2D.x)
		//.rotateY(this.rotation2D.y)
		//.rotateZ(this.rotation2D.z)
		.translate(-this.position.x, -this.position.y, -this.position.z);
	}
	
	@Override
	public void setPosition(float x, float y, float z) {
		super.setPosition(x, y, z);
		this.updateCameraTransformMatrix();
	}
	
	public void setRotation2D(float x, float y) {
		this.rotation2D.set(x, y, 0);
		this.updateCameraTransformMatrix();
	}
	
	public Projection getProjection() {
		return this.projection;
	}
	
	/*public Vector3f getRotation2D() {
		return this.rotation2D;
	}*/
	
	public Matrix4f getCameraTransform() {
		return this.cameraTransform;
	}
}

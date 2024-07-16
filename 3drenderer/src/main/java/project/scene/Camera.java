package project.scene;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import project.component.Projection;
import project.controls.Action;
import project.controls.Controller;
import project.controls.IControllable;
import project.testing.ActionSet;

public class Camera extends ASceneObject implements IControllable {

	private Projection projection;
	private Vector3f direction;
	private Vector3f right;
	private Vector3f up;
	private Vector2f rotation2D;
	private Matrix4f cameraTransform;
	
	private Controller DEBUGcontroller;
	
	public Camera(Scene scene, Projection projection) {
		super(scene);
		this.projection = projection;
		this.direction = new Vector3f(0.0f);
		this.right = new Vector3f(0.0f);
		this.up = new Vector3f(0.0f);
		this.rotation2D = new Vector2f(0.0f);
		this.cameraTransform = new Matrix4f();
		
		this.DEBUGcontroller = null;
	}
	
	
	@Override
	public void tick(float deltaTime) {
		this.DEBUGcontroller.tick(deltaTime);
	}
	
	public void rotate2D(float x, float y) {
		this.rotation2D.add(x, y);
		this.updateCameraTransformMatrix();
	}
	
	public void moveForward(float amount) {
		this.cameraTransform.positiveZ(this.direction).negate().mul(amount);
		this.position.add(this.direction);
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
		.rotateX(this.rotation2D.x)
		.rotateY(this.rotation2D.y)
		.translate(-this.position.x, -this.position.y, -this.position.z);
	}
	
	@Override
	public void setPosition(float x, float y, float z) {
		super.setPosition(x, y, z);
		this.updateCameraTransformMatrix();
	}
	
	public void setRotation2D(float x, float y) {
		this.rotation2D.set(x, y);
		this.updateCameraTransformMatrix();
	}
	
	public Projection getProjection() {
		return this.projection;
	}
	
	public Vector2f getRotation2D() {
		return this.rotation2D;
	}
	
	public Matrix4f getCameraTransform() {
		return this.cameraTransform;
	}


	@Override
	public void control(Action action, float deltaTime) {
		float sensitivity = 0.1f;
		float speed = 1.0f;
		
		switch( action.getActionID() ) {
			case ActionSet.LOOK_AROUND: {
				this.rotate2D(
					(float) Math.toRadians(sensitivity * action.getAxisIntensity(1)),
					(float) Math.toRadians(sensitivity * action.getAxisIntensity(0))
				);
			} break;

			case ActionSet.MOVE_FORWARD: {
				this.moveForward(action.getAxisIntensity(0) * speed * deltaTime);
			} break;

			case ActionSet.MOVE_LEFT: {
				this.moveLeft(action.getAxisIntensity(0) * speed * deltaTime);
			} break;

			case ActionSet.MOVE_BACKWARDS: {
				this.moveBackwards(action.getAxisIntensity(0) * speed * deltaTime);
			} break;

			case ActionSet.MOVE_RIGHT: {
				this.moveRight(action.getAxisIntensity(0) * speed * deltaTime);
			} break;
			
		}
	}


	@Override
	public void setController(Controller controller) {
		this.DEBUGcontroller = controller;
	}
}

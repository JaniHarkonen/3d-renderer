package project.scene;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import project.Window;
import project.geometry.Projection;

public class Camera extends SceneObject {

	private Projection projection;
	private Vector3f direction;
	private Vector3f right;
	private Vector3f up;
	private Vector2f rotation2D;
	private Matrix4f cameraTransform;
	
		// Should be stored in a separate controller object
	private double mousePreviousX;
	private double mousePreviousY;
		///////////////////////////////////////////////////
	
	public Camera(Scene scene, Projection projection) {
		super(scene);
		this.projection = projection;
		this.direction = new Vector3f(0.0f);
		this.right = new Vector3f(0.0f);
		this.up = new Vector3f(0.0f);
		this.rotation2D = new Vector2f(0.0f);
		this.cameraTransform = new Matrix4f();
		
		this.mousePreviousX = 0.0d;
		this.mousePreviousY = 0.0d;
	}
	
	
	@Override
	public void tick(float deltaTime) {
		Window window = this.scene.getApp().getWindow();
		float sensitivity = 0.1f;
		this.rotate2D(
			(float) Math.toRadians(sensitivity * (window.mouseY - this.mousePreviousY)),
			(float) Math.toRadians(sensitivity * (window.mouseX - this.mousePreviousX))
		);
		
		if( GLFW.glfwGetKey(window.getHandle(), GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS ) {
			this.moveForward(1.0f * deltaTime);
		} else if( GLFW.glfwGetKey(window.getHandle(), GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS ) {
			this.moveBackwards(1.0f * deltaTime);
		}
		
		if( GLFW.glfwGetKey(window.getHandle(), GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS ) {
			this.moveLeft(1.0f * deltaTime);
		} else if( GLFW.glfwGetKey(window.getHandle(), GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS ) {
			this.moveRight(1.0f * deltaTime);
		}
		
		GLFW.glfwSetCursorPos(window.getHandle(), window.getWidth() / 2, window.getHeight() / 2);
		
		this.mousePreviousX = window.getWidth() / 2;
		this.mousePreviousY = window.getHeight() / 2;
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
}

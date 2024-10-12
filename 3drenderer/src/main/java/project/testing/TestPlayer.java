package project.testing;

import org.joml.Vector3f;

import project.component.Projection;
import project.controls.Action;
import project.controls.Controller;
import project.controls.IControllable;
import project.scene.ASceneObject;
import project.scene.Camera;
import project.scene.Scene;

public class TestPlayer extends ASceneObject implements IControllable {
	
	private Controller controller;
	private Camera playerCamera;

	public TestPlayer(Scene scene) {
		super(scene);
		this.playerCamera = new Camera(scene, new Projection(75.0f, 5.0f, 5000.0f));
		this.addChild(playerCamera);
		this.controller = null;
	}

	
	@Override
	public void submitToRenderer() {
		for( ASceneObject child : this.getChildren() ) {
			child.submitToRenderer();
		}
	}
	
	@Override
	public TestPlayer rendererCopy() {
		return null;
	}
	
	public boolean rendererEquals(ASceneObject previous) {
		return false;
	}
	
	@Override
	public void tick(float deltaTime) {
		this.controller.tick(deltaTime);
	}
	
	@Override
	public void control(Action action, float deltaTime) {
		float sensitivity = 0.1f;
		float speed = 1000.0f;
		float finalSpeed = speed * deltaTime * action.getAxisIntensity(0);
		Camera camera = this.playerCamera;
		
		switch( action.getActionID() ) {
			case ActionSet.LOOK_AROUND: {
				camera.getTransform().getRotator().rotate(
					(float) Math.toRadians(sensitivity * action.getAxisIntensity(1)),
					(float) Math.toRadians(sensitivity * action.getAxisIntensity(0)),
					0
				);
			} break;

			case ActionSet.MOVE_FORWARD: {
				camera.getTransform().getPosition().add(
					camera.getTransform().getRotator().getForwardVector(new Vector3f(0.0f)).mul(finalSpeed)
				);
			} break;

			case ActionSet.MOVE_LEFT: {
				camera.getTransform().getPosition().add(
					camera.getTransform().getRotator().getLeftVector(new Vector3f()).mul(finalSpeed)
				);
			} break;

			case ActionSet.MOVE_BACKWARDS: {
				camera.getTransform().getPosition().add(
					camera.getTransform().getRotator().getBackwardVector(new Vector3f()).mul(finalSpeed)
				);
			} break;

			case ActionSet.MOVE_RIGHT: {
				camera.getTransform().getPosition().add(
					camera.getTransform().getRotator().getRightVector(new Vector3f()).mul(finalSpeed)
				);
			} break;
		}
	}

	@Override
	public void setController(Controller controller) {
		this.controller = controller;
	}
	
	public Camera getCamera() {
		return this.playerCamera;
	}
}

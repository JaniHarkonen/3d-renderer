package project.testing;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import project.controls.Action;
import project.controls.Controller;
import project.controls.IControllable;
import project.scene.ASceneObject;
import project.scene.PointLight;
import project.scene.Scene;

public class TestPointLight extends ASceneObject implements IControllable {
	private PointLight pointLight;
	private Controller controller;
	
	public TestPointLight(Scene scene) {
		super(scene);
		this.pointLight = new PointLight(scene, new Vector3f(1.0f, 1.0f, 1.0f), 100.5f);
		this.addChild(this.pointLight);
		this.controller = null;
	}

	
	@Override
	public void submitToRenderer() {
		for( ASceneObject child : this.getChildren() ) {
			child.submitToRenderer();
		}
	}
	
	@Override
	public TestPointLight rendererCopy() {
		return null;
	}
	
	public boolean rendererEquals(ASceneObject previous) {
		return false;
	}
	
	@Override
	public void tick(float deltaTime) {
		this.controller.tick(deltaTime);
		for( ASceneObject object : this.getChildren() ) {
			Quaternionf rotationQuaternion = this.getTransform().getRotator().getAsQuaternion();
			Vector3f position = this.getTransform().getPosition();
			object.getTransform().setPosition(position.x, position.y, position.z);
			object.getTransform().getRotator().setQuaternion(
				rotationQuaternion.x, rotationQuaternion.y, rotationQuaternion.z, rotationQuaternion.w
			);
			Vector3f scale = this.getTransform().getScale();
			object.getTransform().setScale(scale.x, scale.y, scale.z);
		}
	}
	
	@Override
	public void control(Action action, float deltaTime) {
		PointLight light = this.pointLight;
		Vector3f color = light.getColor();
		float movementSpeed = deltaTime * 100.0f;
		float intensitySpeed = deltaTime * 10.0f;
		float colorSpeed = deltaTime * 0.1f;
		Vector3f position = this.getTransform().getPosition();
		switch( action.getActionID() ) {
			case ActionSet.MOVE_FORWARD: {
				this.getTransform().setPosition(
					position.x, position.y, position.z + movementSpeed
				);
			} break;
			
			case ActionSet.MOVE_BACKWARDS: {
				this.getTransform().setPosition(
					position.x, position.y, position.z - movementSpeed
				);
			} break;
			
			case ActionSet.MOVE_LEFT: {
				this.getTransform().setPosition(
					position.x + movementSpeed, position.y, position.z
				);
			} break;
			
			case ActionSet.MOVE_RIGHT: {
				this.getTransform().setPosition(
					position.x - movementSpeed, position.y, position.z
				);
			} break;
			
			case ActionSet.LIGHT_INTENSIFY: {
				light.setIntensity(light.getIntensity() + intensitySpeed);
			} break;
			
			case ActionSet.LIGHT_DIM: {
				light.setIntensity(light.getIntensity() - intensitySpeed);
			} break;
			
			case ActionSet.LIGHT_INCREASE_RED: {
				light.setColor(color.x + colorSpeed, color.y, color.z);
			} break;
			
			case ActionSet.LIGHT_DECREASE_RED: {
				light.setColor(color.x - colorSpeed, color.y, color.z);
			} break;
			
			case ActionSet.LIGHT_INCREASE_GREEN: {
				light.setColor(color.x, color.y + colorSpeed, color.z);
			} break;
			
			case ActionSet.LIGHT_DECREASE_GREEN: {
				light.setColor(color.x, color.y - colorSpeed, color.z);
			} break;
			
			case ActionSet.LIGHT_INCREASE_BLUE: {
				light.setColor(color.x, color.y, color.z + colorSpeed);
			} break;
			
			case ActionSet.LIGHT_DECREASE_BLUE: {
				light.setColor(color.x, color.y, color.z - colorSpeed);
			} break;
		}
	}

	@Override
	public void setController(Controller controller) {
		this.controller = controller;
	}
	
	public PointLight getPointLight() {
		return this.pointLight;
	}
}

package project.scene;

import org.joml.Vector3f;

import project.component.Attenuation;
import project.controls.Action;
import project.controls.Controller;
import project.controls.IControllable;
import project.testing.ActionSet;

public class PointLight extends ASceneObject implements IControllable {

	private Attenuation attenuation;
	private Vector3f lightColor;
	private float intensity;
	private Controller DEBUGcontroller;
	
	public PointLight(Scene scene, Vector3f lightColor, float intensity) {
		super(scene);
		this.attenuation = new Attenuation(0, 0, 1);
		this.lightColor = lightColor;
		this.intensity = intensity;
		this.DEBUGcontroller = null;
	}
	
	
	@Override
	public void tick(float deltaTime) {
		this.DEBUGcontroller.tick(deltaTime);
	}
	
	@Override
	public void control(Action action, float deltaTime) {
		switch( action.getActionID() ) {
			case ActionSet.MOVE_FORWARD: {
				this.setPosition(
					this.position.x, this.position.y, this.position.z + deltaTime
				);
			} break;
			
			case ActionSet.MOVE_BACKWARDS: {
				this.setPosition(
					this.position.x, this.position.y, this.position.z - deltaTime
				);
			} break;
			
			case ActionSet.MOVE_LEFT: {
				this.setPosition(
					this.position.x + deltaTime, this.position.y, this.position.z
				);
			} break;
			
			case ActionSet.MOVE_RIGHT: {
				this.setPosition(
					this.position.x - deltaTime, this.position.y, this.position.z
				);
			} break;
			
			case ActionSet.LIGHT_INTENSIFY: {
				this.intensity += deltaTime * 0.1f;
			} break;
			
			case ActionSet.LIGHT_DIM: {
				this.intensity -= deltaTime * 0.1f;
			} break;
			
			case ActionSet.LIGHT_INCREASE_RED: {
				this.setColor(this.lightColor.x + deltaTime * 0.1f, this.lightColor.y, this.lightColor.z);
			} break;
			
			case ActionSet.LIGHT_DECREASE_RED: {
				this.setColor(this.lightColor.x - deltaTime * 0.1f, this.lightColor.y, this.lightColor.z);
			} break;
			
			case ActionSet.LIGHT_INCREASE_GREEN: {
				this.setColor(this.lightColor.x, this.lightColor.y + deltaTime * 0.1f, this.lightColor.z);
			} break;
			
			case ActionSet.LIGHT_DECREASE_GREEN: {
				this.setColor(this.lightColor.x, this.lightColor.y - deltaTime * 0.1f, this.lightColor.z);
			} break;
			
			case ActionSet.LIGHT_INCREASE_BLUE: {
				this.setColor(this.lightColor.x, this.lightColor.y, this.lightColor.z + deltaTime * 0.1f);
			} break;
			
			case ActionSet.LIGHT_DECREASE_BLUE: {
				this.setColor(this.lightColor.x, this.lightColor.y, this.lightColor.z - deltaTime * 0.1f);
			} break;
		}
	}

	@Override
	public void setController(Controller controller) {
		this.DEBUGcontroller = controller;
	}
	
	public void setAttenuation(Attenuation attenuation) {
		this.attenuation = attenuation;
	}
	
	public void setColor(float r, float g, float b) {
		this.lightColor = new Vector3f(r, g, b);
	}
	
	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}
	
	public Attenuation getAttenuation() {
		return this.attenuation;
	}
	
	public Vector3f getColor() {
		return this.lightColor;
	}
	
	public float getIntensity() {
		return this.intensity;
	}
}

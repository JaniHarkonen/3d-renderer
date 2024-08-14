package project.testing;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import project.component.Animator;
import project.scene.ASceneObject;
import project.scene.Model;
import project.scene.Scene;

public class TestDummy extends ASceneObject {

	private Model model;
	
	public TestDummy(Scene scene, Model model) {
		super(scene);
		this.model = model;
		this.model.swapAnimator(new Animator() {
			@Override
			public void onFinish() {
				this.restart();
			}
		}).setSpeed(1 / 24.0f);
		this.children.add(model);
	}
	
	public TestDummy(Scene scene) {
		this(scene, new Model(scene));
	}
	
	
	@Override
	public void submitToRenderer() {
		for( ASceneObject child : this.children ) {
			child.submitToRenderer();
		}
	}
	
	@Override
	protected TestDummy rendererCopy() {
		return null;
	}
	
	@Override
	public void tick(float deltaTime) {
		this.model.getAnimator().update(deltaTime);
		
		for( ASceneObject object : this.children ) {
			Vector3f position = getTransform().getPosition();
			object.getTransform().setPosition(position.x, position.y, position.z);
			Quaternionf rotationQuaternion = this.getTransform().getRotator().getAsQuaternion();
			object.getTransform().getRotator().setQuaternion(
				rotationQuaternion.x, rotationQuaternion.y, rotationQuaternion.z, rotationQuaternion.w
			);
			Vector3f scale = this.getTransform().getScale();
			object.getTransform().setScale(scale.x, scale.y, scale.z);
		}
	}
	
	public Model getModel() {
		return this.model;
	}	
}

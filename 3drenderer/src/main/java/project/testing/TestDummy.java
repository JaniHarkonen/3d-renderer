package project.testing;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import project.scene.ASceneObject;
import project.scene.Model;
import project.scene.Scene;

public class TestDummy extends ASceneObject {

	private Model model;
	private float animationTimer;
	private float animationFrameTime;
	
	public TestDummy(Scene scene, Model model) {
		super(scene);
		this.model = model;
		this.children.add(model);
		this.animationTimer = 0.0f;
		this.animationFrameTime = 0.075f;
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
		this.animationTimer += deltaTime;
		if( this.animationTimer >= this.animationFrameTime ) {
			if( this.model.getAnimationData() != null ) {
				this.model.getAnimationData().nextFrame();
			}
			
			this.animationTimer = 0.0f;
		}
		
		for( ASceneObject object : this.children ) {
			Vector3f position = getTransformComponent().getPosition();
			object.getTransformComponent().setPosition(position.x, position.y, position.z);
			Quaternionf rotationQuaternion = this.getTransformComponent().getRotationComponent().getAsQuaternion();
			object.getTransformComponent().getRotationComponent().setQuaternion(
				rotationQuaternion.x, rotationQuaternion.y, rotationQuaternion.z, rotationQuaternion.w
			);
			Vector3f scale = this.getTransformComponent().getScale();
			object.getTransformComponent().setScale(scale.x, scale.y, scale.z);
		}
	}
	
	public Model getModel() {
		return this.model;
	}	
}

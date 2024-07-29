package project.testing;

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
	public void tick(float deltaTime) {
		this.animationTimer += deltaTime;
		if( this.animationTimer >= this.animationFrameTime ) {
			if( this.model.getAnimationData() != null ) {
				this.model.getAnimationData().nextFrame();
			}
			
			this.animationTimer = 0.0f;
		}
		
		for( ASceneObject object : this.children ) {
			object.setPosition(this.position.x, this.position.y, this.position.z);
			object.setRotationXYZW(this.rotation.x, this.rotation.y, this.rotation.z, this.rotation.w);
			object.setScale(this.scale.x, this.scale.y, this.scale.z);
		}
	}
	
	public Model getModel() {
		return this.model;
	}	
}

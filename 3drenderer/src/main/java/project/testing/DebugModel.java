package project.testing;

import project.controls.Action;
import project.controls.Controller;
import project.controls.IControllable;
import project.scene.Model;
import project.scene.Scene;

public class DebugModel extends Model implements IControllable {
	private Controller controller;
	private boolean isNormalMapActive;

	public DebugModel(Scene scene) {
		super(scene);
		this.controller = null;
		this.isNormalMapActive = true;
	}
	
	@Override
	public void tick(float deltaTime) {
		this.controller.tick(deltaTime);
	}

	@Override
	public void control(Action action, float deltaTime) {
		if( this.isNormalMapActive ) {
			this.getMaterial(0).setTexture(1, null);
		} else {
			//this.getMaterial(0).setTexture(1, TestAssets.TEXTURE_BRICK_NORMAL);
		}
		
		this.isNormalMapActive = !this.isNormalMapActive;
	}

	@Override
	public void setController(Controller controller) {
		this.controller = controller;
	}

	public boolean isNormalMapActive() {
		return this.isNormalMapActive;
	}
}

package project.scene;

import java.util.ArrayList;
import java.util.List;

import project.Application;
import project.component.Transform;

public abstract class ASceneObject {

	protected final Scene scene;
	protected final List<ASceneObject> children;
	
	protected Transform transform;
	
	public ASceneObject(Scene scene) {
		this.children = new ArrayList<>();
		this.transform = new Transform();
		this.scene = scene;
	}
	
	
	public void tick(float deltaTime) {
		
	}
	
	public void submitToRenderer() {
		Application.getApp().getRenderer().getBackGameState().listSceneObject(this.rendererCopy());
	}
	
	protected abstract ASceneObject rendererCopy();
	
	public Transform getTransform() {
		return this.transform;
	}
	
	public void addChild(ASceneObject child) {
		this.children.add(child);
	}
	
	public Scene getScene() {
		return this.scene;
	}
	
	public List<ASceneObject> getChildren() {
		return this.children;
	}
}

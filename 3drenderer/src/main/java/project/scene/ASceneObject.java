package project.scene;

import java.util.ArrayList;
import java.util.List;

import project.Application;
import project.component.Transform;
import project.core.IRenderable;
import project.core.ITickable;
import project.core.UUID;

public abstract class ASceneObject implements IRenderable, ITickable {

	protected final long id;
	protected final Scene scene;
	protected final List<ASceneObject> children;
	
	protected Transform transform;
	
	public ASceneObject(Scene scene, long id) {
		this.id = id;
		this.children = new ArrayList<>();
		this.transform = new Transform();
		this.scene = scene;
	}
	
	public ASceneObject(Scene scene) {
		this(scene, UUID.getUUID());
	}
	
	
	@Override
	public void tick(float deltaTime) {
		// Empty by default
	}
	
	@Override
	public void submitToRenderer() {
		Application.getApp().getRenderer().getBackGameState().listSceneObject(this);
	}
	
	public abstract ASceneObject rendererCopy();
	
	public abstract boolean rendererEquals(ASceneObject previous);
	
	public Transform getTransform() {
		return this.transform;
	}
	
	public void addChild(ASceneObject child) {
		this.children.add(child);
	}
	
	public long getID() {
		return this.id;
	}
	
	public Scene getScene() {
		return this.scene;
	}
	
	public List<ASceneObject> getChildren() {
		return this.children;
	}
}

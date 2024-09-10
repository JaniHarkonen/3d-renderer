package project.server.game;

import project.server.IHasID;

public abstract class AGameObject implements IHasID {

	protected int id;
	protected Transform transform;
	
	public AGameObject() {
		this.id = UUID.nextID();
		this.transform = new Transform(this);
	}
	
	
	public void onCreate() {
		//this.id = UUID.nextID();
		
	}
	
	public abstract void tick(float deltaTime);
	
	public abstract void onDestroy();
	
	public abstract AGameObject deepCopy();
	
	public abstract boolean notifyChanges(AGameObject comparison, GameState gameState);
	
	@Override
	public int getID() {
		return this.id;
	}
	
	public Transform getTransform() {
		return this.transform;
	}
	
	public abstract int getObjectType();
}

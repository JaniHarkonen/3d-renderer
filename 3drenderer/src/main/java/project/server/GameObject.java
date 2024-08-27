package project.server;

public class GameObject {

	private long id;
	
	public GameObject(long id) {
		this.id = id;
	}
	
	
	public void tick(float deltaTime) {
		
	}
	
	public void applyChanges(GameObject changed) {
		
	}
	
	public void submit(GameState gameState) {
		
	}
	
	public long getID() {
		return this.id;
	}
}

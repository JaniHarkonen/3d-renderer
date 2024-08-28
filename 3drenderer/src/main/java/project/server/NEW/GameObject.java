package project.server.NEW;

public class GameObject {

	private final int id;
	
	public GameObject() {
		this.id = UUID.nextID();
	}
	
	
	public void tick(float deltaTime) {
		
	}
	
	public GameObject deepCopy() {
		return null;
	}
	
	public void notifyChanges(GameObject comparison, GameState gameState) {
		
	}
	
	public int getID() {
		return this.id;
	}
}

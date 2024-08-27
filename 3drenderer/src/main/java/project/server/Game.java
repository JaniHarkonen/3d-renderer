package project.server;

import java.util.LinkedHashMap;
import java.util.Map;

public class Game {

	private final Application app;
	
	private volatile GameState frontGameState;
	
	private Map<Long, GameObject> objects;
	private GameState backGameState;
	
	public Game(Application app) {
		this.app = app;
		this.objects = new LinkedHashMap<>();
		this.frontGameState = null;
		this.backGameState = null;
	}
	
	
	public void tick(float deltaTime) {
		
			// Update game objects
		for( Map.Entry<Long, GameObject> en : this.objects.entrySet() ) {
			en.getValue().tick(deltaTime);
		}
		
			// Submit changes to the back game state
		for( Map.Entry<Long, GameObject> en : this.objects.entrySet() ) {
			en.getValue().submit(this.backGameState);
		}
	}
	
	public void addObject(GameObject object) {
		this.objects.put(object.getID(), object);
		
	}
	
	public void removeObject(long id) {
		this.objects.remove(id);
	}
	
	public void removeObject(GameObject object) {
		this.removeObject(object.getID());
	}
	
	public GameState getLatestGameState() {
		return this.frontGameState;
	}
}

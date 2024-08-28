package project.server.NEW;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GameState {

	public class Delta {
		private List<GameObject> addedObjects;
		private List<GameObject> deletedObjects;
		private List<IGameComponent> alteredComponents;
	}
	
	
	private Map<Integer, GameObject> gameObjects;
	private List<GameObject> addedObjects;
	private List<Integer> deletedObjects;
	private List<IGameComponent> alteredComponents;
	
	public GameState() {
		this.gameObjects = new LinkedHashMap<>();
		this.addedObjects = new ArrayList<>();
		this.deletedObjects = new ArrayList<>();
		this.addedObjects = new ArrayList<>();
	}
	
	
	public void tick(float deltaTime) {
		for( GameObject object : this.gameObjects.values() ) {
			object.tick(deltaTime);
		}
		
		for( GameObject object : this.addedObjects ) {
			this.gameObjects.put(object.getID(), object);
		}
		
		for( Integer objectID : this.deletedObjects ) {
			this.gameObjects.remove(objectID);
		}
	}
	
	public GameState deepCopyComplete() {
		Map<Integer, GameObject> objects = new LinkedHashMap<>(this.gameObjects.size());
		for( GameObject object : this.gameObjects.values() ) {
			objects.put(object.getID(), object.deepCopy());
		}
		
			// Deep copy game objects whose copies can't already be found in the objects-map
		List<GameObject> added = new ArrayList<>(this.addedObjects.size());
		for( GameObject object : this.addedObjects ) {
			added.add(objects.getOrDefault(object.getID(), object.deepCopy()));
		}
		
		GameState copy = new GameState();
		copy.gameObjects = objects;
		copy.addedObjects = added;
		copy.deletedObjects = new ArrayList<>(this.deletedObjects);
		
		return copy;
	}
	
	public GameState deepCopyChangesOnly(GameState comparison) {
		GameState copy = new GameState();
		Map<Integer, GameObject> objects = new LinkedHashMap<>(this.gameObjects.size());
		
		for( GameObject object : this.gameObjects.values() ) {
			GameObject comparisonObject = comparison.gameObjects.get(object.getID());
			
			if( comparisonObject == null ) {
				objects.put(object.getID(), object.deepCopy());
			} else {
				object.notifyChanges(comparisonObject, copy);
			}
		}
		
		copy.addedObjects = new ArrayList<>(this.addedObjects);
		copy.deletedObjects = new ArrayList<>(this.deletedObjects);
		return copy;
	}
	
	public void notifyComponentChange(IGameComponent gameComponent) {
		this.alteredComponents.add(gameComponent);
	}
	
	public void reset() {
		this.addedObjects = new ArrayList<>();
		this.deletedObjects = new ArrayList<>();
	}
}

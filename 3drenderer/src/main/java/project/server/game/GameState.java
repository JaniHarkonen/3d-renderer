package project.server.game;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GameState {
	public class Delta {
		private List<AGameObject> addedObjects;
		private List<Integer> deletedObjects;
		private List<IGameComponent> alteredComponents;
		
		private Delta() {
			this.reset();
		}
		
		private void reset() {
			this.addedObjects = new ArrayList<>();
			this.deletedObjects = new ArrayList<>();
			this.alteredComponents = new ArrayList<>();
		}
		
		
		public List<AGameObject> getAdditions() {
			return this.addedObjects;
		}
		
		public List<IGameComponent> getAlteredComponents() {
			return this.alteredComponents;
		}
		
		public List<Integer> getDeletions() {
			return this.deletedObjects;
		}
	}
	
	private Map<Integer, AGameObject> gameObjects;
	private Delta stateDelta;
	
	
	public GameState() {
		this.gameObjects = new LinkedHashMap<>();
		this.stateDelta = new Delta();
	}
	
	
	public void tick(float deltaTime) {
		for( AGameObject object : this.gameObjects.values() ) {
			object.tick(deltaTime);
		}
		
		for( AGameObject object : this.stateDelta.addedObjects ) {
			this.gameObjects.put(object.getID(), object);
		}
		
		for( Integer objectID : this.stateDelta.deletedObjects ) {
			this.gameObjects.remove(objectID);
		}
	}
	
	public GameState deepCopyComplete() {
		Map<Integer, AGameObject> objects = new LinkedHashMap<>();
		for( AGameObject object : this.gameObjects.values() ) {
			objects.put(object.getID(), object.deepCopy());
		}
		
			// Deep copy game objects whose copies can't already be found in the objects-map
		List<AGameObject> addedObjects = this.stateDelta.addedObjects;
		List<AGameObject> added = new ArrayList<>(addedObjects.size());
		for( AGameObject object : addedObjects ) {
			added.add(objects.getOrDefault(object.getID(), object.deepCopy()));
		}
		
		GameState copy = new GameState();
		copy.gameObjects = objects;
		copy.stateDelta.addedObjects = added;
		copy.stateDelta.deletedObjects = new ArrayList<>();
		
		return copy;
	}
	
	public GameState deepCopyChangesOnly(GameState comparison) {
		GameState copy = new GameState();
		Map<Integer, AGameObject> objects = new LinkedHashMap<>();
		
		copy.stateDelta.alteredComponents = new ArrayList<>();
		
		for( AGameObject object : this.gameObjects.values() ) {
			AGameObject comparisonObject = comparison.gameObjects.get(object.getID());
			
			if( comparisonObject == null ) {
				objects.put(object.getID(), object.deepCopy());
			} else if( !object.notifyChanges(comparisonObject, copy) ) {
				objects.put(comparisonObject.getID(), comparisonObject);
			}
		}
		
		copy.stateDelta.addedObjects = new ArrayList<>(this.stateDelta.addedObjects);
		copy.stateDelta.deletedObjects = new ArrayList<>(this.stateDelta.deletedObjects);
		copy.gameObjects = objects;
		return copy;
	}
	
	public void notifyComponentChange(IGameComponent... gameComponents) {
		for( IGameComponent component : gameComponents ) {
			this.stateDelta.alteredComponents.add(component);
		}
	}
	
	public void reset() {
		this.stateDelta.reset();
	}
	
	public void addObject(AGameObject object) {
		this.stateDelta.addedObjects.add(object);
	}
	
	public Map<Integer, AGameObject> getObjects() {
		return this.gameObjects;
	}
	
	public Delta getDelta() {
		return this.stateDelta;
	}
}

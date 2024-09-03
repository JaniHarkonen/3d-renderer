package project.server.NEW;

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
			this.alteredComponents = null;
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
	
	//private List<GameObject> addedObjects;
	//private List<Integer> deletedObjects;
	//private List<IGameComponent> alteredComponents;
	
	
	public GameState() {
		this.gameObjects = new LinkedHashMap<>();
		this.stateDelta = new Delta();
		//this.addedObjects = new ArrayList<>();
		//this.deletedObjects = new ArrayList<>();
		//this.alteredComponents = null;
	}
	
	
	public void tick(float deltaTime) {
		for( AGameObject object : this.gameObjects.values() ) {
			object.tick(deltaTime);
		}
		
		//for( GameObject object : this.addedObjects ) {
		for( AGameObject object : this.stateDelta.addedObjects ) {
			this.gameObjects.put(object.getID(), object);
		}
		
		//for( Integer objectID : this.deletedObjects ) {
		for( Integer objectID : this.stateDelta.deletedObjects ) {
			this.gameObjects.remove(objectID);
		}
	}
	
	public GameState deepCopyComplete() {
		Map<Integer, AGameObject> objects = new LinkedHashMap<>(this.gameObjects.size());
		for( AGameObject object : this.gameObjects.values() ) {
			objects.put(object.getID(), object.deepCopy());
		}
		
			// Deep copy game objects whose copies can't already be found in the objects-map
		//List<GameObject> added = new ArrayList<>(this.addedObjects.size());
		//for( GameObject object : this.addedObjects ) {
		List<AGameObject> addedObjects = this.stateDelta.addedObjects;
		List<AGameObject> added = new ArrayList<>(addedObjects.size());
		for( AGameObject object : addedObjects ) {
			added.add(objects.getOrDefault(object.getID(), object.deepCopy()));
		}
		
		GameState copy = new GameState();
		copy.gameObjects = objects;
		//copy.addedObjects = added;
		copy.stateDelta.addedObjects = added;
		copy.stateDelta.deletedObjects = new ArrayList<>(this.stateDelta.deletedObjects);
		//copy.deletedObjects = new ArrayList<>(this.deletedObjects);
		
		return copy;
	}
	
	public GameState deepCopyChangesOnly(GameState comparison) {
		GameState copy = new GameState();
		Map<Integer, AGameObject> objects = new LinkedHashMap<>(this.gameObjects.size());
		
		//copy.alteredComponents = new ArrayList<>();
		copy.stateDelta.alteredComponents = new ArrayList<>();
		
		for( AGameObject object : this.gameObjects.values() ) {
			AGameObject comparisonObject = comparison.gameObjects.get(object.getID());
			
			if( comparisonObject == null ) {
				objects.put(object.getID(), object.deepCopy());
			} else if( !object.notifyChanges(comparisonObject, copy) ) {
				objects.put(comparisonObject.getID(), comparisonObject);
			}
		}
		
		//copy.addedObjects = new ArrayList<>(this.addedObjects);
		//copy.deletedObjects = new ArrayList<>(this.deletedObjects);
		copy.stateDelta.addedObjects = new ArrayList<>(this.stateDelta.addedObjects);
		copy.stateDelta.deletedObjects = new ArrayList<>(this.stateDelta.deletedObjects);
		return copy;
	}
	
	public void notifyComponentChange(IGameComponent... gameComponents) {
		for( IGameComponent component : gameComponents ) {
			this.stateDelta.alteredComponents.add(component);
			//this.alteredComponents.add(component);
		}
	}
	
	public void reset() {
		this.stateDelta.reset();
		//this.addedObjects = new ArrayList<>();
		//this.deletedObjects = new ArrayList<>();
	}
	
	public Map<Integer, AGameObject> getObjects() {
		return this.gameObjects;
	}
	
	public Delta getDelta() {
		return this.stateDelta;
	}
}

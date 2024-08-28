package project.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GameState {

	private Map<Long, GameObject> objects;
	private List<GameObject> addedObjects;
	private List<GameObject> alteredObjects;
	private List<GameObject> removedObjects;
	
	public GameState() {
		this.objects = new LinkedHashMap<>();
		this.addedObjects = new ArrayList<>();
		this.alteredObjects = new ArrayList<>();
		this.removedObjects = new ArrayList<>();
	}
	
	
	public void tick(float deltaTime) {
		Collection<GameObject> gameObjects = this.objects.values();
		for( GameObject object : gameObjects ) {
			object.tick(deltaTime);
		}
		
		for( GameObject object : gameObjects ) {
			object.tick(deltaTime);
		}
		
		while( this.addedObjects.size() > 0 ) {
			GameObject object = this.addedObjects.get(0);
			this.objects.put(object.getID(), object);
		}
		
		while( this.removedObjects.size() > 0 ) {
			this.objects.remove(this.removedObjects.get(0).getID());
		}
	}
	
	public void commitChanges() {
		while( this.addedObjects.size() > 0 ) {
			this.objects.remove(this.addedObjects.get(0).getID());
		}
		
		while( this.removedObjects.size() > 0 ) {
			this.objects.remove(this.removedObjects.get(0).getID());
		}
		
		while( this.alteredObjects.size() > 0 ) {
			GameObject alteredObject = this.alteredObjects.get(0);
			GameObject staleObject = this.objects.get(alteredObject.getID());
			
				// New object
			if( staleObject == null ) {
				this.objects.put(alteredObject.getID(), alteredObject);
			} else {
				staleObject.applyChanges(alteredObject);
			}
		}
	}
	
	public void addObject(GameObject object) {
		this.addedObjects.add(object);
	}
	
	public void alterObject(GameObject object) {
		this.alteredObjects.add(object);
	}
	
	public void removeObject(GameObject object) {
		this.removedObjects.add(object);
	}
}

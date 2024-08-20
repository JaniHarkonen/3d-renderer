package project.core;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import project.core.asset.IGraphicsAsset;
import project.scene.ASceneObject;
import project.scene.Camera;

public class GameState {
	private final Map<Long, ASceneObject> activeScene;
	private final Map<Long, ASceneObject> activeGUI;
	private final Deque<IGraphicsAsset> graphicsGenerationRequests;
	private final Deque<IGraphicsAsset> graphicsDisposalRequests;
	private final Map<String, Object> debugData;
	
	private Camera activeCamera;
	
	public GameState() {
		this.activeScene = new LinkedHashMap<>();
		this.activeGUI = new LinkedHashMap<>();
		this.graphicsGenerationRequests = new ArrayDeque<>();
		this.graphicsDisposalRequests = new ArrayDeque<>();
		this.debugData = new HashMap<>();
	}
	
	public GameState(GameState src) {
		this.activeScene = src.activeScene;
		this.activeGUI = src.activeGUI;
		this.graphicsGenerationRequests = new ArrayDeque<>();
		this.graphicsDisposalRequests = new ArrayDeque<>();
		this.debugData = new HashMap<>();
	}
	
	
	public void listSceneObject(ASceneObject object) {
		if( object instanceof Camera ) {
			Camera c = (Camera) object;
			if( this.activeCamera == null || !c.rendererEquals(this.activeCamera) ) {
				this.activeCamera = c.rendererCopy();
			} else {
				this.activeCamera = c;
			}
		} else {
			long objectID = object.getID();
			ASceneObject previous = this.activeScene.get(objectID);
			
			if( previous == null || !object.rendererEquals(previous)) {
				this.activeScene.put(objectID, object.rendererCopy());
			}
		}
	}
	
	public void listGUIElement(ASceneObject element) {
		long elementID = element.getID();
		ASceneObject previous = this.activeGUI.get(elementID);
		
		if( previous == null || !element.rendererEquals(previous) ) {
			this.activeGUI.put(elementID, element.rendererCopy());
		}
	}
	
	public void listGenerationRequest(IGraphicsAsset asset) {
		this.graphicsGenerationRequests.add(asset);
	}
	
	public void listDisposalRequest(IGraphicsAsset asset) {
		this.graphicsDisposalRequests.add(asset);
	}
	
	public IGraphicsAsset pollGenerationRequest() {
		return this.graphicsGenerationRequests.poll();
	}
	
	public IGraphicsAsset pollDisposalRequest() {
		return this.graphicsDisposalRequests.poll();
	} 
	
	public GameState setDebugData(String key, Object data) {
		this.debugData.put(key, data);
		return this;
	}

	public Object getDebugData(String key) {
		return this.debugData.get(key);
	}
	
	public Map<Long, ASceneObject> getActiveScene() {
		return this.activeScene;
	}
	
	public Map<Long, ASceneObject> getActiveGUI() {
		return this.activeGUI;
	}
	
	public Camera getActiveCamera() {
		return this.activeCamera;
	}
}

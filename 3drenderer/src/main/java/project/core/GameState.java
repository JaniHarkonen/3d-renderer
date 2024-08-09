package project.core;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import project.core.asset.IGraphicsAsset;
import project.gui.GUI;
import project.scene.ASceneObject;
import project.scene.Camera;
import project.scene.Scene;

public class GameState {

	private class SceneObjectQueue {
		private class Node {
			private Node next;
			private ASceneObject value;
			private Node(ASceneObject value) {
				this.next = null;
				this.value = value;
			}
		}
		
		private Node head;
		private Node next;
		
		private SceneObjectQueue() {
			this.head = new Node(null);
			this.next = this.head;
		}
		
		
		public void add(ASceneObject value) {
			this.next.value = value;
			this.next.next = new Node(null);
			this.next = this.next.next;
		}
		
		public void reset() {
			this.next = this.head;
		}
		
		public ASceneObject next() {
			if( this.next.value == null ) {
				return null;
			}
			
			ASceneObject result = this.next.value;
			this.next = this.next.next;
			return result;
		}
	}

	
	/************************* GameState-class *************************/
	
	private SceneObjectQueue renderedObjects;
	private Deque<IGraphicsAsset> graphicsGenerationRequests;
	private Deque<IGraphicsAsset> graphicsDisposalRequests;
	private Map<String, Object> debugData;
	private Camera activeCamera;
	
	private Scene DEBUGscene;
	private Camera DEBUGactiveCamera;
	private GUI DEBUGgui;
	
	public GameState() {
		this.renderedObjects = new SceneObjectQueue();
		this.graphicsGenerationRequests = new ArrayDeque<>();
		this.graphicsDisposalRequests = new ArrayDeque<>();
		this.debugData = new HashMap<>();
		this.activeCamera = null;
		
		this.DEBUGgui = null;
	}
	
	
	public void listRenderable(ASceneObject object) {
		if( object instanceof Camera ) {
			this.activeCamera = (Camera) object;
		} else {
			this.renderedObjects.add(object);
		}
	}
	
	public void listGenerationRequest(IGraphicsAsset asset) {
		this.graphicsGenerationRequests.add(asset);
	}
	
	public void listDisposalRequest(IGraphicsAsset asset) {
		this.graphicsDisposalRequests.add(asset);
	}
	
	public void resetQueue() {
		this.renderedObjects.reset();
	}
	
	public ASceneObject pollRenderable() {
		return this.renderedObjects.next();
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
	
	public Camera getActiveCamera() {
		return this.activeCamera;
	}
	
	public void DEBUGsetGUI(GUI gui) {
		this.DEBUGgui = gui;
	}
	public GUI DEBUGgetGUI() {
		return this.DEBUGgui;
	}
	
	/*public void DEBUGsetActiveScene(Scene scene) {
		this.DEBUGscene = scene;
	}
	public Scene DEBUGgetActiveScene() {
		return this.DEBUGscene;
	}*/
	/*public void DEBUGsetActiveCamera(Camera cam) {
		this.DEBUGactiveCamera = cam;
	}
	public Camera DEBUGgetActiveCamera() {
		return this.DEBUGactiveCamera;
	}*/
}

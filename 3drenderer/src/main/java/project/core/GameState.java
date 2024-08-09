package project.core;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import project.core.asset.IGraphicsAsset;
import project.scene.ASceneObject;
import project.scene.Camera;
import project.scene.Scene;

public class GameState {
	/**
	 * Special purpose linked list that is used to store the renderables (typically scene 
	 * objects) of a game state. The queue is first populated by the active scene via add(), 
	 * and eventually polled via next(). In order to poll the queue, the caller must pass
	 * itself as an argument. This resets the node pointer back to the head of the queue 
	 * with the assumption that the caller is a new render pass, and that the pass must 
	 * iterate over all the renderables.
	 * 
	 * @author Jani Härkönen
	 */
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
		private Object lastPoller;
		
		private SceneObjectQueue() {
			this.head = new Node(null);
			this.next = this.head;
			this.lastPoller = null;
		}
		
		
		public void add(ASceneObject value) {
			this.next.value = value;
			this.next.next = new Node(null);
			this.next = this.next.next;
		}
		
		public ASceneObject next(Object poller) {
			if( poller != this.lastPoller ) {
				this.lastPoller = poller;
				this.next = this.head;
			}
			
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
	
	private Scene DEBUGscene;
	private Camera DEBUGactiveCamera;
	
	public GameState() {
		this.renderedObjects = new SceneObjectQueue();
		this.graphicsGenerationRequests = new ArrayDeque<>();
		this.graphicsDisposalRequests = new ArrayDeque<>();
		this.debugData = new HashMap<>();
		
		this.DEBUGscene = null;
		this.DEBUGactiveCamera = null;
	}
	
	
	public void listRenderable(ASceneObject object) {
		if( object instanceof Camera ) {
			this.DEBUGactiveCamera = (Camera) object;
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
	
	public ASceneObject pollRenderable(Object me) {
		return this.renderedObjects.next(me);
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
	
	
	
	public void DEBUGsetActiveScene(Scene scene) {
		this.DEBUGscene = scene;
	}
	public Scene DEBUGgetActiveScene() {
		return this.DEBUGscene;
	}
	public void DEBUGsetActiveCamera(Camera cam) {
		this.DEBUGactiveCamera = cam;
	}
	public Camera DEBUGgetActiveCamera() {
		return this.DEBUGactiveCamera;
	}
}

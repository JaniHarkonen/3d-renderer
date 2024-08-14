package project.core;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import project.core.asset.IGraphicsAsset;
import project.scene.ASceneObject;
import project.scene.Camera;

public class GameState {

	public class SceneState {
		
		/************************* SceneIterator-class *************************/
		
		public class SceneIterator implements Iterator<ASceneObject> {
			private Node cursor;
			
			private SceneIterator() {
				this.cursor = SceneState.this.head;
			}

			@Override
			public boolean hasNext() {
				return this.cursor.value != null;
			}

			@Override
			public ASceneObject next() {
				Node next = this.cursor;
				this.cursor = this.cursor.next;
				return next.value;
			}
		}
		
		
		/************************* Node-class *************************/
		
		private class Node {
			private Node next;
			private ASceneObject value;
			private Node(ASceneObject value) {
				this.next = null;
				this.value = value;
			}
		}
		
		
		/************************* SceneState-class *************************/
		
		private Node head;
		private Node next;
		
		private SceneState() {
			this.head = new Node(null);
			this.next = this.head;
		}
		
		
		public void add(ASceneObject value) {
			this.next.value = value;
			this.next.next = new Node(null);
			this.next = this.next.next;
		}
		
		public SceneIterator iterator() {
			return new SceneIterator();
		}
	}

	
	/************************* GameState-class *************************/
	
	private final SceneState activeScene;
	private final SceneState activeGUI;
	private final Deque<IGraphicsAsset> graphicsGenerationRequests;
	private final Deque<IGraphicsAsset> graphicsDisposalRequests;
	private final Map<String, Object> debugData;
	
	private Camera activeCamera;
	
	public GameState() {
		this.activeScene = new SceneState();
		this.activeGUI = new SceneState();
		this.graphicsGenerationRequests = new ArrayDeque<>();
		this.graphicsDisposalRequests = new ArrayDeque<>();
		this.debugData = new HashMap<>();
	}
	
	public void listSceneObject(ASceneObject object) {
		if( object instanceof Camera ) {
			this.activeCamera = (Camera) object;
		} else {
			this.activeScene.add(object);
		}
	}
	
	public void listGUIElement(ASceneObject element) {
		this.activeGUI.add(element);
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
	
	public SceneState.SceneIterator getSceneIterator() {
		return this.activeScene.iterator();
	}
	
	public SceneState.SceneIterator getGUIIterator() {
		return this.activeGUI.iterator();
	}
	
	public Camera getActiveCamera() {
		return this.activeCamera;
	}
}

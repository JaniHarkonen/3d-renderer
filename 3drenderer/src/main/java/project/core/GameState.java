package project.core;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import project.core.asset.IGraphicsAsset;
import project.gui.AGUIElement;
import project.scene.ASceneObject;
import project.scene.Camera;

public class GameState {
	private final Map<Long, ASceneObject> activeScene;
	private final Deque<IGraphicsAsset> graphicsGenerationRequests;
	private final Deque<IGraphicsAsset> graphicsDisposalRequests;
	private final Map<String, Object> debugData;
	
	private Camera activeCamera;
	private AGUIElement activeGUIRoot;
	
	public GameState() {
		this.activeScene = new LinkedHashMap<>();
		this.graphicsGenerationRequests = new ArrayDeque<>();
		this.graphicsDisposalRequests = new ArrayDeque<>();
		this.debugData = new HashMap<>();
		this.activeGUIRoot = null;
	}
	
	public GameState(GameState src) {
		this.activeScene = new LinkedHashMap<>(src.activeScene);
		this.graphicsGenerationRequests = new ArrayDeque<>();
		this.graphicsDisposalRequests = new ArrayDeque<>();
		this.debugData = new HashMap<>();
		this.activeGUIRoot = src.getActiveGUIRoot();
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
			
			if( previous == null || !object.rendererEquals(previous) ) {
				this.activeScene.put(objectID, object.rendererCopy());
			}
		}
	}
	
	public void listGUIRoot(AGUIElement root) {
		if( this.activeGUIRoot == null ) {
			this.activeGUIRoot = root.rendererCopy();
		} else {
			this.listChildGUINodes(root, this.activeGUIRoot);
		}
	}
	
	private void listChildGUINodes(AGUIElement real, AGUIElement copy) {
		List<AGUIElement> realChildren = real.getChildren();
		List<AGUIElement> copyChildren = copy.getChildren();
		int minChildCount = Math.min(realChildren.size(), copyChildren.size());
		
			// Check all similar children, exit when dissimilar child found
		int i;
		for( i = 0; i < minChildCount; i++ ) {
			AGUIElement realChild = realChildren.get(i);
			AGUIElement copyChild = copyChildren.get(i);
			
			if( !copyChild.rendererEquals(realChild) ) {
				break;
			}
			
			this.listChildGUINodes(realChild, copyChild);
		}
		
			// Remove dissimilar and everything after it
		while( copyChildren.size() > i ) {
			copyChildren.remove(copyChildren.size() - 1);
		}
		
			// Copy the rest of real children
		for( ; i < realChildren.size(); i++ ) {
			copyChildren.add(realChildren.get(i).rendererCopy());
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
	
	public Collection<ASceneObject> getActiveScene() {
		return this.activeScene.values();
	}
	
	public AGUIElement getActiveGUIRoot() {
		return this.activeGUIRoot;
	}
	
	public Camera getActiveCamera() {
		return this.activeCamera;
	}
}

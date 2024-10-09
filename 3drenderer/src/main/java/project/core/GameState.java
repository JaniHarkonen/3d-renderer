package project.core;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import project.core.asset.IGraphicsAsset;
import project.scene.ASceneObject;
import project.scene.Camera;
import project.ui.AUIElement;
import project.ui.UI;
import project.ui.Theme;

public class GameState {
	private final Map<Long, ASceneObject> activeScene;
	private final Deque<IGraphicsAsset> graphicsGenerationRequests;
	private final Deque<IGraphicsAsset> graphicsDisposalRequests;
	private final Map<String, Object> debugData;
	
	private Camera activeCamera;
	private AUIElement activeUIRoot;
	private Theme activeUITheme;
	
	public GameState() {
		this.activeScene = new LinkedHashMap<>();
		this.graphicsGenerationRequests = new ArrayDeque<>();
		this.graphicsDisposalRequests = new ArrayDeque<>();
		this.debugData = new HashMap<>();
		this.activeUIRoot = null;
		this.activeUITheme = null;
	}
	
	public GameState(GameState src) {
		this.activeScene = new LinkedHashMap<>(src.activeScene);
		this.graphicsGenerationRequests = new ArrayDeque<>();
		this.graphicsDisposalRequests = new ArrayDeque<>();
		this.debugData = new HashMap<>();
		this.activeUIRoot = src.activeUIRoot;
		this.activeUITheme = src.activeUITheme;
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
	
	public void listUI(UI ui) {
		AUIElement root = ui.getBody();
		//this.activeGUITheme = gui.getActiveTheme();
		
		//if( this.activeGUITheme != Theme.NULL_THEME ) {
			//this.activeGUITheme = new Theme(gui.getActiveTheme());
		//}
		
		if( this.activeUIRoot == null || !this.activeUIRoot.rendererEquals(root) ) {
			this.activeUIRoot = root.rendererCopy();
		} else {
			this.listChildUINodes(root, this.activeUIRoot);
		}
	}
	
	private void listChildUINodes(AUIElement real, AUIElement copy) {
		List<AUIElement> realChildren = real.getChildren();
		List<AUIElement> copyChildren = copy.getChildren();
		int minChildCount = Math.min(realChildren.size(), copyChildren.size());
		
			// Check all similar children, exit when dissimilar child found
		int i;
		for( i = 0; i < minChildCount; i++ ) {
			AUIElement realChild = realChildren.get(i);
			AUIElement copyChild = copyChildren.get(i);
			
			if( !copyChild.rendererEquals(realChild) ) {
				break;
			}
			
			this.listChildUINodes(realChild, copyChild);
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
	
	public AUIElement getActiveUIRoot() {
		return this.activeUIRoot;
	}
	
	public Camera getActiveCamera() {
		return this.activeCamera;
	}
	
	public Theme getActiveUITheme() {
		return this.activeUITheme;
	}
}

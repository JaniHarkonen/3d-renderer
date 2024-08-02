package project.scene;

import java.util.ArrayDeque;
import java.util.Deque;

import project.asset.IGraphicsAsset;

public class GameState {

	private Deque<ASceneObject> renderedObjects;
	private Deque<IGraphicsAsset> graphicsGenerationRequests;
	private Deque<IGraphicsAsset> graphicsDisposalRequests;
	private Camera activeCamera;
	
	public GameState() {
		this.renderedObjects = new ArrayDeque<>();
		this.graphicsGenerationRequests = new ArrayDeque<>();
		this.graphicsDisposalRequests = new ArrayDeque<>();
		this.activeCamera = null;
	}
	
	
	public void listRenderable(ASceneObject object) {
		this.renderedObjects.add(object);
	}
	
	public void listGenerationRequest(IGraphicsAsset asset) {
		this.graphicsGenerationRequests.add(asset);
	}
	
	public void listDisposalRequest(IGraphicsAsset asset) {
		this.graphicsDisposalRequests.add(asset);
	}
	
	public ASceneObject pollRenderable() {
		return this.renderedObjects.poll();
	}
	
	public IGraphicsAsset pollGenerationRequest() {
		return this.graphicsGenerationRequests.poll();
	}
	
	public IGraphicsAsset pollDisposalRequest() {
		return this.graphicsDisposalRequests.poll();
	} 
	
	public void setActiveCamera(Camera camera) {
		this.activeCamera = camera;
	}
	
	public Camera getActiveCamera() {
		return this.activeCamera;
	}
}

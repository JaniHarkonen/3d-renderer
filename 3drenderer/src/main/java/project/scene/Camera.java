package project.scene;

import project.component.CameraTransform;
import project.component.Projection;

public class Camera extends ASceneObject {

	private Projection projection;
	private CameraTransform transform;
	
	public Camera(Scene scene, Projection projection) {
		super(scene);
		this.projection = projection;
		this.transform = new CameraTransform();
	}
	
	private Camera(Camera src) {
		super(null);
		this.transform = new CameraTransform(src.transform);
		this.projection = new Projection(src.projection);
	}
	
	
	@Override
	protected Camera rendererCopy() {
		return new Camera(this);
	}
	
	@Override
	public CameraTransform getTransform() {
		return this.transform;
	}
	
	public Projection getProjection() {
		return this.projection;
	}
}

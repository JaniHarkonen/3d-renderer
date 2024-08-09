package project.scene;

import project.component.CameraTransform;
import project.component.Projection;

public class Camera extends ASceneObject {

	private Projection projection;
	private CameraTransform transformComponent;
	
	public Camera(Scene scene, Projection projection) {
		super(scene);
		this.projection = projection;
		this.transformComponent = new CameraTransform();
	}
	
	private Camera(Camera src) {
		super(null);
		this.transformComponent = new CameraTransform(src.transformComponent);
		this.projection = new Projection(src.projection);
	}
	
	
	@Override
	protected Camera rendererCopy() {
		return new Camera(this);
	}
	
	@Override
	public CameraTransform getTransformComponent() {
		return this.transformComponent;
	}
	
	public Projection getProjection() {
		return this.projection;
	}
}

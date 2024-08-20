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
		super(null, src.id);
		this.transform = new CameraTransform(src.transform);
		this.projection = new Projection(src.projection);
	}
	
	
	@Override
	public Camera rendererCopy() {
		return new Camera(this);
	}
	
	@Override
	public boolean rendererEquals(ASceneObject previous) {
		if( !(previous instanceof Camera) ) {
			return false;
		}
		
		Camera c = (Camera) previous;
		return (
			this.id == c.id && 
			this.transform.equals(c.transform) && 
			this.projection.equals(c.projection)
		);
	}
	
	@Override
	public CameraTransform getTransform() {
		return this.transform;
	}
	
	public Projection getProjection() {
		return this.projection;
	}
}

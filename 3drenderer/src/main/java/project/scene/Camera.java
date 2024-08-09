package project.scene;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import project.component.Projection;
import project.component.Rotation;
import project.component.Transform;

public class Camera extends ASceneObject {

	private Projection projection;
	private Transform transformComponent;
	
	public Camera(Scene scene, Projection projection) {
		super(scene);
		this.projection = projection;
		this.transformComponent = new Transform();
	}
	
	private Camera(Camera src) {
		super(null);
		/*this.position = new Vector3f(src.position);
		this.rotationComponent = new Rotation(src.rotationComponent);
		src.updateTransformMatrix();
		this.transformMatrix = new Matrix4f(src.transformMatrix);*/
		this.transformComponent = new Transform(src.transformComponent);
		this.projection = new Projection(src.projection);
	}
	
	
	@Override
	protected Camera rendererCopy() {
		return new Camera(this);
	}
	
	@Override
	public void setPosition(float x, float y, float z) {
		this.transformComponent.setPosition(x, y, z);
	}
	
	@Override
	public Vector3f getPosition() {
		return this.transformComponent.getPosition();
	}
	
	@Override
	public Rotation getRotationComponent() {
		return this.transformComponent.getRotationComponent();
	}
	
	@Override
	public Matrix4f getTransformMatrix() {
		return this.transformComponent.getAsMatrix();
	}
	
	/*@Override
	public void updateTransformMatrix() {
		this.transformMatrix.identity()
		.rotate(this.rotationComponent.getAsQuaternion())
		.translate(-this.position.x, -this.position.y, -this.position.z);
	}*/
	
	public Projection getProjection() {
		return this.projection;
	}
}

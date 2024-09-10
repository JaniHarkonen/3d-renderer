package project.server.NEW;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import project.component.Rotator;
import project.server.IHasID;

public class Transform implements IGameComponent {
	protected Vector3f position;
	protected Rotator rotator;
	protected Vector3f scale;
	protected Matrix4f transformMatrix;
	
	private IHasID host;
	
	public Transform(IHasID host) {
		this.host = host;
		this.position = new Vector3f(0.0f);
		this.rotator = new Rotator();
		this.scale = new Vector3f(1.0f);
		this.transformMatrix = new Matrix4f();
	}
	
	/**
	 * Copy constructor. Creates a new Transform based on a source Transform.
	 * 
	 * @param src Source Transform.
	 */
	public Transform(Transform src) {
		this.position = new Vector3f(src.position);
		this.rotator = new Rotator(src.rotator);
		this.scale = new Vector3f(src.scale);
		src.updateTransformMatrix();
		this.transformMatrix = new Matrix4f(src.transformMatrix);
	}
	
	
	public void updateTransformMatrix() {
		this.transformMatrix.translationRotateScale(
			this.position, this.rotator.getAsQuaternion(), this.scale
		);
	}
	
	public void setPosition(float x, float y, float z) {
		this.position.set(x, y, z);
	}
	
	public void setScale(float x, float y, float z) {
		this.scale.set(x, y, z);
	}
	
	public Vector3f getPosition() {
		return this.position;
	}
	
	public Rotator getRotator() {
		return this.rotator;
	}
	
	public Vector3f getScale() {
		return this.scale;
	}
	
	public Matrix4f getAsMatrix() {
		return this.transformMatrix;
	}
	
	public int getHostID() {
		return this.host.getID();
	}
	
	@Override
	public boolean equals(Object o) {
		if( !(o instanceof Transform) ) {
			return false;
		}
		
		Transform t = (Transform) o;
		return(
			this.position.equals(t.position) &&
			this.rotator.equals(t.rotator) &&
			this.scale.equals(t.scale)
		);
	}
}

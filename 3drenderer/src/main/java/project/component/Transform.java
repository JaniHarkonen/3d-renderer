package project.component;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transform {
	private Vector3f position;
	private Rotation rotationComponent;
	private Vector3f scale;
	private Matrix4f transformMatrix;
	
	public Transform() {
		this.position = new Vector3f(0.0f);
		this.rotationComponent = new Rotation();
		//this.scale = 
		
		
	}
}

package project.scene;

import org.joml.Vector3f;

public class AmbientLight extends ASceneObject {

	private Vector3f lightColor;
	private float intensity;
	
	public AmbientLight(Scene scene, Vector3f lightColor, float intensity) {
		super(scene);
		this.lightColor = lightColor;
		this.intensity = intensity;
	}


	@Override
	public void tick(float deltaTime) {
		
	}
	
	public void setColor(float r, float g, float b) {
		this.lightColor = new Vector3f(r, g, b);
	}
	
	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}
	
	public Vector3f getColor() {
		return this.lightColor;
	}
	
	public float getIntensity() {
		return this.intensity;
	}
}

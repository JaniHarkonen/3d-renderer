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
	
	private AmbientLight(AmbientLight ambientLight) {
		super(null);
		this.lightColor = new Vector3f(ambientLight.lightColor);
		this.intensity = ambientLight.intensity;
	}


	@Override
	protected AmbientLight rendererCopy() {
		return new AmbientLight(this);
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

package project.scene;

import org.joml.Vector3f;

import project.component.Attenuation;

public class PointLight extends ASceneObject {

	private Attenuation attenuation;
	private Vector3f lightColor;
	private float intensity;
	
	public PointLight(Scene scene, Vector3f lightColor, float intensity) {
		super(scene);
		this.attenuation = new Attenuation(0, 0, 1);
		this.lightColor = lightColor;
		this.intensity = intensity;
	}
	
	
	public void setAttenuation(Attenuation attenuation) {
		this.attenuation = attenuation;
	}
	
	public void setColor(float r, float g, float b) {
		this.lightColor = new Vector3f(r, g, b);
	}
	
	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}
	
	public Attenuation getAttenuation() {
		return this.attenuation;
	}
	
	public Vector3f getColor() {
		return this.lightColor;
	}
	
	public float getIntensity() {
		return this.intensity;
	}
}

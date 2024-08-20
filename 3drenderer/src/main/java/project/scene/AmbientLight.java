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
	
	private AmbientLight(AmbientLight src) {
		super(null, src.id);
		this.lightColor = new Vector3f(src.lightColor);
		this.intensity = src.intensity;
	}


	@Override
	public AmbientLight rendererCopy() {
		return new AmbientLight(this);
	}
	
	@Override
	public boolean rendererEquals(ASceneObject previous) {
		if( !(previous instanceof AmbientLight) ) {
			return false;
		}
		
		AmbientLight al = (AmbientLight) previous;
		return (
			this.id == al.id &&
			this.lightColor.equals(al.lightColor) &&
			this.intensity == al.intensity
		);
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

package project.opengl.shader.uniform.object.ptlight;

import project.opengl.shader.uniform.AUniformObject;
import project.opengl.shader.uniform.UFloat1;
import project.opengl.shader.uniform.UVector3f;
import project.opengl.shader.uniform.object.attenuation.UAttenuation;

public class UPointLight extends AUniformObject<SSPointLight> {

	private UVector3f position;
	private UVector3f color;
	private UFloat1 intensity;
	private UAttenuation att;
	
	public UPointLight(String name) {
		super(name);
		
		this.position = new UVector3f();
		this.color = new UVector3f();
		this.intensity = new UFloat1();
		this.att = new UAttenuation();
		
		this
		.addField("position", this.position)
		.addField("color", this.color)
		.addField("intensity", this.intensity)
		.addField("att", this.att);
	}
	
	public UPointLight() {
		this("");
	}

	
	@Override
	public void update(SSPointLight value) {
		this.position.update(value.position);
		this.color.update(value.color);
		this.intensity.update(value.intensity);
		this.att.update(value.att);
	}
}

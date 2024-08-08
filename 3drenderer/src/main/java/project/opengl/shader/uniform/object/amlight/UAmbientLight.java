package project.opengl.shader.uniform.object.amlight;

import project.opengl.shader.uniform.AUniformObject;
import project.opengl.shader.uniform.UFloat1;
import project.opengl.shader.uniform.UVector3f;

public class UAmbientLight extends AUniformObject<SSAmbientLight> {

	private UFloat1 factor;
	private UVector3f color;
	
	public UAmbientLight(String name) {
		super(name);
		
		this.factor = new UFloat1();
		this.color = new UVector3f();
		
		this
		.addField("factor", this.factor)
		.addField("color", this.color);
	}
	
	public UAmbientLight() {
		this("");
	}
	

	@Override
	public void update(SSAmbientLight value) {
		this.factor.update(value.factor);
		this.color.update(value.color);
	}
}

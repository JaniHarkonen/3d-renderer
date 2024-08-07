package project.opengl.shader.custom.amlight;

import project.opengl.shader.test.AUniformObject;
import project.opengl.shader.test.UFloat1;
import project.opengl.shader.test.UVector3f;

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

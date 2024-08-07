package project.opengl.shader.uniform.object.attenuation;

import project.opengl.shader.uniform.AUniformObject;
import project.opengl.shader.uniform.UFloat1;

public class UAttenuation extends AUniformObject<SSAttenuation> {

	private UFloat1 constant;
	private UFloat1 exponent;
	private UFloat1 linear;
	
	public UAttenuation(String name) {
		super(name);
		
		this.constant = new UFloat1();
		this.exponent = new UFloat1();
		this.linear = new UFloat1();
		
		this
		.addField("constant", this.constant)
		.addField("exponent", this.exponent)
		.addField("linear", this.linear);
	}
	
	public UAttenuation() {
		this("");
	}

	
	@Override
	public void update(SSAttenuation value) {
		this.constant.update(value.constant);
		this.exponent.update(value.exponent);
		this.linear.update(value.linear);
	}
}

package project.opengl.shader.custom.cshadow;

import project.opengl.shader.uniform.AUniformObject;
import project.opengl.shader.uniform.UAMatrix4f;
import project.opengl.shader.uniform.UFloat1;

public class UCascadeShadow extends AUniformObject<SSCascadeShadow>{

	private UAMatrix4f lightView;
    private UFloat1 splitDistance;
	
	public UCascadeShadow(String name) {
		super(name);
		
		this.lightView = new UAMatrix4f();
		this.splitDistance = new UFloat1();
		
		this
		.addField("lightView", this.lightView)
		.addField("splitDistance", this.splitDistance);
	}
	
	public UCascadeShadow() {
		this("");
	}

	
	@Override
	public void update(SSCascadeShadow value) {
		this.lightView.update(value.lightView);
		this.splitDistance.update(value.splitDistance);
	}
}

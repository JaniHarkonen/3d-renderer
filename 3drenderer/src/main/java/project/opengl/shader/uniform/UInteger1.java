package project.opengl.shader.uniform;

import org.lwjgl.opengl.GL46;

public class UInteger1 extends AUniformPrimitive<Integer> {

	public UInteger1() {
		this("");
	}
	
	public UInteger1(String name) {
		super(name);
	}

	
	@Override
	public void update(Integer value) {
		GL46.glUniform1i(this.location, value);
	}
}

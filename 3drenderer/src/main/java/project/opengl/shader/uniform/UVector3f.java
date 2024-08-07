package project.opengl.shader.uniform;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL46;

public class UVector3f extends AUniformPrimitive<Vector3f> {

	public UVector3f() {
		this("");
	}
	
	public UVector3f(String name) {
		super(name);
	}

	
	@Override
	public void update(Vector3f value) {
		GL46.glUniform3f(this.location, value.x, value.y, value.z);
	}
}

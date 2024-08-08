package project.opengl.shader.uniform;

import org.joml.Vector4f;
import org.lwjgl.opengl.GL46;

public class UVector4f extends AUniformPrimitive<Vector4f> {

	public UVector4f() {
		this("");
	}
	
	public UVector4f(String name) {
		super(name);
	}

	
	@Override
	public void update(Vector4f value) {
		GL46.glUniform4f(this.location, value.x, value.y, value.z, value.w);
	}
}

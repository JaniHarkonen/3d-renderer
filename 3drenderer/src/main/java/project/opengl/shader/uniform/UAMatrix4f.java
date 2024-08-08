package project.opengl.shader.uniform;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryStack;

public class UAMatrix4f extends AUniformPrimitive<Matrix4f> {

	public UAMatrix4f() {
		this("");
	}
	
	public UAMatrix4f(String name) {
		super(name);
	}

	
	@Override
	public void update(Matrix4f value) {
		try( MemoryStack stack = MemoryStack.stackPush() ) {
			GL46.glUniformMatrix4fv(
				this.location, 
				false, 
				value.get(stack.mallocFloat(16))
			);
		}
	}
	
	public void update(Matrix4f[] values) {
		try( MemoryStack stack = MemoryStack.stackPush() ) {
			
			int length = (values != null) ? values.length : 0;
			FloatBuffer arrayBuffer = stack.mallocFloat(16 * length);
			for( int i = 0; i < length; i++ ) {
				values[i].get(16 * i, arrayBuffer);
			}
			
			GL46.glUniformMatrix4fv(this.location, false, arrayBuffer);
		}
	}
}

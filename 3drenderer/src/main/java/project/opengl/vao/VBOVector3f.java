package project.opengl.vao;

import org.joml.Vector3f;

public class VBOVector3f extends AVBO<Vector3f> {

	public VBOVector3f() {
		super(3);
	}

	@Override
	protected void populate(Vector3f[] data) {
		float[] buffer = new float[data.length * 3];
		for( int i = 0; i < buffer.length; i += 3 ) {
			Vector3f vector = data[i];
			buffer[i] = vector.x;
			buffer[i + 1] = vector.y;
			buffer[i + 2] = vector.z;
		}
		this.populate(buffer);
	}
}

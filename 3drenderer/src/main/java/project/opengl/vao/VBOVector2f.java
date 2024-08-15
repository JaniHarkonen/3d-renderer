package project.opengl.vao;

import org.joml.Vector2f;

public class VBOVector2f extends AVBO<Vector2f> {

	public VBOVector2f() {
		super(2);
	}

	@Override
	protected void populate(Vector2f[] data) {
		float[] buffer = new float[data.length * 2];
		for( int i = 0; i < buffer.length; i += 2 ) {
			Vector2f vector = data[i];
			buffer[i] = vector.x;
			buffer[i + 1] = vector.y;
		}
		this.populate(buffer);
	}
}

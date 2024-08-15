package project.opengl.vao;

public class VBOFloat extends AVBO<Float> {

	public VBOFloat(int size) {
		super(size);
	}

	@Override
	protected void populate(Float[] data) {
		float[] buffer = new float[data.length];
		for( int i = 0; i < data.length; i++ ) {
			buffer[i] = data[i];
		}
		this.populate(buffer);
	}
}

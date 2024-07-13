package project.asset;

public class Mesh {

	private float[] positions;
	private float[] textureCoordinates;
	private int[] indices;
	private int vertexCount;
	
	public Mesh() {
		this.populate(
			new float[] {
				0.0f, 0.5f, -1.0f,
				-0.5f, -0.5f, -1.0f,
				0.5f, -0.5f, -1.0f
			}, 
			new float[] {
				0.5f, 0.0f,
				0.0f, 1.0f,
				1.0f, 1.0f
			}, 
			new int[] {
				0, 1, 2
			}
		);
	}
	
	public void populate(
		float[] positions, float[] textureCoordinates, int[] indices
	) {
		this.positions = positions;
		this.textureCoordinates = textureCoordinates;
		this.indices = indices;
		this.vertexCount = this.positions.length / 3;
	}
	
	public int getVertexCount() {
		return this.vertexCount;
	}
	
	public float[] getPositions() {
		return this.positions;
	}
	
	public float[] getTextureCoordinates() {
		return this.textureCoordinates;
	}
	
	public int[] getIndices() {
		return this.indices;
	}
}

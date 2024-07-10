package project.asset;

public class Mesh {

	private float[] positions;
	private float[] textureCoordinates;
	private int[] indices;
	private int vertexCount;
	
	public Mesh() {
		this.positions = new float[] {
			0.0f, 0.5f, -1.0f,
			-0.5f, -0.5f, -1.0f,
			0.5f, -0.5f, -1.0f
		};
		
		this.textureCoordinates = new float[] {
			0.5f, 0.0f,
			0.0f, 1.0f,
			1.0f, 1.0f
		};
		
		this.indices = new int[] {
			0, 1, 2
		};
		
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

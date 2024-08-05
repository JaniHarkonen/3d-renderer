package project.asset.sceneasset;

public class VertexWeight {

	private int boneID;
	private int vertexID;
	private float weight;
	
	public VertexWeight(int boneID, int vertexID, float weight) {
		this.boneID = boneID;
		this.vertexID = vertexID;
		this.weight = weight;
	}
	
	
	public int getBoneID() {
		return this.boneID;
	}
	
	public int getVertexID() {
		return this.vertexID;
	}
	
	public float getWeight() {
		return this.weight;
	}
}

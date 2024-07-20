package project.asset;

public class AnimationMeshData {

	private float[] weights;
	private int[] boneIDs;
	
	public AnimationMeshData(float[] weights, int[] boneIDs) {
		this.weights = weights;
		this.boneIDs = boneIDs;
	}
	
	
	public float[] getBoneWeights() {
		return this.weights;
	}
	
	public int[] getBoneIDs() {
		return this.boneIDs;
	}
}

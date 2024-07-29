package project.asset;

import org.joml.Vector3f;

public class Mesh {
	
	/************************* Face-class **************************/
	
	public static class Face {
		public static final int INDICES_PER_FACE = 3;
		
		private int[] indices;
		
		public Face(int[] indices) {
			this.indices = indices;
		}
		
		
		public int getIndex(int index) {
			return this.indices[index];
		}
	}
	
	
	/************************* Mesh-class **************************/

	private Vector3f[] vertices;
	private Vector3f[] normals;
	private Vector3f[] tangents;
	private Vector3f[] bitangents;
	private Vector3f[] UVs; // Stored as a 3-vector for consistency, change this if necessary
	private Face[] faces;
	private AnimationMeshData animationMeshData;
	
	public Mesh() {
			// Default config, TO BE REMOVED
		this.populate(
				new Vector3f[0],
				new Vector3f[0],
				new Vector3f[0],
				new Vector3f[0],
				new Vector3f[0],
				new Face[0],
				null
		);
	}
	
	public void populate(
		Vector3f[] vertices,
		Vector3f[] normals,
		Vector3f[] tangents,
		Vector3f[] bitangents,
		Vector3f[] UVs,
		Face[] faces,
		AnimationMeshData animationMeshData
	) {
		this.vertices = vertices;
		this.normals = normals;
		this.tangents = tangents;
		this.bitangents = bitangents;
		this.UVs = UVs;
		this.faces = faces;
		this.animationMeshData = animationMeshData;
	}
	
	public int getVertexCount() {
		return this.vertices.length;
	}
	
	public Vector3f[] getVertices() {
		return this.vertices;
	}
	
	public Vector3f[] getNormals() {
		return this.normals;
	}
	
	public Vector3f[] getTangents() {
		return this.tangents;
	}
	
	public Vector3f[] getBitangents() {
		return this.bitangents;
	}
	
	public Vector3f[] getUVs() {
		return this.UVs;
	}
	
	public AnimationMeshData getAnimationMeshData() {
		return this.animationMeshData;
	}
	
	public Face[] getFaces() {
		return this.faces;
	}
}

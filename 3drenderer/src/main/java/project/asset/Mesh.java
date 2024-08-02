package project.asset;

import org.joml.Vector3f;

import project.Default;

public class Mesh implements IGraphicsAsset {
	
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
	
	
	/************************* Data-class **************************/
	
	public static class Data implements IAssetData {
		Mesh targetMesh;
		Vector3f[] vertices;
		Vector3f[] normals;
		Vector3f[] tangents;
		Vector3f[] bitangents;
		Vector3f[] UVs; // Stored as a 3-vector for consistency, change this if necessary
		Face[] faces;
		AnimationMeshData animationMeshData;

		@Override
		public void assign(long timestamp) {
			this.targetMesh.populate(
				this.vertices, 
				this.normals, 
				this.tangents, 
				this.bitangents, 
				this.UVs, 
				this.faces, 
				this.animationMeshData
			);
			this.targetMesh.lastUpdateTimestamp = timestamp;
		}
	}
	
	
	/************************* Mesh-class **************************/
	
	public static Mesh createMesh(
		String name,
		Vector3f[] vertices,
		Vector3f[] normals,
		Vector3f[] tangents,
		Vector3f[] bitangents,
		Vector3f[] UVs,
		Face[] faces,
		AnimationMeshData animationMeshData
	) {
		Mesh mesh = new Mesh(name, true);
		mesh.populate(
			vertices, normals, tangents, bitangents, UVs, faces, animationMeshData
		);
		return mesh;
	}

	private final String name;
	private long lastUpdateTimestamp;
	private IGraphics graphics;
	private Vector3f[] vertices;
	private Vector3f[] normals;
	private Vector3f[] tangents;
	private Vector3f[] bitangents;
	private Vector3f[] UVs; // Stored as a 3-vector for consistency, change this if necessary
	private Face[] faces;
	private AnimationMeshData animationMeshData;
	
	public Mesh(String name) {
		this(name, false);
	}
	
	private Mesh(String name, boolean isDefault) {
		this.name = name;
		this.lastUpdateTimestamp = -1;
		this.graphics = null;
		this.graphics = null;
		this.vertices = null;
		this.normals = null;
		this.tangents = null;
		this.bitangents = null;
		this.UVs = null;
		this.faces = null;
		this.animationMeshData = null;
		
		if( !isDefault ) {
			Mesh defaultMesh = Default.MESH;
			this.graphics = defaultMesh.graphics;
			this.populate(
				defaultMesh.vertices, 
				defaultMesh.normals, 
				defaultMesh.tangents, 
				defaultMesh.bitangents, 
				defaultMesh.UVs, 
				defaultMesh.faces, 
				defaultMesh.animationMeshData
			);
		}
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
	
	@Override
	public void setGraphics(IGraphics graphics) {
		this.graphics = graphics;
	}

	@Override
	public IGraphics getGraphics() {
		return this.graphics;
	}
	
	public String getName() {
		return this.name;
	}
	
	@Override
	public long getLastUpdateTimestamp() {
		return this.lastUpdateTimestamp;
	}
}

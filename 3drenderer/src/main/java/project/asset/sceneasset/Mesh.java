package project.asset.sceneasset;

import org.joml.Vector3f;

import project.Application;
import project.core.asset.IAssetData;
import project.core.asset.IGraphics;
import project.core.asset.IGraphicsAsset;

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
	
	public static final Mesh DEFAULT = new Mesh("mesh-default", true);
	static {
		DEFAULT.populate(new Vector3f[] {
				new Vector3f(0, 0, 0),
				new Vector3f(10, 0, 0),
				new Vector3f(0, 10, 0),
				new Vector3f(10, 10, 0),
			}, 
			new Vector3f[] {
				new Vector3f(0, 0, 0),
				new Vector3f(0, 0, 0),
				new Vector3f(0, 0, 0),
				new Vector3f(0, 0, 0),	
			},
			new Vector3f[] {
				new Vector3f(0, 0, 0),
				new Vector3f(0, 0, 0),
				new Vector3f(0, 0, 0),
				new Vector3f(0, 0, 0),	
			},
			new Vector3f[] {
				new Vector3f(0, 0, 0),
				new Vector3f(0, 0, 0),
				new Vector3f(0, 0, 0),
				new Vector3f(0, 0, 0),	
			},
			new Vector3f[] {
				new Vector3f(0, 0, 0),
				new Vector3f(1, 0, 0),
				new Vector3f(0, 1, 0),
				new Vector3f(1, 1, 0),
			}, 
			new Face[] {
				new Face(new int[] { 0, 1, 3 } ),
				new Face(new int[] { 0, 3, 2 } )
			},
			null
		);
	}
	
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
		Mesh mesh = new Mesh(name, false);
		mesh.populate(
			vertices, normals, tangents, bitangents, UVs, faces, animationMeshData
		);
		return mesh;
	}
	
	
	/************************* Class body **************************/

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
		this.vertices = null;
		this.normals = null;
		this.tangents = null;
		this.bitangents = null;
		this.UVs = null;
		this.faces = null;
		this.animationMeshData = null;
		
		if( !isDefault ) {
			Application.getApp().getRenderer().getDefaultMeshGraphics().createReference(this);
		} else {
			this.graphics = null;
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
	
	@Override
	public boolean deload() {
		if( this != DEFAULT ) {
			Application.getApp().getRenderer().assetDeloaded(this);
			
			this.vertices = null;
			this.normals = null;
			this.tangents = null;
			this.bitangents = null;
			this.UVs = null;
			this.faces = null;
			this.animationMeshData = null;
			return true;
		}
		return false;
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

package project.opengl;

import java.nio.IntBuffer;

import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryUtil;

import project.asset.IGraphics;
import project.asset.IGraphicsAsset;
import project.asset.Mesh;
import project.asset.SceneAssetLoadTask;

public class VAO implements IGraphics {

	private int vaoHandle;
	private VBO positionsVBO;
	private VBO normalsVBO;
	private VBO tangentsVBO;
	private VBO bitangentsVBO;
	private VBO textureCoordinatesVBO;
	private VBO boneWeightVBO;
	private VBO boneIndicesVBO;
	
	private int indicesVBO;
	
	private Mesh targetMesh;
	private int vertexCount;
	
	public VAO(Mesh targetMesh) {
		this.vaoHandle = -1;
		
		this.positionsVBO = null;
		this.normalsVBO = null;
		this.tangentsVBO = null;
		this.bitangentsVBO = null;
		this.textureCoordinatesVBO = null;
		this.boneWeightVBO = null;
		this.boneIndicesVBO = null;
		this.indicesVBO = -1;
		
		this.targetMesh = targetMesh;
		this.vertexCount = -1;
	}
	
	
	@Override
	public boolean generate() {
		this.vaoHandle = GL46.glGenVertexArrays();
		this.bind();
		
			//Mesh.Data meshData = this.targetMesh.getData();
			Mesh mesh = this.targetMesh;
			float[] boneWeights;
			int[] boneIndices;
			
			if( mesh.getAnimationMeshData() != null ) {
				boneWeights = mesh.getAnimationMeshData().getBoneWeights();
				boneIndices = mesh.getAnimationMeshData().getBoneIDs();
			} else {
				boneWeights = new float[SceneAssetLoadTask.MAX_WEIGHT_COUNT * mesh.getVertexCount()];
				boneIndices = new int[SceneAssetLoadTask.MAX_WEIGHT_COUNT * mesh.getVertexCount()];
			}
		
				// VBOs
			this.positionsVBO = new VBO(0, 3);
			this.positionsVBO.attach(mesh.getVertices());
			
			this.normalsVBO = new VBO(1, 3);
			this.normalsVBO.attach(mesh.getNormals());
			
			this.tangentsVBO = new VBO(2, 3);
			this.tangentsVBO.attach(mesh.getTangents());
			
			this.bitangentsVBO = new VBO(3, 3);
			this.bitangentsVBO.attach(mesh.getBitangents());
			
			this.textureCoordinatesVBO = new VBO(4, 2);
			this.textureCoordinatesVBO.attach(mesh.getUVs(), 2);
			
			this.boneWeightVBO = new VBO(5, 4);
			this.boneWeightVBO.attach(boneWeights);
			
			this.boneIndicesVBO = new VBO(6, 4);
			this.boneIndicesVBO.attach(boneIndices);
			
				// Indices
			int[] indices = new int[mesh.getFaces().length * Mesh.Face.INDICES_PER_FACE];
			Mesh.Face[] faces = mesh.getFaces();
			for( int i = 0; i < faces.length; i++ ) {
				Mesh.Face face = faces[i];
				int j = i * 3;
				indices[j] = face.getIndex(0);
				indices[j + 1] = face.getIndex(1);
				indices[j + 2] = face.getIndex(2);
			}
			this.indicesVBO = GL46.glGenBuffers();
			IntBuffer indicesBuffer = MemoryUtil.memAllocInt(indices.length);
			indicesBuffer.put(0, indices);
			GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, this.indicesVBO);
			GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL46.GL_STATIC_DRAW);
			
			GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, 0);
			MemoryUtil.memFree(indicesBuffer);
		
		this.unbind();
		this.vertexCount = mesh.getVertexCount();
		this.targetMesh.setGraphics(this);
		return true;
	}
	
	@Override
	public boolean dispose() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void bind() {
		GL46.glBindVertexArray(this.vaoHandle);
	}
	
	public void unbind() {
		GL46.glBindVertexArray(0);
	}
	
	public int getVertexCount() {
		return this.vertexCount;
	}
	
	public int getHandle() {
		return this.vaoHandle;
	}
	
	@Override
	public IGraphicsAsset getGraphicsAsset() {
		return this.targetMesh;
	}
	
	@Override
	public boolean isGenerated() {
		return (this.vaoHandle >= 0);
	}
}

package project.opengl;

import java.nio.IntBuffer;

import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryUtil;

import project.asset.Mesh;
import project.asset.SceneAssetLoadTask;

public class VAO {

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
	
	
	public void init() {
		this.vaoHandle = GL46.glGenVertexArrays();
		this.bind();
		
			float[] boneWeights;
			int[] boneIndices;
			
			if( this.targetMesh.getAnimationMeshData() != null ) {
				boneWeights = this.targetMesh.getAnimationMeshData().getBoneWeights();
				boneIndices = this.targetMesh.getAnimationMeshData().getBoneIDs();
			} else {
				boneWeights = new float[SceneAssetLoadTask.MAX_WEIGHT_COUNT * this.targetMesh.getVertexCount()];
				boneIndices = new int[SceneAssetLoadTask.MAX_WEIGHT_COUNT * this.targetMesh.getVertexCount()];
			}
		
				// VBOs
			this.positionsVBO = new VBO(0, 3);
			this.positionsVBO.attach(this.targetMesh.getVertices());
			
			this.normalsVBO = new VBO(1, 3);
			this.normalsVBO.attach(this.targetMesh.getNormals());
			
			this.tangentsVBO = new VBO(2, 3);
			this.tangentsVBO.attach(this.targetMesh.getTangents());
			
			this.bitangentsVBO = new VBO(3, 3);
			this.bitangentsVBO.attach(this.targetMesh.getBitangents());
			
			this.textureCoordinatesVBO = new VBO(4, 2);
			this.textureCoordinatesVBO.attach(this.targetMesh.getUVs(), 2);
			
			this.boneWeightVBO = new VBO(5, 4);
			this.boneWeightVBO.attach(boneWeights);
			
			this.boneIndicesVBO = new VBO(6, 4);
			this.boneIndicesVBO.attach(boneIndices);
			
				// Indices
			int[] indices = new int[this.targetMesh.getFaces().length * Mesh.Face.INDICES_PER_FACE];
			Mesh.Face[] faces = this.targetMesh.getFaces();
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
		this.vertexCount = this.targetMesh.getVertexCount();
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
}

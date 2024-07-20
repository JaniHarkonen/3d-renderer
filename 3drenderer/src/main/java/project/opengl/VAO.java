package project.opengl;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryUtil;

import project.asset.Mesh;

public class VAO {

	private int vaoHandle;
	private int positionsVBO;
	private int normalsVBO;
	private int tangentsVBO;
	private int bitangentsVBO;
	private int textureCoordinatesVBO;
	private int boneWeightVBO;
	private int boneIndicesVBO;
	private int indicesVBO;
	
	private Mesh targetMesh;
	private int vertexCount;
	
	public VAO(Mesh targetMesh) {
		this.vaoHandle = -1;
		this.positionsVBO = -1;
		this.normalsVBO = -1;
		this.tangentsVBO = -1;
		this.bitangentsVBO = -1;
		this.textureCoordinatesVBO = -1;
		this.boneWeightVBO = -1;
		this.boneIndicesVBO = -1;
		this.indicesVBO = -1;
		
		this.targetMesh = targetMesh;
		this.vertexCount = -1;
	}
	
	public void init() {
		this.vaoHandle = GL46.glGenVertexArrays();
		this.bind();
		
			float[] positions = this.targetMesh.getPositions();
			float[] normals = this.targetMesh.getNormals();
			float[] tangents = this.targetMesh.getTangents();
			float[] bitangents = this.targetMesh.getBitangents();
			float[] textureCoordinates = this.targetMesh.getTextureCoordinates();
			float[] boneWeights = this.targetMesh.getAnimationMeshData().getBoneWeights();
			int[] boneIndices = this.targetMesh.getAnimationMeshData().getBoneIDs();
			int[] indices = this.targetMesh.getIndices();
		
				// Positions
			this.positionsVBO = GL46.glGenBuffers();
			FloatBuffer positionsBuffer = MemoryUtil.memAllocFloat(positions.length);
			positionsBuffer.put(0, positions);
			GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, this.positionsVBO);
			GL46.glBufferData(GL46.GL_ARRAY_BUFFER, positionsBuffer, GL46.GL_STATIC_DRAW);
			GL46.glEnableVertexAttribArray(0);
			GL46.glVertexAttribPointer(0, 3, GL46.GL_FLOAT, false, 0, 0);
			
				// Normals
			this.normalsVBO = GL46.glGenBuffers();
			FloatBuffer normalsBuffer = MemoryUtil.memAllocFloat(normals.length);
			normalsBuffer.put(0, normals);
			GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, this.normalsVBO);
			GL46.glBufferData(GL46.GL_ARRAY_BUFFER, normalsBuffer, GL46.GL_STATIC_DRAW);
			GL46.glEnableVertexAttribArray(1);
			GL46.glVertexAttribPointer(1, 3, GL46.GL_FLOAT, false, 0, 0);
			
				// Tangents
			this.tangentsVBO = GL46.glGenBuffers();
			FloatBuffer tangentsBuffer = MemoryUtil.memAllocFloat(tangents.length);
			tangentsBuffer.put(0, tangents);
			GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, this.tangentsVBO);
			GL46.glBufferData(GL46.GL_ARRAY_BUFFER, tangentsBuffer, GL46.GL_STATIC_DRAW);
			GL46.glEnableVertexAttribArray(2);
			GL46.glVertexAttribPointer(2, 3, GL46.GL_FLOAT, false, 0, 0);
			
				// Bitangents
			this.bitangentsVBO = GL46.glGenBuffers();
			FloatBuffer bitangentsBuffer = MemoryUtil.memAllocFloat(bitangents.length);
			bitangentsBuffer.put(0, bitangents);
			GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, this.bitangentsVBO);
			GL46.glBufferData(GL46.GL_ARRAY_BUFFER, bitangentsBuffer, GL46.GL_STATIC_DRAW);
			GL46.glEnableVertexAttribArray(3);
			GL46.glVertexAttribPointer(3, 3, GL46.GL_FLOAT, false, 0, 0);
			
				// Texture coordinates
			this.textureCoordinatesVBO = GL46.glGenBuffers();
			FloatBuffer textureCoordinatesBuffer = MemoryUtil.memAllocFloat(textureCoordinates.length);
			textureCoordinatesBuffer.put(0, textureCoordinates);
			GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, this.textureCoordinatesVBO);
			GL46.glBufferData(GL46.GL_ARRAY_BUFFER, textureCoordinatesBuffer, GL46.GL_STATIC_DRAW);
			GL46.glEnableVertexAttribArray(4);
			GL46.glVertexAttribPointer(4, 2, GL46.GL_FLOAT, false, 0, 0);
			
				// Bone weights
			this.boneWeightVBO = GL46.glGenBuffers();
			FloatBuffer weightsBuffer = MemoryUtil.memAllocFloat(boneWeights.length);
			weightsBuffer.put(0, boneWeights).flip();
			GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, this.boneWeightVBO);
			GL46.glBufferData(GL46.GL_ARRAY_BUFFER, weightsBuffer, GL46.GL_STATIC_DRAW);
			GL46.glEnableVertexAttribArray(5);
			GL46.glVertexAttribPointer(5, 4, GL46.GL_FLOAT, false, 0, 0);
			
				// Bone indices
			this.boneIndicesVBO = GL46.glGenBuffers();
			IntBuffer boneIndicesBuffer = MemoryUtil.memAllocInt(boneIndices.length);
			boneIndicesBuffer.put(0, boneIndices).flip();
			GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, this.boneIndicesVBO);
			GL46.glBufferData(GL46.GL_ARRAY_BUFFER, boneIndicesBuffer, GL46.GL_STATIC_DRAW);
			GL46.glEnableVertexAttribArray(6);
			GL46.glVertexAttribPointer(6, 4, GL46.GL_FLOAT, false, 0, 0);
			
				// Indices
			this.indicesVBO = GL46.glGenBuffers();
			IntBuffer indicesBuffer = MemoryUtil.memAllocInt(indices.length);
			indicesBuffer.put(0, indices);
			GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, this.indicesVBO);
			GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL46.GL_STATIC_DRAW);	
			
			GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, 0);
		
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

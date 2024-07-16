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
	private int textureCoordinatesVBO;
	private int indicesVBO;
	
	private Mesh targetMesh;
	private int vertexCount;
	
	public VAO(Mesh targetMesh) {
		this.vaoHandle = -1;
		this.positionsVBO = -1;
		this.normalsVBO = -1;
		this.textureCoordinatesVBO = -1;
		this.indicesVBO = -1;
		
		this.targetMesh = targetMesh;
		this.vertexCount = -1;
	}
	
	public void init() {
		this.vaoHandle = GL46.glGenVertexArrays();
		this.bind();
		
			float[] positions = this.targetMesh.getPositions();
			float[] normals = this.targetMesh.getNormals();
			float[] textureCoordinates = this.targetMesh.getTextureCoordinates();
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
			
				// Texture coordinates
			this.textureCoordinatesVBO = GL46.glGenBuffers();
			FloatBuffer textureCoordinatesBuffer = MemoryUtil.memAllocFloat(textureCoordinates.length);
			textureCoordinatesBuffer.put(0, textureCoordinates);
			GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, this.textureCoordinatesVBO);
			GL46.glBufferData(GL46.GL_ARRAY_BUFFER, textureCoordinatesBuffer, GL46.GL_STATIC_DRAW);
			GL46.glEnableVertexAttribArray(2);
			GL46.glVertexAttribPointer(2, 2, GL46.GL_FLOAT, false, 0, 0);
			
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

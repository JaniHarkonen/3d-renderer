package project;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryUtil;

public class VAO {

	private int vaoHandle;
	private Vector3f position;
	private float[] positions;
	private float[] textureCoordinates;
	private int[] indices;
	private int positionsVBO;
	private int textureCoordinatesVBO;
	private int indicesVBO;
	
	public VAO(float x, float y, float z) {
		this.vaoHandle = -1;
		this.position = new Vector3f(x, y, z);
		
		this.positions = new float[] {
			this.position.x + 0.0f, this.position.y + 0.5f, this.position.z + 0.0f,
			this.position.x - 0.5f, this.position.y - 0.5f, this.position.z + 0.0f,
			this.position.x + 0.5f, this.position.y - 0.5f, this.position.z + 0.0f
		};
		
		this.textureCoordinates = new float[] {
			0.5f, 0.0f,
			0.0f, 1.0f,
			1.0f, 1.0f
		};
		
		this.indices = new int[] {
			0, 1, 2
		};
		
		this.positionsVBO = -1;
		this.textureCoordinatesVBO = -1;
		this.indicesVBO = -1;
	}
	
	public void init() {
		this.vaoHandle = GL46.glGenVertexArrays();
		this.bind();
		
				// Positions
			this.positionsVBO = GL46.glGenBuffers();
			FloatBuffer positionsBuffer = MemoryUtil.memAllocFloat(this.positions.length);
			positionsBuffer.put(0, this.positions);
			GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, this.positionsVBO);
			GL46.glBufferData(GL46.GL_ARRAY_BUFFER, positionsBuffer, GL46.GL_STATIC_DRAW);
			GL46.glEnableVertexAttribArray(0);
			GL46.glVertexAttribPointer(0, 3, GL46.GL_FLOAT, false, 0, 0);
			
			this.textureCoordinatesVBO = GL46.glGenBuffers();
			FloatBuffer textureCoordinatesBuffer = MemoryUtil.memAllocFloat(this.textureCoordinates.length);
			textureCoordinatesBuffer.put(0, this.textureCoordinates);
			GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, this.textureCoordinatesVBO);
			GL46.glBufferData(GL46.GL_ARRAY_BUFFER, textureCoordinatesBuffer, GL46.GL_STATIC_DRAW);
			GL46.glEnableVertexAttribArray(1);
			GL46.glVertexAttribPointer(1, 2, GL46.GL_FLOAT, false, 0, 0);
			
			this.indicesVBO = GL46.glGenBuffers();
			IntBuffer indicesBuffer = MemoryUtil.memAllocInt(this.indices.length);
			indicesBuffer.put(0, this.indices);
			GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, this.indicesVBO);
			GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL46.GL_STATIC_DRAW);
			
			GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, 0);
		
		this.unbind();
	}
	
	public void bind() {
		GL46.glBindVertexArray(this.vaoHandle);
	}
	
	public void unbind() {
		GL46.glBindVertexArray(0);
	}
	
	public int getVertexCount() {
		return this.positions.length / 3;
	}
	
	public int getHandle() {
		return this.vaoHandle;
	}
	
	public Vector3f getPosition() {
		return this.position;
	}
}

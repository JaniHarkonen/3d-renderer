package project;

import java.nio.FloatBuffer;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryUtil;

public class VAO {

	private int vaoHandle;
	private Vector3f position;
	private float[] positions;
	private int positionsVBO;
	
	public VAO(float x, float y, float z) {
		this.vaoHandle = -1;
		this.position = new Vector3f(x, y, z);
		this.positions= new float[] {
			0.0f, 0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f
		};
		this.positionsVBO = -1;
	}
	
	public void init() {
		this.vaoHandle = GL46.glGenVertexArrays();
		this.bind();
		
			this.positionsVBO = GL46.glGenBuffers();
			FloatBuffer positionsBuffer = MemoryUtil.memAllocFloat(this.positions.length);
			positionsBuffer.put(0, this.positions);
			GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, this.positionsVBO);
			GL46.glBufferData(GL46.GL_ARRAY_BUFFER, positionsBuffer, GL46.GL_STATIC_DRAW);
			GL46.glEnableVertexAttribArray(0);
			GL46.glVertexAttribPointer(0, 3, GL46.GL_FLOAT, false, 0, 0);
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
}

package project.opengl;

import org.lwjgl.opengl.GL46;

public class VBO {
	private int vboHandle;
	private int attributeArrayIndex;
	private int size;
	
	public VBO(int attributeArrayIndex, int size) {
		this.vboHandle = -1;
		this.attributeArrayIndex = attributeArrayIndex;
		this.size = size;
	}
	
	
	private void prepareVBO() {
		this.vboHandle = GL46.glGenBuffers();
		GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, this.vboHandle);
	}
	
	private void finalizeVBO() {
		GL46.glEnableVertexAttribArray(this.attributeArrayIndex);
		GL46.glVertexAttribPointer(this.attributeArrayIndex, this.size, GL46.GL_FLOAT, false, 0, 0);
	}
	
	public void attach(float[] bufferArray) {
		this.prepareVBO();
		GL46.glBufferData(GL46.GL_ARRAY_BUFFER, bufferArray, GL46.GL_STATIC_DRAW);
		this.finalizeVBO();
	}
	
	public void attach(int[] bufferArray) {
		this.prepareVBO();
		GL46.glBufferData(GL46.GL_ARRAY_BUFFER, bufferArray, GL46.GL_STATIC_DRAW);
		this.finalizeVBO();
	}
	
	public void dispose() {
		GL46.glDeleteBuffers(this.vboHandle);
	}
}

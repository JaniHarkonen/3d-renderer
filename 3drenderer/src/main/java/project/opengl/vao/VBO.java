package project.opengl.vao;

import org.lwjgl.opengl.GL46;

public class VBO {
	private int vboHandle;
	private int attributeArrayIndex;
	private int size;
	private int usage;
	
	public VBO(int size, int usage) {
		this.vboHandle = -1;
		this.attributeArrayIndex = -1;
		this.size = size;
		this.usage = usage;
	}
	
	public VBO(int size) {
		this(size, GL46.GL_STATIC_DRAW);
	}
	
	
	protected void generate() {
		this.vboHandle = GL46.glGenBuffers();
		GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, this.vboHandle);
	}
	
	protected void enable() {
		GL46.glEnableVertexAttribArray(this.attributeArrayIndex);
		GL46.glVertexAttribPointer(this.attributeArrayIndex, this.size, GL46.GL_FLOAT, false, 0, 0);
	}
	
	/*public void attach(Vector3f[] bufferArray) {
		this.attach(bufferArray, this.size);
	}
	
	public void attach(Vector3f[] bufferArray, int dimensions) {
		float[] finalBufferArray = new float[bufferArray.length * dimensions];
		int index = 0;
		
		for( Vector3f vector : bufferArray ) {
			for( int i = 0; i < dimensions; i++ ) {
				finalBufferArray[index++] = vector.get(i);
			}
		}
		
		this.prepareVBO();
		GL46.glBufferData(GL46.GL_ARRAY_BUFFER, finalBufferArray, GL46.GL_STATIC_DRAW);
		this.finalizeVBO();
	}*/
	
	public VBO attach(float[] bufferArray) {
		this.generate();
		GL46.glBufferData(GL46.GL_ARRAY_BUFFER, bufferArray, this.usage);
		this.enable();
		return this;
	}
	
	public VBO attach(int[] bufferArray) {
		this.generate();
		GL46.glBufferData(GL46.GL_ARRAY_BUFFER, bufferArray, this.usage);
		this.enable();
		return this;
	}
	
	public void dispose() {
		GL46.glDeleteBuffers(this.vboHandle);
	}
	
	void setAttributeArrayIndex(int index) {
		this.attributeArrayIndex = index;
	}
}

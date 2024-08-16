package project.opengl.vao;

import org.lwjgl.opengl.GL46;

public abstract class AVBO<T> {
	private int vboHandle;
	private int attributeArrayIndex;
	private int size;
	private int usage;
	
	public AVBO(int size, int usage) {
		this.vboHandle = -1;
		this.attributeArrayIndex = -1;
		this.size = size;
		this.usage = usage;
	}
	
	public AVBO(int size) {
		this(size, GL46.GL_STATIC_DRAW);
	}
	
	
	public AVBO<T> attach(T[] data) {
		this.generate();
		this.populate(data);
		this.enable();
		
		return this;
	}
	
	protected void generate() {
		this.vboHandle = GL46.glGenBuffers();
		GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, this.vboHandle);
	}
	
	protected abstract void populate(T[] data);
	
	protected void enable() {
		GL46.glEnableVertexAttribArray(this.attributeArrayIndex);
		GL46.glVertexAttribPointer(this.attributeArrayIndex, this.size, GL46.GL_FLOAT, false, 0, 0);
	}
	
	protected void populate(float[] array) {
		GL46.glBufferData(GL46.GL_ARRAY_BUFFER, array, this.usage);
	}
	
	protected void populate(int[] array) {
		GL46.glBufferData(GL46.GL_ARRAY_BUFFER, array, this.usage);
	}
	
	/*private void prepareVBO() {
		this.vboHandle = GL46.glGenBuffers();
		GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, this.vboHandle);
	}
	
	private void finalizeVBO() {
		GL46.glEnableVertexAttribArray(this.attributeArrayIndex);
		GL46.glVertexAttribPointer(this.attributeArrayIndex, this.size, GL46.GL_FLOAT, false, 0, 0);
	}
	
	public void attach(Vector3f[] bufferArray) {
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
	}*/
	
	public void dispose() {
		GL46.glDeleteBuffers(this.vboHandle);
	}
	
	void setAttributeArrayIndex(int index) {
		this.attributeArrayIndex = index;
	}
}

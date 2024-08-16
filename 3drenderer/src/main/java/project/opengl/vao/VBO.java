package project.opengl.vao;

import org.lwjgl.opengl.GL46;

public class VBO {
	private int vboHandle;
	private int attributeArrayIndex;
	private int size;
	private int usage;
	private int type;
	private int target;
	
	public VBO(int size, int type, int target, int usage) {
		this.vboHandle = -1;
		this.attributeArrayIndex = -1;
		this.size = size;
		this.usage = usage;
		this.type = type;
		this.target = target;
	}
	
	public VBO(int size) {
		this(size, GL46.GL_FLOAT, GL46.GL_ARRAY_BUFFER, GL46.GL_STATIC_DRAW);
	}
	
	
	public void attach() {
		GL46.glEnableVertexAttribArray(this.attributeArrayIndex);
		GL46.glVertexAttribPointer(this.attributeArrayIndex, this.size, this.type, false, 0, 0);
	}
	
	public VAO attachTo(VAO vao) {
		vao.bind();
		this.attach();
		return vao;
	}
	
	protected void create() {
		this.vboHandle = GL46.glGenBuffers();
	}
	
	public VBO generate(float[] bufferArray) {
		this.create();
		this.bind();
		GL46.glBufferData(this.target, bufferArray, this.usage);
		return this;
	}
	
	public VBO generate(int[] bufferArray) {
		this.create();
		this.bind();
		GL46.glBufferData(this.target, bufferArray, this.usage);
		return this;
	}
	
	public VAO generateAndAttach(VAO vao, float[] bufferArray) {
		this.generate(bufferArray);
		this.attachTo(vao);
		return vao;
	}
	
	public VAO generateAndAttach(VAO vao, int[] bufferArray) {
		this.generate(bufferArray);
		this.attachTo(vao);
		return vao;
	}
	
	public void bind() {
		GL46.glBindBuffer(this.target, this.vboHandle);
	}
	
	public void unbind() {
		GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, 0);
	}
	
	public void dispose() {
		GL46.glDeleteBuffers(this.vboHandle);
	}
	
	void setAttributeArrayIndex(int index) {
		this.attributeArrayIndex = index;
	}
	
	public int getHandle() {
		return this.vboHandle;
	}
	
	public int getAttributeArrayIndex() {
		return this.attributeArrayIndex;
	}
	
	public int getSize() {
		return this.size;
	}
	
	public int getUsage() {
		return this.usage;
	}
	
	public int getType() {
		return this.type;
	}
	
	public int getTarget() {
		return this.target;
	}
}

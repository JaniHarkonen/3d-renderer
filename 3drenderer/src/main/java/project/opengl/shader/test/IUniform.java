package project.opengl.shader.test;

import project.opengl.shader.ShaderProgram;

public interface IUniform<T> {

	public void initialize(ShaderProgram shaderProgram);
	
	public void update(T value);
	
	public void setName(String name);
	
	public String getName();
}

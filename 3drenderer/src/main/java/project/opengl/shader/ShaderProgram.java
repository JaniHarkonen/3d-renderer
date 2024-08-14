package project.opengl.shader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL46;

import project.opengl.shader.uniform.IUniform;
import project.utils.DebugUtils;

public class ShaderProgram {

	private int programHandle;
	private Map<String, IUniform<?>> uniforms;
	private List<Shader> shaders;

	public ShaderProgram() {
		this.programHandle = -1;
		this.uniforms = new HashMap<>();
		this.shaders = new ArrayList<>();
	}
	
	public void initialize() {
		this.programHandle = GL46.glCreateProgram();
		
		for( Shader shader : this.shaders ) {
			shader.initialize();
			shader.attach(this);
		}
		
		GL46.glLinkProgram(this.programHandle);	
		
		if( GL46.glGetProgrami(this.programHandle, GL46.GL_LINK_STATUS) != GL46.GL_TRUE ) {
			DebugUtils.log(this, "ERROR: Unable to link shader program!");
			return;
		}
		
		for( Shader shader : this.shaders ) {
			shader.detach(this);
		}
		
		for( Map.Entry<String, IUniform<?>> en : this.uniforms.entrySet() ) {
			en.getValue().initialize(this);
		}
	}
	
	public ShaderProgram declareUniform(IUniform<?> uniform) {
		this.uniforms.put(uniform.getName(), uniform);
		return this;
	}
	
	public void bind() {
		GL46.glUseProgram(this.programHandle);
	}
	
	public void unbind() {
		GL46.glUseProgram(0);
	}
	
	public void addShader(Shader shader) {
		this.shaders.add(shader);
	}
	
	public int getHandle() {
		return this.programHandle;
	}
	
	public IUniform<?> getUniform(String name) {
		return this.uniforms.get(name);
	}
}

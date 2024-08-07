package project.opengl.shader;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryStack;

import project.opengl.shader.uniform.IUniform;
import project.utils.DebugUtils;

public class ShaderProgram {

	private int programHandle;
	private Map<String, Integer> uniformLocationMap;
	private Map<String, IUniform<?>> uniforms;
	private List<Shader> shaders;

	public ShaderProgram() {
		this.programHandle = -1;
		this.uniformLocationMap = new HashMap<>();
		this.uniforms = new HashMap<>();
		this.shaders = new ArrayList<>();
	}
	
	public void init() {
		this.programHandle = GL46.glCreateProgram();
		
		for( Shader shader : this.shaders ) {
			shader.init();
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
		
		for( Map.Entry<String, Integer> en : this.uniformLocationMap.entrySet() ) {
			String key = en.getKey();
			this.uniformLocationMap.put(
				key, GL46.glGetUniformLocation(this.programHandle, key)
			);
		}
		
		for( Map.Entry<String, IUniform<?>> en : this.uniforms.entrySet() ) {
			en.getValue().initialize(this);
		}
	}
	
	public ShaderProgram declareUniform(IUniform<?> uniform) {
		this.uniforms.put(uniform.getName(), uniform);
		return this;
	}
	
	public ShaderProgram declareUniform(String uniformName) {
		final char START = 'u';
		if( uniformName.charAt(0) != START ) {
			DebugUtils.log(
				this, 
				"ERROR: Trying to declare a uniform with an invalid name! Name: '" + 
				uniformName + "'"
			);
			
			throw new RuntimeException(
				"FATAL ERROR: Trying to create a uniform whose name doesn't start " +
				"with '" + START + "'!"
			);
		}
		
		this.uniformLocationMap.put(uniformName, -1);
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
	
	private Integer getUniformOrError(String name) {
		Integer uniformLocation = this.uniformLocationMap.get(name);
		
		if( uniformLocation == null ) {
			throw new RuntimeException(
				"ERROR: Trying to get a non-existent uniform '" + name + "'!"
			);
		}
		
		return uniformLocation;
	}
	
	public void setInteger1Uniform(String name, int i1) {
		GL46.glUniform1i(this.getUniformOrError(name), i1);
	}
	
	public void setFloat1Uniform(String name, float f1) {
		GL46.glUniform1f(this.getUniformOrError(name), f1);
	}
	
	public void setVector3fUniform(String name, Vector3f vec3f) {
		GL46.glUniform3f(this.getUniformOrError(name), vec3f.x, vec3f.y, vec3f.z);
	}
	
	public void setVector4fUniform(String name, Vector4f vec4f) {
		GL46.glUniform4f(
			this.getUniformOrError(name), vec4f.x, vec4f.y, vec4f.z, vec4f.w
		);
	}
	
	public void setMatrix4fUniform(String name, Matrix4f mat4f) {
		try( MemoryStack stack = MemoryStack.stackPush() ) {
			GL46.glUniformMatrix4fv(
				this.getUniformOrError(name), 
				false, 
				mat4f.get(stack.mallocFloat(16))
			);
		}
	}
	
	public void setMatrix4fArrayUniform(String name, Matrix4f[] mat4fArray) {
		try( MemoryStack stack = MemoryStack.stackPush() ) {
			
			int length = (mat4fArray != null) ? mat4fArray.length : 0;
			FloatBuffer arrayBuffer = stack.mallocFloat(16 * length);
			for( int i = 0; i < length; i++ ) {
				mat4fArray[i].get(16 * i, arrayBuffer);
			}
			
			GL46.glUniformMatrix4fv(this.getUniformOrError(name), false, arrayBuffer);
		}
	}
	
	public int getHandle() {
		return this.programHandle;
	}
	
	public IUniform<?> getUniform(String name) {
		return this.uniforms.get(name);
	}
}

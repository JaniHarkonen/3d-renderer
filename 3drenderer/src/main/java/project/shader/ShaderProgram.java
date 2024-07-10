package project.shader;

import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryStack;

import project.utils.DebugUtils;

public class ShaderProgram {

	private int programHandle;
	//private int uniformDiffuseSamplerLocation;
	//private int uniformProjectionLocation;
	//private int uniformObjectTransformLocation;
	private Map<String, Integer> uniformLocationMap;

	public ShaderProgram() {
		this.programHandle = -1;
		//this.uniformDiffuseSamplerLocation = -1;
		//this.uniformProjectionLocation = -1;
		//this.uniformObjectTransformLocation = -1;
		this.uniformLocationMap = new HashMap<>();
	}
	
	public void init() {
		this.programHandle = GL46.glCreateProgram();
		
		Shader vertexShader = new Shader("default.vert", GL46.GL_VERTEX_SHADER);
		Shader fragmentShader = new Shader("default.frag", GL46.GL_FRAGMENT_SHADER);
		
		vertexShader.init();
		vertexShader.attach(this);
		
		fragmentShader.init();
		fragmentShader.attach(this);
		
		GL46.glLinkProgram(this.programHandle);	
		
		if( GL46.glGetProgrami(this.programHandle, GL46.GL_LINK_STATUS) != GL46.GL_TRUE ) {
			DebugUtils.log(this, "ERROR: Unable to link shader program!");
			return;
		}
		
		vertexShader.detach(this);
		fragmentShader.detach(this);
		
		this.createUniform("uDiffuseSampler");
		this.createUniform("uProjection");
		this.createUniform("uCameraTransform");
		this.createUniform("uObjectTransform");
		
		/*this.uniformDiffuseSamplerLocation = GL46.glGetUniformLocation(
			this.programHandle, "uDiffuseSampler"
		);
		
		this.uniformProjectionLocation = GL46.glGetUniformLocation(
			this.programHandle, "uProjection"
		);
		
		this.uniformObjectTransformLocation = GL46.glGetUniformLocation(
			this.programHandle, "uObjectTransform"
		);*/
	}
	
	private void createUniform(String name) {
		final char START = 'u';
		if( name.charAt(0) != START ) {
			DebugUtils.log(this, "ERROR: Trying to declare a uniform with an invalid name! Name: '" + name + "'");
			throw new RuntimeException("FATAL ERROR: Trying to create a uniform whose name doesn't start with '" + START + "'!");
		}
		
		int uniformLocation = GL46.glGetUniformLocation(this.programHandle, name);
		this.uniformLocationMap.put(name, uniformLocation);
	}
	
	public void bind() {
		GL46.glUseProgram(this.programHandle);
	}
	
	public void unbind() {
		GL46.glUseProgram(0);
	}
	
	private Integer getUniformOrError(String name) {
		Integer uniformLocation = this.uniformLocationMap.get(name);
		
		if( uniformLocation == null ) {
			throw new RuntimeException("ERROR: Trying to get a non-existent uniform '" + name + "'!");
		}
		
		return uniformLocation;
	}
	
	public void setInteger1Uniform(String name, int i1) {
		GL46.glUniform1i(this.getUniformOrError(name), i1);
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
	
	public void setDiffuseSamplerUniform(int sampler) {
		//GL46.glUniform1i(this.uniformDiffuseSamplerLocation, 0);
		this.setInteger1Uniform("uDiffuseSampler", sampler);
	}
	
	public void setProjectionUniform(Matrix4f projectionMatrix) {
		/*try( MemoryStack stack = MemoryStack.stackPush() ) {
			GL46.glUniformMatrix4fv(
				this.uniformProjectionLocation, 
				false, 
				projectionMatrix.get(stack.mallocFloat(16))
			);
		}*/
		this.setMatrix4fUniform("uProjection", projectionMatrix);
	}
	
	public void setObjectTransformUniform(Matrix4f transformMatrix) {
		/*try( MemoryStack stack = MemoryStack.stackPush() ) {
			GL46.glUniformMatrix4fv(
				this.uniformObjectTransformLocation, 
				false, 
				transformMatrix.get(stack.mallocFloat(16))
			);
		}*/
		this.setMatrix4fUniform("uObjectTransform", transformMatrix);
	}
	
	public void setCameraTransformUniform(Matrix4f cameraTransformMatrix) {
		this.setMatrix4fUniform("uCameraTransform", cameraTransformMatrix);
	}
	
	public int getHandle() {
		return this.programHandle;
	}
}

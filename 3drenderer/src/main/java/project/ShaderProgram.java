package project;

import org.lwjgl.opengl.GL46;

public class ShaderProgram {

	private int programHandle;
	private int uniformDiffuseSamplerLocation;

	public ShaderProgram() {
		this.programHandle = -1;
		this.uniformDiffuseSamplerLocation = -1;
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
			System.out.println("ERROR: Unable to link shader program!");
			return;
		}
		
		vertexShader.detach(this);
		fragmentShader.detach(this);
		
		this.uniformDiffuseSamplerLocation = GL46.glGetUniformLocation(
			this.programHandle, "diffuseSampler"
		);
	}
	
	public void bind() {
		GL46.glUseProgram(this.programHandle);
		GL46.glUniform1i(this.uniformDiffuseSamplerLocation, 0);
	}
	
	public void unbind() {
		GL46.glUseProgram(0);
	}
	
	public int getHandle() {
		return this.programHandle;
	}
}

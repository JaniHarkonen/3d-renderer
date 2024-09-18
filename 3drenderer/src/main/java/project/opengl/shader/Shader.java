package project.opengl.shader;

import org.lwjgl.opengl.GL46;

import project.shared.logger.Logger;
import project.utils.FileUtils;

public class Shader {

	private int handle;
	private String sourcePath;
	private int type;
	
	public Shader(String sourcePath, int type) {
		this.handle = -1;
		this.sourcePath = sourcePath;
		this.type = type;
	}
	
	public void initialize() {
		this.handle = GL46.glCreateShader(this.type);
		String sourceCode = FileUtils.readTextFile(FileUtils.getResourcePath(this.sourcePath));
		GL46.glShaderSource(this.handle, sourceCode);
		GL46.glCompileShader(this.handle);
		
		if( GL46.glGetShaderi(this.handle, GL46.GL_COMPILE_STATUS) != GL46.GL_TRUE ) {
			Logger.get().error(this, 
				"Unable to compile shader. Reason:",
				GL46.glGetShaderInfoLog(this.handle)
			);
		}
	}
	
	public void attach(ShaderProgram program) {
		GL46.glAttachShader(program.getHandle(), this.handle);
	}
	
	public void detach(ShaderProgram program) {
		GL46.glDetachShader(program.getHandle(), this.handle);
	}
}

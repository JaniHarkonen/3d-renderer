package project.opengl;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL46;

import project.Window;
import project.scene.Camera;
import project.scene.Scene;
import project.shader.ShaderProgram;

public class Renderer {
	
	private Window clientWindow;
	private ShaderProgram shaderProgram;
	private Scene scene;
	
	public Renderer(Window clientWindow, Scene scene) {
		this.clientWindow = clientWindow;
		this.shaderProgram = null;
		this.scene = scene;
	}

	public void init() {
		GL.createCapabilities();
		GL46.glClearColor(0.85f, 0.85f, 0.85f, 0.0f);
		
			// Shaders
		this.shaderProgram = new ShaderProgram();
		this.shaderProgram.init();
		
			// Initialize scene graphics assets
		this.scene.init();
		
		for( VAO vao : this.scene.getObjects() ) {
			vao.init();
		}
		
		this.scene.getTexture().init();
	}
		
	public void render() {
		GL46.glClear(GL46.GL_COLOR_BUFFER_BIT | GL46.GL_DEPTH_BUFFER_BIT);
		this.shaderProgram.bind();
		this.shaderProgram.setDiffuseSamplerUniform(0);
		
		Camera activeCamera = this.scene.getCamera();
		
		activeCamera.getProjection().update(
			this.clientWindow.getWidth(), this.clientWindow.getHeight()
		);
		
		this.shaderProgram.setProjectionUniform(activeCamera.getProjection().getMatrix());
			
				// Width and height are not yet updated when resizing the window
			GL46.glViewport(
				0, 0, this.clientWindow.getWidth(), this.clientWindow.getHeight()
			);
			
			GL46.glActiveTexture(GL46.GL_TEXTURE0);
			this.scene.getTexture().bind();
			
			for( VAO vao : this.scene.getObjects() ) {
				vao.bind();
				GL46.glDrawArrays(GL46.GL_TRIANGLES, 0, vao.getVertexCount());
			}
		
		this.shaderProgram.unbind();
	}
}

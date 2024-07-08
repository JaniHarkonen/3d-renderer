package project;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL46;

import project.shader.ShaderProgram;
import project.utils.FileUtils;

public class Renderer {
	
	private Window clientWindow;
	private ShaderProgram shaderProgram;
	private List<VAO> scene;
	private Texture texture;
	
	public Renderer(Window clientWindow) {
		this.clientWindow = clientWindow;
		this.shaderProgram = null;
		this.scene = null;
		this.texture = null;
	}

	public void init() {
		GL.createCapabilities();
		GL46.glClearColor(0.85f, 0.85f, 0.85f, 0.0f);
		
		this.shaderProgram = new ShaderProgram();
		this.shaderProgram.init();
		
		this.scene = new ArrayList<>();
		VAO vao = new VAO(0, 0, 0);
		vao.init();
		this.scene.add(vao);
		
		this.texture = new Texture(FileUtils.getResourcePath("creep.png"));
		this.texture.init();
	}
		
	public void render() {
		GL46.glClear(GL46.GL_COLOR_BUFFER_BIT | GL46.GL_DEPTH_BUFFER_BIT);
		this.shaderProgram.bind();
			
				// Width and height are not yet updated when resizing the window
			GL46.glViewport(
				0, 0, this.clientWindow.getWidth(), this.clientWindow.getHeight()
			);
			
			GL46.glActiveTexture(GL46.GL_TEXTURE0);
			this.texture.bind();
			
			for( VAO vao : this.scene ) {
				vao.bind();
				GL46.glDrawArrays(GL46.GL_TRIANGLES, 0, vao.getVertexCount());
			}
		
		this.shaderProgram.unbind();
	}
}

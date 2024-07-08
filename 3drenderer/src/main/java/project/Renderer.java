package project;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL46;

public class Renderer {
	
	private Window clientWindow;
	private ShaderProgram shaderProgram;
	private List<VAO> scene;
	
	public Renderer(Window clientWindow) {
		this.clientWindow = clientWindow;
		this.shaderProgram = null;
		this.scene = null;
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
	}
	
	public void render() {
		GL46.glClear(GL46.GL_COLOR_BUFFER_BIT | GL46.GL_DEPTH_BUFFER_BIT);
		this.shaderProgram.bind();
			
			GL46.glViewport(0, 0, this.clientWindow.getWidth(), this.clientWindow.getHeight());
			
			for( VAO vao : this.scene ) {
				vao.bind();
				GL46.glDrawArrays(GL46.GL_TRIANGLES, 0, vao.getVertexCount());
			}
		
		this.shaderProgram.unbind();
	}
}

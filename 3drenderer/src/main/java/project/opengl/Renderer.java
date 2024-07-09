package project.opengl;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL46;

import project.Window;
import project.geometry.Projection;
import project.scene.Camera;
import project.shader.ShaderProgram;
import project.utils.FileUtils;

public class Renderer {
	
	private Window clientWindow;
	private ShaderProgram shaderProgram;
	private List<VAO> scene;
	private Texture texture;
	private Camera camera;
	
	public Renderer(Window clientWindow) {
		this.clientWindow = clientWindow;
		this.shaderProgram = null;
		this.scene = null;
		this.texture = null;
		this.camera = null;
	}

	public void init() {
		GL.createCapabilities();
		GL46.glClearColor(0.85f, 0.85f, 0.85f, 0.0f);
		
			// Shaders
		this.shaderProgram = new ShaderProgram();
		this.shaderProgram.init();
		
			// VAOs
		this.scene = new ArrayList<>();
		VAO vao = new VAO(0, 0, -1);
		vao.init();
		this.scene.add(vao);
		
			// Textures
		this.texture = new Texture(FileUtils.getResourcePath("creep.png"));
		this.texture.init();

			// Camera
		this.camera = new Camera(new Projection(60.0f, 0.01f, 1000.0f));
	}
		
	public void render() {
		GL46.glClear(GL46.GL_COLOR_BUFFER_BIT | GL46.GL_DEPTH_BUFFER_BIT);
		this.shaderProgram.bind();
		this.shaderProgram.setDiffuseSamplerUniform(0);
		this.camera.getProjection().update(this.clientWindow.getWidth(), this.clientWindow.getHeight());
		this.shaderProgram.setProjectionUniform(this.camera.getProjection().getMatrix());
			
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

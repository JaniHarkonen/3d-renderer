package project.opengl;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL46;

import project.Window;
import project.asset.Mesh;
import project.scene.Camera;
import project.scene.Model;
import project.scene.Scene;
import project.scene.SceneObject;
import project.shader.ShaderProgram;

public class Renderer {
	
	private Window clientWindow;
	private ShaderProgram shaderProgram;
	private VAOCache vaoCache;
	private TextureCache textureCache;
	private Scene scene;
	
	public Renderer(Window clientWindow, Scene scene) {
		this.clientWindow = clientWindow;
		this.shaderProgram = null;
		this.vaoCache = null;
		this.textureCache = null;
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
		this.vaoCache = new VAOCache();
		this.textureCache = new TextureCache();
	}
		
	public void render() {
		GL46.glClear(GL46.GL_COLOR_BUFFER_BIT | GL46.GL_DEPTH_BUFFER_BIT);
		this.shaderProgram.bind();
		this.shaderProgram.setDiffuseSamplerUniform(0);
		
		Camera activeCamera = this.scene.getActiveCamera();
		activeCamera.getProjection().update(
			this.clientWindow.getWidth(), this.clientWindow.getHeight()
		);
		
		this.shaderProgram.setProjectionUniform(activeCamera.getProjection().getMatrix());
		this.shaderProgram.setCameraTransformUniform(activeCamera.getCameraTransform());
			
				// Width and height are not yet updated when resizing the window
			GL46.glViewport(
				0, 0, this.clientWindow.getWidth(), this.clientWindow.getHeight()
			);
			
			for( SceneObject object : this.scene.getObjects() ) {
				
					// Determine the appropriate way of rendering the object
					// (THIS MUST BE CHANGED TO A MORE DYNAMIC APPROACH)
				if( object instanceof Model ) {
					this.shaderProgram.setObjectTransformUniform(object.getTransformMatrix());
					
					Model model = (Model) object;
					Texture texture = model.getTexture();
					Mesh mesh = model.getMesh();
					
					this.textureCache.generateIfNotEncountered(texture);
					GL46.glActiveTexture(GL46.GL_TEXTURE0);
					texture.bind();
					
					VAO vao = this.vaoCache.getOrGenerate(mesh);
					vao.bind();
					GL46.glDrawArrays(GL46.GL_TRIANGLES, 0, vao.getVertexCount());
				}
			}
		
		this.shaderProgram.unbind();
	}
}

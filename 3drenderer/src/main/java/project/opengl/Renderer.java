package project.opengl;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL46;

import project.Window;
import project.pass.CascadeShadowRenderPass;
import project.pass.GUIRenderPass;
import project.pass.SceneRenderPass;
import project.scene.Camera;
import project.scene.Scene;

public class Renderer {
	
	private Window clientWindow;
	
	private CascadeShadowRenderPass cascadeRenderPass;
	private SceneRenderPass sceneRenderPass;
	private GUIRenderPass guiRenderPass;
	
	private VAOCache vaoCache;
	private TextureCache textureCache;
	private Scene scene;
	
	public Renderer(Window clientWindow, Scene scene) {
		this.clientWindow = clientWindow;
		this.vaoCache = null;
		this.textureCache = null;
		this.scene = scene;
		this.cascadeRenderPass = new CascadeShadowRenderPass();
		this.sceneRenderPass = new SceneRenderPass();
		this.guiRenderPass = new GUIRenderPass();
	}

	public void init() {
		GL.createCapabilities();
		GL46.glClearColor(0.643f, 0.62f, 0.557f, 1.0f);
		
		this.cascadeRenderPass.init();
		this.sceneRenderPass.init();
		this.guiRenderPass.init();
		
			// Initialize scene graphics assets
		this.scene.init();
		this.vaoCache = new VAOCache();
		this.textureCache = new TextureCache();
	}
		
	public void render() {
		GL46.glClear(GL46.GL_COLOR_BUFFER_BIT | GL46.GL_DEPTH_BUFFER_BIT);
		
			// Cascade shadow render pass
		GL46.glEnable(GL46.GL_DEPTH_TEST);
		GL46.glEnable(GL46.GL_BLEND);
		GL46.glBlendFunc(GL46.GL_SRC_ALPHA, GL46.GL_ONE_MINUS_SRC_ALPHA);
		GL46.glEnable(GL46.GL_MULTISAMPLE);
		GL46.glEnable(GL46.GL_CULL_FACE);
		GL46.glCullFace(GL46.GL_BACK);
		
		this.cascadeRenderPass.render(this);
        
			// Scene render pass
		Camera activeCamera = this.scene.getActiveCamera();
		
		GL46.glViewport(
			0, 0, this.clientWindow.getWidth(), this.clientWindow.getHeight()
		);
		activeCamera.getProjection().update(
			this.clientWindow.getWidth(), this.clientWindow.getHeight()
		);
		this.sceneRenderPass.setCascadeShadowRenderPass(this.cascadeRenderPass);
		
		this.sceneRenderPass.render(this);
		
			// GUI render pass
		if( this.scene.getGUI() != null ) {
			GL46.glDisable(GL46.GL_CULL_FACE);
		  	GL46.glDisable(GL46.GL_DEPTH_TEST);
		  	
		  	this.guiRenderPass.render(this);
		}
	}
	
	public Window getClientWindow() {
		return this.clientWindow;
	}
	
	public Scene getActiveScene() {
		return this.scene;
	}
	
	public VAOCache getVAOCache() {
		return this.vaoCache;
	}
	
	public TextureCache getTextureCache() {
		return this.textureCache;
	}
}

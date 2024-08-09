package project.opengl;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL46;

import project.Window;
import project.asset.sceneasset.Mesh;
import project.asset.texture.Texture;
import project.core.GameState;
import project.core.asset.IAsset;
import project.core.asset.IGraphics;
import project.core.asset.IGraphicsAsset;
import project.core.renderer.IRenderer;
import project.opengl.cshadow.CascadeShadowRenderPass;
import project.opengl.gui.GUIRenderPass;
import project.opengl.scene.SceneRenderPass;
import project.scene.ASceneObject;
import project.scene.Camera;
import project.scene.Scene;
import project.utils.DebugUtils;

public class RendererGL implements IRenderer {
	
	private Window clientWindow;
	
	private Queue<GameState> gameStateQueue;
	private GameState backGameState;
	private CascadeShadowRenderPass cascadeRenderPass;
	private SceneRenderPass sceneRenderPass;
	private GUIRenderPass guiRenderPass;
	
	private IGraphics defaultMeshGraphics;
	private IGraphics defaultTextureGraphics;
	
	public RendererGL(Window clientWindow, Scene scene) {
		this.gameStateQueue = new ConcurrentLinkedQueue<>();
		this.backGameState = new GameState();
		this.clientWindow = clientWindow;
		this.cascadeRenderPass = new CascadeShadowRenderPass();
		this.sceneRenderPass = new SceneRenderPass();
		this.guiRenderPass = new GUIRenderPass();
		
		this.defaultMeshGraphics = null;
		this.defaultTextureGraphics = null;
	}

	
	@Override
	public boolean initialize() {
		GL.createCapabilities();
		GL46.glClearColor(0.643f, 0.62f, 0.557f, 1.0f);
		
		this.generateDefaults();
		this.cascadeRenderPass.init();
		this.sceneRenderPass.init();
		this.guiRenderPass.init();
		
		return true;
	}
	
	@Override
	public boolean generateDefaults() {
		
			// Generate default mesh VAO
		VAO vao = new VAO(Mesh.DEFAULT);
		vao.generate();
		this.defaultMeshGraphics = vao;
		
			// Generate default TextureGL
		TextureGL textureGL = new TextureGL(Texture.DEFAULT);
		textureGL.generate();
		this.defaultTextureGraphics = textureGL;
		
		return true;
	}
		
	@Override
	public void render() {
		GameState gameState;
		while( (gameState = this.gameStateQueue.poll()) != null ) {
			
				// Process graphics asset generations and disposals before rendering
			IGraphicsAsset graphicsAsset;
			while( (graphicsAsset = gameState.pollGenerationRequest()) != null ) {
				this.processGenerationRequest(graphicsAsset);
			}
			
			while( (graphicsAsset = gameState.pollDisposalRequest()) != null ) {
				this.processDisposalRequest(graphicsAsset);
			}
			
			GL46.glClear(GL46.GL_COLOR_BUFFER_BIT | GL46.GL_DEPTH_BUFFER_BIT);
				
				// Cascade shadow render pass
			GL46.glEnable(GL46.GL_DEPTH_TEST);
			GL46.glEnable(GL46.GL_BLEND);
			GL46.glBlendFunc(GL46.GL_SRC_ALPHA, GL46.GL_ONE_MINUS_SRC_ALPHA);
			GL46.glEnable(GL46.GL_MULTISAMPLE);
			GL46.glEnable(GL46.GL_CULL_FACE);
			GL46.glCullFace(GL46.GL_BACK);
			
			this.cascadeRenderPass.render(this, gameState);
	        
				// Scene render pass
			Camera activeCamera = gameState.getActiveCamera();
			
			GL46.glViewport(
				0, 0, this.clientWindow.getWidth(), this.clientWindow.getHeight()
			);
			activeCamera.getProjection().update(
				this.clientWindow.getWidth(), this.clientWindow.getHeight()
			);
			this.sceneRenderPass.setCascadeShadowRenderPass(this.cascadeRenderPass);
			
			this.sceneRenderPass.render(this, gameState);
			
				// GUI render pass
			if( gameState.DEBUGgetGUI() != null ) {
				GL46.glDisable(GL46.GL_CULL_FACE); // Ignores which direction GUI elements are facing
			  	GL46.glDisable(GL46.GL_DEPTH_TEST); // Prevents close faces from overlapping with GUI
			  	
			  	this.guiRenderPass.render(this, gameState);
			}
		}
	}
	
	private void processGenerationRequest(IGraphicsAsset graphicsAsset) {
		if( !graphicsAsset.getGraphics().regenerate() ) {
			DebugUtils.log(
				this, 
				"WARNING: Unable to generate a graphics representation for graphics asset '" + 
				graphicsAsset.getName() + "'!", 
				"Using default instead."
			);
		}
	}
	
	private void processDisposalRequest(IGraphicsAsset graphicsAsset) {
		if( !graphicsAsset.getGraphics().dispose() ) {
			DebugUtils.log(
				this, 
				"WARNING: Unable to dispose the graphics representation of graphics asset '" + 
				graphicsAsset.getName() + "'!", 
				"Possibly attempting to dispose a default graphics representation."
			);
		}
	}
	
	@Override
	public void submitGameState() {
		this.gameStateQueue.add(this.backGameState);
		this.backGameState = new GameState();
	}
	
	@Override
	public void submitRenderable(ASceneObject object) {
		this.backGameState.listRenderable(object);
	}
	
	@Override
	public void assetLoaded(IAsset asset) {
		this.backGameState.listGenerationRequest((IGraphicsAsset) asset);
	}
	
	@Override
	public void assetDeloaded(IAsset asset) {
		this.backGameState.listDisposalRequest((IGraphicsAsset) asset);
	}
	
	@Override
	public Window getClientWindow() {
		return this.clientWindow;
	}
	
	@Override
	public IGraphics getDefaultMeshGraphics() {
		return this.defaultMeshGraphics;
	}
	
	@Override
	public IGraphics getDefaultTextureGraphics() {
		return this.defaultTextureGraphics;
	}
	
	@Override
	public GameState getBackGameState() {
		return this.backGameState;
	}
}

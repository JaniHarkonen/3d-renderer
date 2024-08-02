package project.opengl;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL46;

import project.Default;
import project.Window;
import project.asset.IAsset;
import project.asset.IGraphicsAsset;
import project.asset.ISystem;
import project.asset.Mesh;
import project.asset.Texture;
import project.pass.cshadow.CascadeShadowRenderPass;
import project.pass.gui.GUIRenderPass;
import project.pass.scene.SceneRenderPass;
import project.scene.ASceneObject;
import project.scene.Camera;
import project.scene.GameState;
import project.scene.Scene;

public class Renderer implements ISystem {
	
	private Window clientWindow;
	
	private Queue<GameState> gameStateQueue;
	private GameState backGameState;
	private CascadeShadowRenderPass cascadeRenderPass;
	private SceneRenderPass sceneRenderPass;
	private GUIRenderPass guiRenderPass;
	
	private Scene scene;
	
	public Renderer(Window clientWindow, Scene scene) {
		this.gameStateQueue = new ConcurrentLinkedQueue<>();
		this.backGameState = new GameState();
		this.clientWindow = clientWindow;
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
		this.generateDefaults();
		
			// Initialize scene graphics assets
		this.scene.init();
	}
	
	public void generateDefaults() {
		
			// Add generation requests for the default assets
		this.backGameState.listGenerationRequest(Default.MESH);
		this.backGameState.listGenerationRequest(Default.TEXTURE);
	}
		
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
				GL46.glDisable(GL46.GL_CULL_FACE); // Ignores which direction GUI elements are facing
			  	GL46.glDisable(GL46.GL_DEPTH_TEST); // Prevents close faces from overlapping with GUI
			  	
			  	this.guiRenderPass.render(this);
			}
		}
	}
	
	private void processGenerationRequest(IGraphicsAsset graphicsAsset) {
		if( graphicsAsset instanceof Mesh ) {
			Mesh mesh = (Mesh) graphicsAsset;
			VAO vao = new VAO(mesh);
			vao.generate();
		} else if( graphicsAsset instanceof Texture ) {
			Texture texture = (Texture) graphicsAsset;
			TextureGL textureGL = new TextureGL(texture);
			textureGL.generate();
		}
	}
	
	private void processDisposalRequest(IGraphicsAsset graphicsAsset) {
		graphicsAsset.getGraphics().dispose();
	}
	
	public void submitGameState() {
		this.gameStateQueue.add(this.backGameState);
		this.backGameState = new GameState();
	}
	
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
	
	public Window getClientWindow() {
		return this.clientWindow;
	}
	
	public Scene getActiveScene() {
		return this.scene;
	}
}

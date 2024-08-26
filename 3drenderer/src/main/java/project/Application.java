package project;

import project.core.Networker;
import project.core.asset.AssetManager;
import project.core.renderer.IRenderer;
import project.opengl.RendererGL;
import project.scene.Scene;
import project.shared.NetworkStandard;
import project.testing.TestAssets;
import project.utils.DebugUtils;

public class Application {
	
	public static void main(String[] args) {
		if( APPLICATION != null )
		return;
		
		APPLICATION = new Application();
		APPLICATION.execute();
	}
	
	
	private static Application APPLICATION;
	private AssetManager assetManager;
	private Networker networker;
	private Window window;
	private IRenderer renderer;
	
	public Application() {
		this.assetManager = null;
		this.networker = null;
		this.window = null;
		this.renderer = null;
	}
	
	
	public void execute() {
		final String TITLE = "3D Renderer - JOHNEngine";
		final int FPS_MAX = 60;
		final int TICK_RATE = 60;
		
			// Asset manager
		this.assetManager = new AssetManager();
		
			// Networker
		NetworkStandard networkStandard = new NetworkStandard();
		networkStandard.declare();
		this.networker = new Networker(networkStandard);
		this.networker.launchSession("localhost", 12345);
		
			// Window and renderer
		Window window = new Window(TITLE, 800, 600, FPS_MAX, 0);
		this.window = window;
			this.renderer = new RendererGL(window);
			window.setRenderer(this.renderer);
		window.initialize();
		
			// Scene and assets
		TestAssets.initialize();
		Scene scene = new Scene(this, TICK_RATE);
		scene.initialize();
		
			// Game loop
		while( !window.isDestroyed() ) {
			window.refresh();
			this.networker.handleInboundMessages();
			scene.update();
			this.networker.handleOutboundMessages();
		}
		
		this.networker.abortSession();
		DebugUtils.log(this, "main loop terminated!");
	}
	
	public static Application getApp() {
		return APPLICATION;
	}
	
	public AssetManager getAssetManager() {
		return this.assetManager;
	}
	
	public Window getWindow() {
		return this.window;
	}
	
	public IRenderer getRenderer() {
		return this.renderer;
	}
	
	public Networker getNetworker() {
		return this.networker;
	}
}

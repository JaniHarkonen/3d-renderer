package project;

import project.core.Client;
import project.core.asset.AssetManager;
import project.core.renderer.IRenderer;
import project.opengl.RendererGL;
import project.scene.Scene;
import project.shared.Networker;
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
	private Client client;
	private Window window;
	private IRenderer renderer;
	
	public void execute() {
		final String TITLE = "3D Renderer - JOHNEngine";
		final int FPS_MAX = 60;
		final int TICK_RATE = 60;
		
			// Asset manager
		this.assetManager = new AssetManager();
		
			// Networker
		this.client = new Client("localhost", 12345);
		this.networker = new Networker(this.client);
		this.client.assignToNetworker(this.networker);
		
		Thread networkerThread = new Thread(this.networker);
		networkerThread.start();
		
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
			scene.update();
		}
		
		this.networker.shutdown();
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
	
	public Client getClient() {
		return this.client;
	}
}

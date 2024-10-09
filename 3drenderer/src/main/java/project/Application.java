package project;

import project.core.Networker;
import project.core.asset.AssetManager;
import project.core.renderer.IRenderer;
import project.opengl.RendererGL;
import project.scene.Scene;
import project.shared.ConsoleRecorder;
import project.shared.logger.Logger;
import project.testing.TestAssets;

public class Application {
	
	private static Application APPLICATION;
	
	public static void main(String[] args) {
		if( APPLICATION != null )
		return;
		
		APPLICATION = new Application();
		APPLICATION.execute();
	}
	
	
	private AssetManager assetManager;
	private Networker networker;
	private Window window;
	private IRenderer renderer;
	private Scene scene;
	
	public Application() {
		this.assetManager = null;
		this.networker = null;
		this.window = null;
		this.renderer = null;
		this.scene = null;
		Logger.configure(
			//Logger.LOG_TIMESTAMP | 
			//Logger.LOG_SYSTEM | 
			Logger.LOG_CALLER | 
			Logger.LOG_SEVERITY, 
			Logger.INFO
		);
		ConsoleRecorder consoleRecoder = new ConsoleRecorder();
		Logger.get().registerTarget(consoleRecoder);
		Logger.get().info(this, "Logger has been configured!");
	}
	
	
	public void execute() {
		final String TITLE = "3D Renderer - JOHNEngine";
		final int FPS_MAX = 60;
		final int TICK_RATE = 60;
		
			// Asset manager
		this.assetManager = new AssetManager();
		
			// Networker
		//NetworkStandard networkStandard = new NetworkStandard();
		//networkStandard.declare();
		//this.networker = new Networker(networkStandard);
		//this.networker.launchSession("localhost", 12345);
		
			// Window and renderer
		Window window = new Window(TITLE, 800, 600, FPS_MAX, 0);
		this.window = window;
			this.renderer = new RendererGL(window);
			window.setRenderer(this.renderer);
		window.initialize();
		
			// Scene and assets
		TestAssets.initialize();
		this.scene = new Scene(this, TICK_RATE);
		this.scene.initialize();
		
			// Game loop
		while( !window.isDestroyed() ) {
			window.refresh();
			//this.networker.handleInboundMessages();
			this.scene.update();
			//this.networker.handleOutboundMessages();
		}
		
		//this.networker.abortSession();
		//DebugUtils.log(this, "main loop terminated!");
		Logger.get().info(this, "Main loop terminated!");
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
	
	public Scene getScene() {
		return this.scene;
	}
}

package project;

import java.io.IOException;
import java.net.Socket;

import project.core.asset.AssetManager;
import project.core.renderer.IRenderer;
import project.opengl.RendererGL;
import project.scene.Scene;
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
	private Window window;
	private IRenderer renderer;
	
	public void execute() {
		final String TITLE = "3D Renderer - JOHNEngine";
		final int FPS_MAX = 60;
		final int TICK_RATE = 60;
		
		this.assetManager = new AssetManager();
		
		Thread networkerThread = new Thread(new Networker("localhost", 12345));
		networkerThread.start();
		
		Scene scene = new Scene(this, TICK_RATE);
		Window window = new Window(TITLE, 800, 600, FPS_MAX, 0);
		this.window = window;
			this.renderer = new RendererGL(window, scene);
			window.setRenderer(this.renderer);
		window.initialize();
		
		TestAssets.initialize();
		scene.initialize();
		
		while( !window.isDestroyed() ) {
			window.refresh();
			scene.update();
		}
		
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
}

package project;

import project.opengl.Renderer;
import project.scene.Scene;
import project.utils.DebugUtils;

public class Application {
	
	private Window window;

	public static void main(String[] args) {
		Application app = new Application();
		app.execute();
	}
	
	public void execute() {
		final String TITLE = "3D Renderer - JOHNEngine";
		final int FPS_MAX = 300;
		final int TICK_RATE = 60;
		
		Scene scene = new Scene(this, TICK_RATE);
		Window window = new Window(TITLE, 800, 600, FPS_MAX, 0);
		this.window = window;
		Renderer renderer = new Renderer(window, scene);
		
		window.setRenderer(renderer);
		window.init();
		
		while( !window.isDestroyed() ) {
			window.refresh();
			scene.update();
		}
		
		DebugUtils.log(this, "main loop terminated!");
	}
	
	public Window getWindow() {
		return this.window;
	}
}

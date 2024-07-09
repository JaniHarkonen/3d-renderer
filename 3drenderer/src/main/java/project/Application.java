package project;

import project.opengl.Renderer;
import project.scene.Scene;

public class Application {

	public static void main(String[] args) {
		Application app = new Application();
		app.execute();
	}
	
	public void execute() {
		final String TITLE = "3D Renderer";
		final int FPS_MAX = 60;
		
		Scene scene = new Scene();
		Window window = new Window(TITLE, 800, 600, FPS_MAX, 0);
		Renderer renderer = new Renderer(window, scene);
		
		window.setRenderer(renderer);
		window.init();
		
		while( !window.isDestroyed() ) {
			window.refresh();
			scene.update();
		}
		
		System.out.println("main loop terminated!");
	}
}

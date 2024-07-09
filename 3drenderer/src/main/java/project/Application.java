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
		
		Window window = new Window(TITLE, 800, 600, FPS_MAX, 0);
		window.setRenderer(new Renderer(window));
		window.init();
		
		Scene scene = new Scene();
		
		while( !window.isDestroyed() ) {
			window.refresh();
			scene.update();
		}
		
		System.out.println("main loop terminated!");
	}
}

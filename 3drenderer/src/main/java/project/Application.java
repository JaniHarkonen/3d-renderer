package project;

public class Application {

	public static void main(String[] args) {
		Application app = new Application();
		app.execute();
	}
	
	public void execute() {
		final String TITLE = "3D Renderer";
		final int FPS_MAX = 60;
		
		Window window = new Window(TITLE, 800, 600);
		window.setRenderer(new Renderer(window));
		window.init();
		
		long time = System.nanoTime();
		long frameTime = System.nanoTime();
		long frameDelta = 1000000000 / FPS_MAX;
		int counter = 0;
		while( !window.closeIfNeeded() ) {
			if( System.nanoTime() - frameTime >= frameDelta ) {
				frameTime = System.nanoTime();
				
				window.pollInputEvents();
				window.swapBuffers();
				counter++;
				
				if( System.nanoTime() - time >= 1000000000 ) {
					window.setTitle(TITLE + " | FPS: " + counter + "/" + FPS_MAX);
					counter = 0;
					time = System.nanoTime();
				}
			}
		}
		
		System.out.println("main loop terminated!");
	}
}

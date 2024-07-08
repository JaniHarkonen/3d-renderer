package project;

public class Application {

	public static void main(String[] args) {
		Application app = new Application();
		app.execute();
	}
	
	public void execute() {
		final String TITLE = "3D Renderer";
		Window window = new Window(TITLE, 800, 600);
		window.setRenderer(new Renderer(window));
		window.init();
		
		long time = System.nanoTime();
		int counter = 0;
		while( !window.closeIfNeeded() ) {
			window.pollInputEvents();
			window.swapBuffers();
			counter++;
			
			if( System.nanoTime() - time >= 1000000000 ) {
				window.setTitle(TITLE + " | FPS: " + counter);
				counter = 0;
				time = System.nanoTime();
			}
		}
		
		System.out.println("main loop terminated!");
	}
}

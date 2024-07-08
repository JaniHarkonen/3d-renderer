package project;

public class Application {

	public static void main(String[] args) {
		Application app = new Application();
		app.execute();
	}
	
	public void execute() {
		Window window = new Window(800, 600);
		window.setRenderer(new Renderer(window));
		window.init();
		
		while( !window.closeIfNeeded() ) {
			window.pollInputEvents();
			window.swapBuffers();
		}
		
		
		System.out.println("main loop terminated!");
	}
}

package project.server;

import project.shared.Networker;

public class Application {

	public static void main(String[] args) {
		Application app = new Application();
		app.execute();
	}
	
	
	public void execute() {
		Server server = new Server(12345);
		Networker networker = new Networker(server);
		server.assignToNetworker(networker);
		
		Thread networkerThread = new Thread(networker);
		networkerThread.start();
		
			// Update game logic
		long time = System.nanoTime();
		while( true ) {
			if( System.nanoTime() - time >= 1000000000 ) {
				System.out.println("game tick");
			}
		}
	}
}

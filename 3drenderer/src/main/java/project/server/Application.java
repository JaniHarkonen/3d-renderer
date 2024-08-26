package project.server;

import project.shared.NetworkStandard;

public class Application {

	public static void main(String[] args) {
		Application app = new Application();
		app.execute();
	}
	
	
	public void execute() {
		NetworkStandard networkStandard = new NetworkStandard();
		networkStandard.declare();
		Networker networker = new Networker(networkStandard, 12345);
		Thread networkerThread = new Thread(networker);
		networkerThread.start();
		
		while( true ) {
			networker.handleInboundMessages();
		}
	}
}

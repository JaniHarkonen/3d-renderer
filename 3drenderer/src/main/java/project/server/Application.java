package project.server;

import project.shared.NetworkStandard;

public class Application {

	public static void main(String[] args) {
		Application app = new Application();
		app.execute();
	}
	
	
	private Networker networker;
	
	public Application() {
		this.networker = null;
	}
	
	
	public void execute() {
		Game game = new Game(this);
		
		NetworkStandard networkStandard = new NetworkStandard();
		networkStandard.declare();
		this.networker = new Networker(networkStandard, 12345);
		Thread networkerThread = new Thread(this.networker);
		networkerThread.start();
		
		while( true ) {
			this.networker.handleInboundMessages();
			game.tick(0.0f);
			//this.networker.handleOutboundMessages();
		}
	}
	
	public Networker getNetworker() {
		return this.networker;
	}
}

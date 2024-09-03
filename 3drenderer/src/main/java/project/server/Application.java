package project.server;

import project.shared.NetworkStandard;
import project.server.NEW.Game;

public class Application {	
	private static Application app;
	
	public static void main(String[] args) {
		app = new Application();
		app.execute();
	}
	
	public static Application getApp() {
		return app;
	}
	
	
	private Networker networker;
	private Game game;
	
	public Application() {
		this.networker = null;
	}
	
	
	public void execute() {
		this.game = new Game();
		
		NetworkStandard networkStandard = new NetworkStandard();
		networkStandard.declare();
		this.networker = new Networker(networkStandard, 12345);
		Thread networkerThread = new Thread(this.networker);
		networkerThread.start();
		
		while( true ) {
			this.networker.handleInboundMessages();
			this.game.tick(0.0f);
			this.networker.handleOutboundMessages();
		}
	}
	
	public Networker getNetworker() {
		return this.networker;
	}
	
	public Game getGame() {
		return this.game;
	}
}

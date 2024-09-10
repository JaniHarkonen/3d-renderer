package project.server;

import project.server.NEW.Game;
import project.shared.NetworkStandard;

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
		
		final long tickRate = 1000000000 / 8;
		long time = System.nanoTime();
		while( true ) {
			if( System.nanoTime() - time < tickRate ) {
				continue;
			}
			
			this.networker.handleInboundMessages();
			
				float deltaTime = (System.nanoTime() - time) / 1000000000.0f;
				time = System.nanoTime();
				this.game.tick(deltaTime);
				
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

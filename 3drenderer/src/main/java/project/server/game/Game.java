package project.server.game;

import project.utils.DebugUtils;

public class Game {
	
	private GameState frontGameState;
	private GameState backGameState;
	private long DEBUGtimer;
	
	public Game() {
		this.backGameState = new GameState();
		this.frontGameState = null;
		this.DEBUGtimer = System.nanoTime();
	}
	
	
	public void tick(float deltaTime) {
			// DEBUG - create test soldier
		if( System.nanoTime() - this.DEBUGtimer >= 1000000000l ) {
			this.backGameState.addObject(new TestSoldier());
			this.DEBUGtimer = Long.MAX_VALUE;
			DebugUtils.log(this, "soldier added");
		}
		
		this.backGameState.tick(deltaTime);
		
		if( this.frontGameState == null ) {
				// First game state submission -> copy completely
			this.frontGameState = this.backGameState.deepCopyComplete();
		} else {
				// First submission already posted -> copy only changed objects
			this.frontGameState = this.backGameState.deepCopyChangesOnly(this.frontGameState);
			this.backGameState.reset();
		}
	}
	
	public GameState getLatestGameState() {
		return this.frontGameState;
	}
}

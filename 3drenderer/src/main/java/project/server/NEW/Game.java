package project.server.NEW;

public class Game {
	
	private GameState frontGameState;
	private GameState backGameState;
	
	public Game() {
		this.backGameState = new GameState();
		this.frontGameState = null;
	}
	
	
	public void tick(float deltaTime) {
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

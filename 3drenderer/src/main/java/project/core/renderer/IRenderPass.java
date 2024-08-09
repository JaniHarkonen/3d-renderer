package project.core.renderer;

import project.core.GameState;

public interface IRenderPass {

	public boolean init();
	public void render(IRenderer renderer, GameState gameState);
	public GameState getGameState();
}

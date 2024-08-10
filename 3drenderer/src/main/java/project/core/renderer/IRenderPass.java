package project.core.renderer;

import project.core.GameState;

public interface IRenderPass {

	public boolean initialize();
	public void render(IRenderer renderer, GameState gameState);
	public GameState getGameState();
}

package project.core.renderer;

import project.Window;
import project.core.GameState;
import project.core.asset.IGraphics;
import project.core.asset.ISystem;
import project.scene.ASceneObject;

public interface IRenderer extends ISystem {

	public boolean initialize();
	
	public boolean generateDefaults();
	
	public void render();
	
	public void submitGameState();
	
	public void submitRenderable(ASceneObject object);
	
	public Window getClientWindow();
	
	public IGraphics getDefaultMeshGraphics();
	
	public IGraphics getDefaultTextureGraphics();
	
	public GameState getBackGameState();
}

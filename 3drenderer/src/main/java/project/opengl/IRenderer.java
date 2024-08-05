package project.opengl;

import project.Window;
import project.asset.IGraphics;
import project.asset.ISystem;
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
}

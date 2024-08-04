package project.pass;

import project.opengl.Renderer;

public interface IRenderPass {

	public boolean init();
	public void render(Renderer renderer);
}

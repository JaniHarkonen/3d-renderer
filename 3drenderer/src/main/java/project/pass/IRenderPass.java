package project.pass;

import project.opengl.IRenderer;

public interface IRenderPass {

	public boolean init();
	public void render(IRenderer renderer);
}

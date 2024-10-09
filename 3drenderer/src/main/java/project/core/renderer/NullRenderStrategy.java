package project.core.renderer;

import project.core.IRenderable;

public class NullRenderStrategy<P extends IRenderPass, R extends IRenderable> 
	implements IRenderStrategy<P, R> 
{
	@Override
	public void execute(IRenderer renderer, P renderPass, R renderable) {}
}

package project.core.renderer;

import project.core.IRenderable;

public class NullRenderStrategy<T extends IRenderPass> implements IRenderStrategy<T> {

	@Override
	public void execute(IRenderer renderer, T renderPass, IRenderable renderable) {}
}

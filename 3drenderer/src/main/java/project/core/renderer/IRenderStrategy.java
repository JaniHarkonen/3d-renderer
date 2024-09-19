package project.core.renderer;

import project.core.IRenderable;

public interface IRenderStrategy<T extends IRenderPass> {

	public void execute(IRenderer renderer, T renderPass, IRenderable renderable);
}

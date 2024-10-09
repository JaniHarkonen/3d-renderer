package project.core.renderer;

import project.core.IRenderable;

public interface IRenderStrategy<P extends IRenderPass, R extends IRenderable> {

	public void execute(IRenderer renderer, P renderPass, R renderable);
}

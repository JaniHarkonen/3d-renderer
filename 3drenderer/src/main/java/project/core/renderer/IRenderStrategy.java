package project.core.renderer;

import project.scene.ASceneObject;

public interface IRenderStrategy<T extends IRenderPass> {

	public void execute(IRenderer renderer, T renderPass, ASceneObject target);
}

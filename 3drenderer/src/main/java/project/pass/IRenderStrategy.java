package project.pass;

import project.opengl.IRenderer;
import project.scene.ASceneObject;

public interface IRenderStrategy<T extends IRenderPass> {

	public void execute(IRenderer renderer, T renderPass, ASceneObject target);
}

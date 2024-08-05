package project.pass;

import project.opengl.IRenderer;
import project.scene.ASceneObject;

public class NullRenderStrategy<T extends IRenderPass> implements IRenderStrategy<T> {

	@Override
	public void execute(IRenderer renderer, T renderPass, ASceneObject target) {}
}

package project.pass;

import project.opengl.Renderer;
import project.scene.ASceneObject;

public class NullRenderStrategy<T extends IRenderPass> implements IRenderStrategy<T> {

	@Override
	public void execute(Renderer renderer, T renderPass, ASceneObject target) {}
}

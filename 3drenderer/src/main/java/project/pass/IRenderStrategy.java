package project.pass;

import project.opengl.Renderer;
import project.scene.ASceneObject;

public interface IRenderStrategy<T extends IRenderPass> {

	public void execute(Renderer renderer, T renderPass, ASceneObject target);
}

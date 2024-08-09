package project.opengl.scene;

import project.core.renderer.IRenderStrategy;
import project.core.renderer.IRenderer;
import project.scene.ASceneObject;
import project.scene.PointLight;

public class RenderPointLight implements IRenderStrategy<SceneRenderPass> {

	@Override
	public void execute(IRenderer renderer, SceneRenderPass renderPass, ASceneObject target) {
			// Currently only supports a single point light, CHANGE THIS LATER
		renderPass.updatePointLight((PointLight) target, 0);
	}
}

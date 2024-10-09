package project.opengl.scene;

import project.core.renderer.IRenderStrategy;
import project.core.renderer.IRenderer;
import project.scene.ASceneObject;
import project.scene.PointLight;

public class RenderPointLight implements IRenderStrategy<SceneRenderPass, ASceneObject> {

	@Override
	public void execute(IRenderer renderer, SceneRenderPass renderPass, ASceneObject renderable) {
			// Currently only supports a single point light, CHANGE THIS LATER
		renderPass.updatePointLight((PointLight) renderable, 0);
	}
}

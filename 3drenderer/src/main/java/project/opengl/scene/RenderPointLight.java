package project.opengl.scene;

import project.core.IRenderable;
import project.core.renderer.IRenderStrategy;
import project.core.renderer.IRenderer;
import project.scene.PointLight;

public class RenderPointLight implements IRenderStrategy<SceneRenderPass> {

	@Override
	public void execute(IRenderer renderer, SceneRenderPass renderPass, IRenderable renderable) {
			// Currently only supports a single point light, CHANGE THIS LATER
		renderPass.updatePointLight((PointLight) renderable, 0);
	}
}

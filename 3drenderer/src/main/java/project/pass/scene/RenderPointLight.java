package project.pass.scene;

import project.opengl.IRenderer;
import project.opengl.Renderer;
import project.pass.IRenderStrategy;
import project.scene.ASceneObject;
import project.scene.PointLight;

public class RenderPointLight implements IRenderStrategy<SceneRenderPass> {

	@Override
	public void execute(IRenderer renderer, SceneRenderPass renderPass, ASceneObject target) {
			// Currently only supports a single point light, CHANGE THIS LATER
		renderPass.updatePointLight(((Renderer) renderer).getActiveScene(), (PointLight) target, 0);
	}
}

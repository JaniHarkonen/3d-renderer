package project.pass.scene;

import project.opengl.Renderer;
import project.pass.IRenderStrategy;
import project.scene.ASceneObject;
import project.scene.PointLight;

public class RenderPointLight implements IRenderStrategy<SceneRenderPass> {

	@Override
	public void execute(Renderer renderer, SceneRenderPass renderPass, ASceneObject target) {
			// Currently only supports a single point light, CHANGE THIS LATER
		renderPass.updatePointLight(renderer.getActiveScene(), (PointLight) target, 0);
	}
}

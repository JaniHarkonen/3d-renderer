package project.opengl.scene;

import project.core.renderer.IRenderStrategy;
import project.core.renderer.IRenderer;
import project.opengl.shader.uniform.object.amlight.SSAmbientLight;
import project.scene.ASceneObject;
import project.scene.AmbientLight;

public class RenderAmbientLight implements IRenderStrategy<SceneRenderPass, ASceneObject> {

	private SSAmbientLight ambientLightStruct;
	
	RenderAmbientLight() {
		this.ambientLightStruct = new SSAmbientLight();
	}
	
	@Override
	public void execute(IRenderer renderer, SceneRenderPass renderPass, ASceneObject renderable) {
		AmbientLight ambientLight = (AmbientLight) renderable;
		this.ambientLightStruct.factor = ambientLight.getIntensity();
		this.ambientLightStruct.color = ambientLight.getColor();
		renderPass.uAmbientLight.update(this.ambientLightStruct);
	}
}

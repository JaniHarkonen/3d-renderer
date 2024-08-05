package project.opengl.scene;

import project.core.renderer.IRenderStrategy;
import project.core.renderer.IRenderer;
import project.opengl.shader.ShaderProgram;
import project.scene.ASceneObject;
import project.scene.AmbientLight;

public class RenderAmbientLight implements IRenderStrategy<SceneRenderPass> {

	@Override
	public void execute(IRenderer renderer, SceneRenderPass renderPass, ASceneObject target) {
		ShaderProgram activeShaderProgram = renderPass.shaderProgram;
		AmbientLight ambientLight = (AmbientLight) target;
		activeShaderProgram.setFloat1Uniform(
			SceneRenderPass.U_AMBIENT_LIGHT_FACTOR, ambientLight.getIntensity()
		);
		activeShaderProgram.setVector3fUniform(
			SceneRenderPass.U_AMBIENT_LIGHT_COLOR, ambientLight.getColor()
		);
	}
}

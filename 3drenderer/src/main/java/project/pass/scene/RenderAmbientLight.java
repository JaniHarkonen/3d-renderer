package project.pass.scene;

import project.opengl.IRenderer;
import project.pass.IRenderStrategy;
import project.scene.ASceneObject;
import project.scene.AmbientLight;
import project.shader.ShaderProgram;

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

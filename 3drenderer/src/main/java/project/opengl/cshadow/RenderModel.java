package project.opengl.cshadow;

import org.lwjgl.opengl.GL46;

import project.asset.sceneasset.Mesh;
import project.core.renderer.IRenderStrategy;
import project.core.renderer.IRenderer;
import project.opengl.shader.ShaderProgram;
import project.opengl.shader.uniform.UAMatrix4f;
import project.opengl.vao.VAO;
import project.scene.ASceneObject;
import project.scene.Model;

class RenderModel implements IRenderStrategy<CascadeShadowRenderPass> {

	@Override
	public void execute(IRenderer renderer, CascadeShadowRenderPass renderPass, ASceneObject target) {
		ShaderProgram activeShaderProgram = renderPass.shaderProgram;
		Model model = (Model) target;
		for( int j = 0; j < model.getMeshCount(); j++ ) {
			Mesh mesh = model.getMesh(j);
			VAO vao = (VAO) mesh.getGraphics();
    		vao.bind();
    		
    		UAMatrix4f.class.cast(activeShaderProgram.getUniform(Uniforms.OBJECT_TRANSFORM))
    		.update(target.getTransform().getAsMatrix());
    		
    		UAMatrix4f.class.cast(activeShaderProgram.getUniform(Uniforms.BONE_MATRICES))
			.update(model.getAnimator().getCurrentFrame().getBoneTransforms());
    		
    		GL46.glDrawElements(
				GL46.GL_TRIANGLES, vao.getVertexCount() * 3, GL46.GL_UNSIGNED_INT, 0
			);
		}
	}
}

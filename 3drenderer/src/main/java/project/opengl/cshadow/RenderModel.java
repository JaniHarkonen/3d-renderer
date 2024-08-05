package project.opengl.cshadow;

import org.lwjgl.opengl.GL46;

import project.asset.sceneasset.AnimationData;
import project.asset.sceneasset.Mesh;
import project.core.renderer.IRenderStrategy;
import project.core.renderer.IRenderer;
import project.opengl.VAO;
import project.opengl.shader.ShaderProgram;
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
    		
    		activeShaderProgram.setMatrix4fUniform(
				CascadeShadowRenderPass.U_OBJECT_TRANSFORM, target.getTransformMatrix()
			);
    		AnimationData animationData = model.getAnimationData();
			if( animationData == null ) {
				activeShaderProgram.setMatrix4fArrayUniform(
					CascadeShadowRenderPass.U_BONE_MATRICES, AnimationData.DEFAULT_BONE_TRANSFORMS
				);
			} else {
				activeShaderProgram.setMatrix4fArrayUniform(
					CascadeShadowRenderPass.U_BONE_MATRICES, 
					animationData.getCurrentFrame().getBoneTransforms()
				);
			}
    		GL46.glDrawElements(
				GL46.GL_TRIANGLES, vao.getVertexCount() * 3, GL46.GL_UNSIGNED_INT, 0
			);
		}
	}
}

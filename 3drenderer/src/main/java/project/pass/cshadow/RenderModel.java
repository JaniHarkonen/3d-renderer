package project.pass.cshadow;

import org.lwjgl.opengl.GL46;

import project.asset.AnimationData;
import project.asset.Mesh;
import project.opengl.Renderer;
import project.opengl.VAO;
import project.opengl.VAOCache;
import project.pass.IRenderStrategy;
import project.scene.ASceneObject;
import project.scene.Model;
import project.shader.ShaderProgram;

class RenderModel implements IRenderStrategy<CascadeShadowRenderPass> {

	@Override
	public void execute(Renderer renderer, CascadeShadowRenderPass renderPass, ASceneObject target) {
		VAOCache vaoCache = renderer.getVAOCache();
		ShaderProgram activeShaderProgram = renderPass.shaderProgram;
		Model model = (Model) target;
		for( int j = 0; j < model.getMeshCount(); j++ ) {
			Mesh mesh = model.getMesh(j);
    		VAO vao = vaoCache.getOrGenerate(mesh);
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

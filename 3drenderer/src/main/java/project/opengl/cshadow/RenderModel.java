package project.opengl.cshadow;

import org.lwjgl.opengl.GL46;

import project.asset.sceneasset.Mesh;
import project.core.IRenderable;
import project.core.renderer.IRenderStrategy;
import project.core.renderer.IRenderer;
import project.opengl.vao.VAO;
import project.scene.Model;

class RenderModel implements IRenderStrategy<CascadeShadowRenderPass> {

	@Override
	public void execute(IRenderer renderer, CascadeShadowRenderPass renderPass, IRenderable renderable) {
		Model model = (Model) renderable;
		for( int j = 0; j < model.getMeshCount(); j++ ) {
			Mesh mesh = model.getMesh(j);
			VAO vao = (VAO) mesh.getGraphics();
    		vao.bind();
    		
    		renderPass.uObjectTransform.update(model.getTransform().getAsMatrix());
    		renderPass.uBoneMatrices.update(model.getAnimator().getCurrentFrame().getBoneTransforms());
    		
    		GL46.glDrawElements(
				GL46.GL_TRIANGLES, vao.getVertexCount() * 3, GL46.GL_UNSIGNED_INT, 0
			);
		}
	}
}

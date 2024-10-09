package project.opengl.cshadow;

import org.lwjgl.opengl.GL46;

import project.asset.sceneasset.Mesh;
import project.core.renderer.IRenderStrategy;
import project.core.renderer.IRenderer;
import project.opengl.vao.VAO;
import project.scene.ASceneObject;
import project.scene.Model;

class RenderModel implements IRenderStrategy<CascadeShadowRenderPass, ASceneObject> {

	@Override
	public void execute(IRenderer renderer, CascadeShadowRenderPass renderPass, ASceneObject object) {
		Model model = (Model) object;
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

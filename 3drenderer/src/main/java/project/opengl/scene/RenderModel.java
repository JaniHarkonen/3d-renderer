package project.opengl.scene;

import org.lwjgl.opengl.GL46;

import project.asset.sceneasset.Mesh;
import project.asset.texture.Texture;
import project.component.Material;
import project.core.renderer.IRenderStrategy;
import project.core.renderer.IRenderer;
import project.opengl.TextureGL;
import project.opengl.shader.uniform.object.material.SSMaterial;
import project.opengl.vao.VAO;
import project.scene.ASceneObject;
import project.scene.Model;
import project.testing.TestDebugDataHandles;

class RenderModel implements IRenderStrategy<SceneRenderPass> {
	
	private SSMaterial materialStruct;
	
	RenderModel() {
		this.materialStruct = new SSMaterial();
	}

	@Override
	public void execute(IRenderer renderer, SceneRenderPass renderPass, ASceneObject target) {
		Model model = (Model) target;
		renderPass.uObjectTransform.update(target.getTransform().getAsMatrix());
		
		for( int m = 0; m < model.getMeshCount(); m++ ) {
			Material material = model.getMaterial(m);
			Mesh mesh = model.getMesh(m);
			
			for( int i = 0; i < material.getTextures().length; i++ ) {
				Texture texture = material.getTextures()[i];
				
				if( texture == null ) {
					continue;
				}
				
				TextureGL textureGL = (TextureGL) texture.getGraphics();
				GL46.glActiveTexture(GL46.GL_TEXTURE0 + i);
				textureGL.bind();
			}
			
			boolean areNormalsActive = 
				(boolean) renderPass.getGameState().getDebugData(TestDebugDataHandles.NORMALS_ACTIVE);
			boolean isRoughnessActive = 
				(boolean) renderPass.getGameState().getDebugData(TestDebugDataHandles.ROUGHNESS_ACTIVE);
			
			this.materialStruct.hasNormalMap = 
				(material.getTexture(1) != null && areNormalsActive) ? 1 : 0;
			this.materialStruct.hasRoughnessMap = 
				(material.getTexture(2) != null && isRoughnessActive) ? 1 : 0;
			this.materialStruct.ambient = material.getAmbientColor();
			this.materialStruct.diffuse = material.getDiffuseColor();
			this.materialStruct.specular = material.getSpecularColor();
			this.materialStruct.reflectance = material.getReflectance();
			
			renderPass.uMaterial.update(this.materialStruct);
			
			VAO vao = (VAO) mesh.getGraphics();
			vao.bind();
			
			renderPass.uBoneMatrices.update(model.getAnimator().getCurrentFrame().getBoneTransforms());

			GL46.glDrawElements(
				GL46.GL_TRIANGLES, vao.getVertexCount() * 3, GL46.GL_UNSIGNED_INT, 0
			);
		}
	}
}

package project.opengl.scene;

import org.lwjgl.opengl.GL46;

import project.asset.sceneasset.AnimationData;
import project.asset.sceneasset.Mesh;
import project.asset.texture.Texture;
import project.component.Material;
import project.core.renderer.IRenderStrategy;
import project.core.renderer.IRenderer;
import project.opengl.RendererGL;
import project.opengl.TextureGL;
import project.opengl.VAO;
import project.opengl.shader.ShaderProgram;
import project.opengl.shader.custom.material.SSMaterial;
import project.opengl.shader.custom.material.UMaterial;
import project.opengl.shader.uniform.UAMatrix4f;
import project.scene.ASceneObject;
import project.scene.Model;

class RenderModel implements IRenderStrategy<SceneRenderPass> {
	
	private SSMaterial materialStruct;
	
	RenderModel() {
		this.materialStruct = new SSMaterial();
	}

	@Override
	public void execute(IRenderer renderer, SceneRenderPass renderPass, ASceneObject target) {
		ShaderProgram activeShaderProgram = renderPass.shaderProgram;
		
		target.updateTransformMatrix();
		UAMatrix4f.class.cast(
			activeShaderProgram.getUniform("uObjectTransform")).update(target.getTransformMatrix()
		);
		
		Model model = (Model) target;
		
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
			
			if( material.getTexture(1) != null && ((RendererGL) renderer).getActiveScene().DEBUGareNormalsActive() ) {
				this.materialStruct.hasNormalMap = 1;
			} else {
				this.materialStruct.hasNormalMap = 0;
			}
			
			this.materialStruct.ambient = material.getAmbientColor();
			this.materialStruct.diffuse = material.getDiffuseColor();
			this.materialStruct.specular = material.getSpecularColor();
			this.materialStruct.reflectance = material.getReflectance();
			
			UMaterial.class.cast(activeShaderProgram.getUniform("uMaterial")).update(this.materialStruct);
			
			VAO vao = (VAO) mesh.getGraphics();
			vao.bind();
			
			AnimationData animationData = model.getAnimationData();
			
			if( animationData == null ) {
				UAMatrix4f.class.cast(activeShaderProgram.getUniform("uBoneMatrices"))
				.update(AnimationData.DEFAULT_BONE_TRANSFORMS);
				/*activeShaderProgram.setMatrix4fArrayUniform(
					SceneRenderPass.U_BONE_MATRICES, AnimationData.DEFAULT_BONE_TRANSFORMS
				);*/
				
			} else {
				UAMatrix4f.class.cast(activeShaderProgram.getUniform("uBoneMatrices"))
				.update(animationData.getCurrentFrame().getBoneTransforms());
				/*activeShaderProgram.setMatrix4fArrayUniform(
					SceneRenderPass.U_BONE_MATRICES, animationData.getCurrentFrame().getBoneTransforms()
				);*/
			}

			GL46.glDrawElements(
				GL46.GL_TRIANGLES, vao.getVertexCount() * 3, GL46.GL_UNSIGNED_INT, 0
			);
		}
	}
}

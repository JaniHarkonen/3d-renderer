package project.opengl.scene;

import org.lwjgl.opengl.GL46;

import project.asset.sceneasset.AnimationData;
import project.asset.sceneasset.Mesh;
import project.asset.texture.Texture;
import project.component.Material;
import project.core.renderer.IRenderStrategy;
import project.core.renderer.IRenderer;
import project.opengl.Renderer;
import project.opengl.TextureGL;
import project.opengl.VAO;
import project.opengl.shader.ShaderProgram;
import project.scene.ASceneObject;
import project.scene.Model;

class RenderModel implements IRenderStrategy<SceneRenderPass> {

	@Override
	public void execute(IRenderer renderer, SceneRenderPass renderPass, ASceneObject target) {
		ShaderProgram activeShaderProgram = renderPass.shaderProgram;
		
		target.updateTransformMatrix();
		activeShaderProgram.setMatrix4fUniform(
			SceneRenderPass.U_OBJECT_TRANSFORM, target.getTransformMatrix()
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
			
			if( material.getTexture(1) != null && ((Renderer) renderer).getActiveScene().DEBUGareNormalsActive() ) {
				activeShaderProgram.setInteger1Uniform(
					SceneRenderPass.U_MATERIAL_HAS_NORMAL_MAP, 1
				);
			} else {
				activeShaderProgram.setInteger1Uniform(
					SceneRenderPass.U_MATERIAL_HAS_NORMAL_MAP, 0
				);
			}
			
			activeShaderProgram.setVector4fUniform(
				SceneRenderPass.U_MATERIAL_AMBIENT, material.getAmbientColor()
			);
			activeShaderProgram.setVector4fUniform(
				SceneRenderPass.U_MATERIAL_DIFFUSE, material.getDiffuseColor()
			);
			activeShaderProgram.setVector4fUniform(
				SceneRenderPass.U_MATERIAL_SPECULAR, material.getSpecularColor()
			);
			activeShaderProgram.setFloat1Uniform(
				SceneRenderPass.U_MATERIAL_REFLECTANCE, material.getReflectance()
			);
			
			VAO vao = (VAO) mesh.getGraphics();
			vao.bind();
			
			AnimationData animationData = model.getAnimationData();
			
			if( animationData == null ) {
				activeShaderProgram.setMatrix4fArrayUniform(
					SceneRenderPass.U_BONE_MATRICES, AnimationData.DEFAULT_BONE_TRANSFORMS
				);
				
			} else {
				activeShaderProgram.setMatrix4fArrayUniform(
					SceneRenderPass.U_BONE_MATRICES, animationData.getCurrentFrame().getBoneTransforms()
				);
			}

			GL46.glDrawElements(
				GL46.GL_TRIANGLES, vao.getVertexCount() * 3, GL46.GL_UNSIGNED_INT, 0
			);
		}
	}
}

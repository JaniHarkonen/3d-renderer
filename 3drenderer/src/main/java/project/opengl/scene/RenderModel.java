package project.opengl.scene;

import org.lwjgl.opengl.GL46;

import project.asset.sceneasset.Mesh;
import project.asset.texture.Texture;
import project.component.Material;
import project.core.renderer.IRenderStrategy;
import project.core.renderer.IRenderer;
import project.opengl.TextureGL;
import project.opengl.VAO;
import project.opengl.shader.ShaderProgram;
import project.opengl.shader.uniform.UAMatrix4f;
import project.opengl.shader.uniform.object.material.SSMaterial;
import project.opengl.shader.uniform.object.material.UMaterial;
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
		ShaderProgram activeShaderProgram = renderPass.shaderProgram;
		
		UAMatrix4f.class.cast(activeShaderProgram.getUniform(Uniforms.OBJECT_TRANSFORM))
		.update(target.getTransform().getAsMatrix());
		
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
			
			if( material.getTexture(1) != null && (boolean) renderPass.getGameState().getDebugData(TestDebugDataHandles.NORMALS_ACTIVE) ) {
				this.materialStruct.hasNormalMap = 1;
			} else {
				this.materialStruct.hasNormalMap = 0;
			}
			
			this.materialStruct.ambient = material.getAmbientColor();
			this.materialStruct.diffuse = material.getDiffuseColor();
			this.materialStruct.specular = material.getSpecularColor();
			this.materialStruct.reflectance = material.getReflectance();
			
			UMaterial.class.cast(activeShaderProgram.getUniform(Uniforms.MATERIAL)).update(this.materialStruct);
			
			VAO vao = (VAO) mesh.getGraphics();
			vao.bind();
			
			UAMatrix4f.class.cast(activeShaderProgram.getUniform(Uniforms.BONE_MATRICES))
			.update(model.getAnimator().getCurrentFrame().getBoneTransforms());

			GL46.glDrawElements(
				GL46.GL_TRIANGLES, vao.getVertexCount() * 3, GL46.GL_UNSIGNED_INT, 0
			);
		}
	}
}

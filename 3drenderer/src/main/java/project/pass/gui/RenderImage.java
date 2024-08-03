package project.pass.gui;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL46;

import project.Default;
import project.gui.Image;
import project.opengl.Renderer;
import project.opengl.TextureGL;
import project.opengl.VAO;
import project.pass.IRenderStrategy;
import project.scene.ASceneObject;
import project.shader.ShaderProgram;

public class RenderImage implements IRenderStrategy<GUIRenderPass> {

	@Override
	public void execute(Renderer renderer, GUIRenderPass renderPass, ASceneObject target) {
		ShaderProgram activeShaderProgram = renderPass.shaderProgram;
		Image element = (Image) target;
		
		TextureGL textureGL = (TextureGL) element.getTexture().getGraphics();
		Vector4f color = element.getPrimaryColor();
		
		activeShaderProgram.setVector4fUniform(GUIRenderPass.U_TEXT_COLOR, color);
		GL46.glActiveTexture(GL46.GL_TEXTURE0);
		textureGL.bind();

		activeShaderProgram.setMatrix4fUniform(
			GUIRenderPass.U_OBJECT_TRANSFORM, 
			new Matrix4f()
			.translationRotateScale(
				element.getPosition().x - element.getAnchor().x, 
				element.getPosition().y - element.getAnchor().y, 
				0.0f, 
				element.getRotation().x, 
				element.getRotation().y, 
				element.getRotation().z, 
				element.getRotation().w, 
				1.0f, 1.0f, 1.0f
			)
		);
		
		//VAO vao = vaoCache.getOrGenerate(GUIRenderPass.IMAGE_PLANE);
		//vao.bind();
		VAO vao = (VAO) renderPass.imagePlane.getGraphics();//GUIRenderPass.IMAGE_PLANE.getGraphics();
		
		if( vao == null ) {
			vao = (VAO) Default.MESH.getGraphics();
		}
		
		vao.bind();
		GL46.glDrawElements(
			GL46.GL_TRIANGLES, 
			vao.getVertexCount() * 3, 
			GL46.GL_UNSIGNED_INT, 
			0
		);
	}
}

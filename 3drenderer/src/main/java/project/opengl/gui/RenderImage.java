package project.opengl.gui;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL46;

import project.core.renderer.IRenderStrategy;
import project.core.renderer.IRenderer;
import project.gui.Image;
import project.opengl.TextureGL;
import project.opengl.VAO;
import project.opengl.shader.ShaderProgram;
import project.opengl.shader.uniform.UAMatrix4f;
import project.opengl.shader.uniform.UVector4f;
import project.scene.ASceneObject;

public class RenderImage implements IRenderStrategy<GUIRenderPass> {

	@Override
	public void execute(IRenderer renderer, GUIRenderPass renderPass, ASceneObject target) {
		ShaderProgram activeShaderProgram = renderPass.shaderProgram;
		Image element = (Image) target;
		
		TextureGL textureGL = (TextureGL) element.getTexture().getGraphics();
		Vector4f color = element.getPrimaryColor();
		
		UVector4f.class.cast(activeShaderProgram.getUniform("uTextColor")).update(color);
		GL46.glActiveTexture(GL46.GL_TEXTURE0);
		textureGL.bind();
		UAMatrix4f.class.cast(activeShaderProgram.getUniform("uObjectTransform"))
		.update(
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
		
		VAO vao = (VAO) renderPass.imagePlane.getGraphics();
		vao.bind();
		GL46.glDrawElements(
			GL46.GL_TRIANGLES, 
			vao.getVertexCount() * 3, 
			GL46.GL_UNSIGNED_INT, 
			0
		);
	}
}

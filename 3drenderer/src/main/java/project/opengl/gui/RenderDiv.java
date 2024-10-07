package project.opengl.gui;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL46;

import project.core.IRenderable;
import project.core.renderer.IRenderStrategy;
import project.core.renderer.IRenderer;
import project.opengl.vao.VAO;

public class RenderDiv implements IRenderStrategy<GUIRenderPass> {

	@Override
	public void execute(IRenderer renderer, GUIRenderPass renderPass, IRenderable renderable) {
		StyleCascade renderContext = renderPass.context;
		Vector4f backgroundColor = renderContext.secondaryColor;
		
			// Skip rendering if the div's alpha value is too low
			// NOTICE: The content MAY still be rendered if children's primary colors aren't inherited
		if( backgroundColor.w <= 0.00001f ) {
			return;
		}
		
		renderPass.uPrimaryColor.update(backgroundColor);
		
		float x = renderContext.left - renderContext.anchorX;
		float y = renderContext.top - renderContext.anchorY;
		float width = renderContext.width;
		float height = renderContext.height;
		
		Matrix4f transform = new Matrix4f()
		.translationRotateScale(x, y, 0.0f, 0, 0, 0, 0, width, height, 1.0f);
		renderPass.uObjectTransform.update(transform);
		renderPass.uHasTexture.update(0);
		
		VAO vao = (VAO) renderPass.imagePlane.getGraphics();
		vao.bind();
		GL46.glDrawElements(GL46.GL_TRIANGLES, vao.getVertexCount() * 3, GL46.GL_UNSIGNED_INT, 0);
	}
}

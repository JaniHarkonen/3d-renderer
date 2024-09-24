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
		Context renderContext = renderPass.context;
		
		Vector4f primaryColor = renderContext.primaryColor;
		renderPass.uPrimaryColor.update(primaryColor);
		
		float x = renderContext.left;
		float y = renderContext.top;
		float width = renderContext.width;
		float height = renderContext.height;
		
		Matrix4f transform = new Matrix4f()
		.translationRotateScale(x, y, 0.0f, 0, 0, 0, 0, width, height, 1.0f);
		renderPass.uObjectTransform.update(transform);
		
		VAO vao = (VAO) renderPass.imagePlane.getGraphics();
		vao.bind();
		GL46.glDrawElements(GL46.GL_TRIANGLES, vao.getVertexCount() * 3, GL46.GL_UNSIGNED_INT, 0);
	}
}

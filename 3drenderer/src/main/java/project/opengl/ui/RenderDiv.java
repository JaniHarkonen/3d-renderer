package project.opengl.ui;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL46;

import project.core.renderer.IRenderStrategy;
import project.core.renderer.IRenderer;
import project.opengl.vao.VAO;
import project.ui.AUIElement;
import project.ui.props.Properties;

public class RenderDiv implements IRenderStrategy<UIRenderPass, AUIElement> {

	@Override
	public void execute(IRenderer renderer, UIRenderPass renderPass, AUIElement element) {
		Properties.Statistics stats = element.getStatistics();
		Vector4f backgroundColor = stats.backgroundColor;
		
			// Skip rendering if the div's alpha value is too low
			// NOTICE: The content MAY still be rendered if children's primary colors aren't inherited
		if( backgroundColor.w <= 0.00001f ) {
			return;
		}
		
		renderPass.uColor.update(backgroundColor);
		
		float x = stats.left - stats.anchorX;
		float y = stats.top - stats.anchorY;
		float width = stats.width;
		float height = stats.height;
		
		Matrix4f transform = new Matrix4f()
		.translationRotateScale(x, y, 0.0f, 0, 0, 0, 0, width, height, 1.0f);
		renderPass.uObjectTransform.update(transform);
		renderPass.uHasTexture.update(0);
		
		VAO vao = (VAO) renderPass.imagePlane.getGraphics();
		vao.bind();
		GL46.glDrawElements(GL46.GL_TRIANGLES, vao.getVertexCount() * 3, GL46.GL_UNSIGNED_INT, 0);
	}
}

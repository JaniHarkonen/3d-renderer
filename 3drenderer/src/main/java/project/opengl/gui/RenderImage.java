package project.opengl.gui;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL46;

import project.core.renderer.IRenderStrategy;
import project.core.renderer.IRenderer;
import project.gui.AGUIElement;
import project.gui.Image;
import project.gui.props.Properties;
import project.opengl.TextureGL;
import project.opengl.vao.VAO;

public class RenderImage implements IRenderStrategy<GUIRenderPass, AGUIElement> {

	@Override
	public void execute(IRenderer renderer, GUIRenderPass renderPass, AGUIElement element) {
		Image image = (Image) element;
		Properties.Statistics stats = element.getStatistics();
		TextureGL textureGL = (TextureGL) image.getTexture().getGraphics();
		Vector4f primaryColor = stats.primaryColor;
		
		renderPass.uPrimaryColor.update(primaryColor);
		renderPass.uHasTexture.update(1);
		GL46.glActiveTexture(GL46.GL_TEXTURE0);
		textureGL.bind();
		
		float x = stats.left;
		float y = stats.top;
		float width = stats.width;
		float height = stats.height;
		float anchorX = stats.anchorX;
		float anchorY = stats.anchorY;
		
		Matrix4f transform = new Matrix4f()
		.translationRotateScale(x - anchorX, y - anchorY, 0.0f, 0, 0, 0, 0, width, height, 1.0f);
		renderPass.uObjectTransform.update(transform);
		
		VAO vao = (VAO) renderPass.imagePlane.getGraphics();
		vao.bind();
		GL46.glDrawElements(GL46.GL_TRIANGLES, vao.getVertexCount() * 3, GL46.GL_UNSIGNED_INT, 0);
	}
}

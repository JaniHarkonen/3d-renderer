package project.opengl.ui;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL46;

import project.asset.font.Font;
import project.core.renderer.IRenderStrategy;
import project.core.renderer.IRenderer;
import project.opengl.TextureGL;
import project.opengl.vao.VAO;
import project.ui.AUIElement;
import project.ui.Text;
import project.ui.props.Properties;

public class RenderText implements IRenderStrategy<UIRenderPass, AUIElement> {

	@Override
	public void execute(IRenderer renderer, UIRenderPass renderPass, AUIElement element) {
		Properties.Statistics stats = element.getStatistics();
		float textX = stats.left;
		float textY = stats.top;
		Text text = (Text) element;
		Font font = text.getFont();
		TextureGL textureGL = (TextureGL) font.getTexture().getGraphics();
		Vector4f color = stats.color;
		
		renderPass.uColor.update(color);
		renderPass.uHasTexture.update(1);
		GL46.glActiveTexture(GL46.GL_TEXTURE0);
		textureGL.bind();
		
		for( String line : text.getContent().split("\n") ) {
			for( int i = 0; i < line.length(); i++ ) {
				Font.Glyph glyph = font.getGlyph(line.charAt(i));
				renderPass.uObjectTransform.update(
					new Matrix4f()
					.translationRotateScale(
						textX, textY + stats.baseline - glyph.getOriginY(), 0.0f,
						0, 0, 0, 0,
						1.0f, 1.0f, 1.0f
					)
				);
				
				VAO vao = (VAO) glyph.getMesh().getGraphics();
				vao.bind();
				GL46.glDrawElements(
					GL46.GL_TRIANGLES, 
					vao.getVertexCount() * 3, 
					GL46.GL_UNSIGNED_INT, 
					0
				);
				
				textX += glyph.getWidth();
			}
			
			textX = 0.0f;
			textY += stats.lineHeight;
		}
	}
}

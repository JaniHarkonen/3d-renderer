package project.pass.gui;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL46;

import project.Default;
import project.asset.Font;
import project.gui.Text;
import project.opengl.Renderer;
import project.opengl.TextureGL;
import project.opengl.VAO;
import project.pass.IRenderStrategy;
import project.scene.ASceneObject;
import project.shader.ShaderProgram;

public class RenderText implements IRenderStrategy<GUIRenderPass> {

	@Override
	public void execute(Renderer renderer, GUIRenderPass renderPass, ASceneObject target) {
		ShaderProgram activeShaderProgram = renderPass.shaderProgram;
		Text element = (Text) target;
		
		float textX = element.getPosition().x;
		float textY = element.getPosition().y;
		Text text = (Text) element;
		Font font = text.getFont();
		TextureGL textureGL = (TextureGL) font.getTexture().getGraphics();
		Vector4f color = text.getTextColor();
		
		activeShaderProgram.setVector4fUniform(GUIRenderPass.U_TEXT_COLOR, color);
		GL46.glActiveTexture(GL46.GL_TEXTURE0);
		textureGL.bind();
		//texture.bind();

		for( String line : text.getContent().split("\n") ) {
			for( int i = 0; i < line.length(); i++ ) {
				Font.Glyph glyph = font.getGlyph(line.charAt(i));
				activeShaderProgram.setMatrix4fUniform(
					GUIRenderPass.U_OBJECT_TRANSFORM, 
					new Matrix4f()
					.translationRotateScale(
						textX, textY + renderPass.baseLine - glyph.getOriginY(), 0.0f, 
						element.getRotation().x, 
						element.getRotation().y, 
						element.getRotation().z, 
						element.getRotation().w, 
						1.0f, 1.0f, 1.0f
					)
				);
				
				//VAO vao = vaoCache.getOrGenerate(glyph.getMesh());
				VAO vao = (VAO) glyph.getMesh().getGraphics();
				
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
				
				textX += glyph.getWidth();
			}
			
			textX = 0.0f;
			textY += renderPass.lineHeight;
		}
	}
}

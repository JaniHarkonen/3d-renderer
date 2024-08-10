package project.opengl.gui;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL46;

import project.asset.font.Font;
import project.component.Transform;
import project.core.renderer.IRenderStrategy;
import project.core.renderer.IRenderer;
import project.gui.Text;
import project.opengl.TextureGL;
import project.opengl.VAO;
import project.opengl.shader.ShaderProgram;
import project.opengl.shader.uniform.UAMatrix4f;
import project.opengl.shader.uniform.UVector4f;
import project.scene.ASceneObject;

public class RenderText implements IRenderStrategy<GUIRenderPass> {

	@Override
	public void execute(IRenderer renderer, GUIRenderPass renderPass, ASceneObject target) {
		ShaderProgram activeShaderProgram = renderPass.shaderProgram;
		Text element = (Text) target;
		
		Transform transform = element.getTransformComponent();
		Vector3f position = transform.getPosition();
		Quaternionf rotation = transform.getRotationComponent().getAsQuaternion();
		
		float textX = position.x;
		float textY = position.y;
		Text text = (Text) element;
		Font font = text.getFont();
		TextureGL textureGL = (TextureGL) font.getTexture().getGraphics();
		Vector4f primaryColor = text.getPrimaryColor();
		
		UVector4f.class.cast(activeShaderProgram.getUniform(Uniforms.PRIMARY_COLOR)).update(primaryColor);
		GL46.glActiveTexture(GL46.GL_TEXTURE0);
		textureGL.bind();

		for( String line : text.getContent().split("\n") ) {
			for( int i = 0; i < line.length(); i++ ) {
				Font.Glyph glyph = font.getGlyph(line.charAt(i));
				UAMatrix4f.class.cast(activeShaderProgram.getUniform(Uniforms.OBJECT_TRANSFORM))
				.update(
					new Matrix4f()
					.translationRotateScale(
						textX, textY + renderPass.baseLine - glyph.getOriginY(), 0.0f, 
						rotation.x, 
						rotation.y, 
						rotation.z, 
						rotation.w, 
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
			textY += renderPass.lineHeight;
		}
	}
}

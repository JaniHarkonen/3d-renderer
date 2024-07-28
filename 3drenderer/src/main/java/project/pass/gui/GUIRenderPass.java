package project.pass.gui;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL46;

import project.asset.Font;
import project.gui.AGUIElement;
import project.gui.Text;
import project.opengl.Renderer;
import project.opengl.Texture;
import project.opengl.TextureCache;
import project.opengl.VAO;
import project.opengl.VAOCache;
import project.pass.IRenderPass;
import project.scene.Scene;
import project.shader.Shader;
import project.shader.ShaderProgram;

public class GUIRenderPass implements IRenderPass {
	private static final String U_PROJECTION = "uProjection";
	private static final String U_DIFFUSE_SAMPLER= "uDiffuseSampler";
	private static final String U_OBJECT_TRANSFORM = "uObjectTransform";
	private static final String U_TEXT_COLOR = "uTextColor";

	private ShaderProgram shaderProgram;
	
	public GUIRenderPass() {
		this.shaderProgram = new ShaderProgram();
	}
	
	@Override
	public boolean init() {
		this.shaderProgram = new ShaderProgram();
		this.shaderProgram.declareUniform(U_PROJECTION);
		this.shaderProgram.declareUniform(U_DIFFUSE_SAMPLER);
		this.shaderProgram.declareUniform(U_OBJECT_TRANSFORM);
		this.shaderProgram.declareUniform(U_TEXT_COLOR);
		this.shaderProgram.addShader(
			new Shader("shaders/gui/gui.vert", GL46.GL_VERTEX_SHADER)
		);
		this.shaderProgram.addShader(
			new Shader("shaders/gui/gui.frag", GL46.GL_FRAGMENT_SHADER)
		);
		this.shaderProgram.init();
		
		return true;
	}

	@Override
	public void render(Renderer renderer) {
		Scene scene = renderer.getActiveScene();
		ShaderProgram activeShaderProgram = this.shaderProgram;
		VAOCache vaoCache = renderer.getVAOCache();
		TextureCache textureCache = renderer.getTextureCache();
		
        activeShaderProgram.bind();
        activeShaderProgram.setInteger1Uniform(U_DIFFUSE_SAMPLER, 0);
        activeShaderProgram.setMatrix4fUniform(
			U_PROJECTION, 
			scene.getGUI().calculateAndGetProjection()
		);
		
		float lineHeight = 22.0f;
		float baseLine = 16.0f;
		for( AGUIElement element : scene.getGUI().getElements() ) {
			
				// Determine the appropriate way of rendering the element
				// (THIS MUST BE CHANGED TO A MORE DYNAMIC APPROACH)			
			if( element instanceof Text ) {
				float textX = element.getPosition().x;
				float textY = element.getPosition().y;
				Text text = (Text) element;
				Font font = text.getFont();
				Texture texture = font.getTexture();
				Vector4f color = text.getTextColor();
				
				activeShaderProgram.setVector4fUniform(U_TEXT_COLOR, color);
				textureCache.generateIfNotEncountered(texture);
				GL46.glActiveTexture(GL46.GL_TEXTURE0);
				texture.bind();

				for( String line : text.getContent().split("\n") ) {
					for( int i = 0; i < line.length(); i++ ) {
						Font.Glyph glyph = font.getGlyph(line.charAt(i));
						activeShaderProgram.setMatrix4fUniform(
							U_OBJECT_TRANSFORM, 
							new Matrix4f()
							.translationRotateScale(
								textX, textY + baseLine - glyph.getOriginY(), 0.0f, 
								element.getRotation().x, 
								element.getRotation().y, 
								element.getRotation().z, 
								element.getRotation().w, 
								1.0f, 1.0f, 1.0f
							)
						);
						
						VAO vao = vaoCache.getOrGenerate(glyph.getMesh());	
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
					textY += lineHeight;
				}
			}
		}
		
		activeShaderProgram.unbind();
	}
}

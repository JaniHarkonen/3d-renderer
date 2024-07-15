package project.opengl;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL46;

import project.Window;
import project.asset.Font;
import project.asset.Mesh;
import project.gui.AGUIElement;
import project.gui.Text;
import project.scene.ASceneObject;
import project.scene.Camera;
import project.scene.Model;
import project.scene.Scene;
import project.shader.Shader;
import project.shader.ShaderProgram;

public class Renderer {
	
		// Uniform names
	private static final String U_DIFFUSE_SAMPLER = "uDiffuseSampler";
	private static final String U_PROJECTION = "uProjection";
	private static final String U_CAMERA_TRANSFORM = "uCameraTransform";
	private static final String U_OBJECT_TRANSFORM = "uObjectTransform";
	
	private static final String U_PROJECTION_GUI = "uProjection";
	private static final String U_DIFFUSE_SAMPLER_GUI = "uDiffuseSampler";
	private static final String U_OBJECT_TRANSFORM_GUI = "uObjectTransform";
	private static final String U_TEXT_COLOR_GUI = "uTextColor";
	
	private Window clientWindow;
	
	private ShaderProgram shaderProgram;
	private ShaderProgram shaderProgramGUI;
	
	private VAOCache vaoCache;
	private TextureCache textureCache;
	private Scene scene;
	
	public Renderer(Window clientWindow, Scene scene) {
		this.clientWindow = clientWindow;
		this.shaderProgram = null;
		this.shaderProgramGUI = null;
		this.vaoCache = null;
		this.textureCache = null;
		this.scene = scene;
	}

	public void init() {
		GL.createCapabilities();
		GL46.glClearColor(0.85f, 0.85f, 0.85f, 0.0f);
		
			// Scene shaders
		this.shaderProgram = new ShaderProgram();
		this.shaderProgram.declareUniform(Renderer.U_DIFFUSE_SAMPLER);
		this.shaderProgram.declareUniform(Renderer.U_PROJECTION);
		this.shaderProgram.declareUniform(Renderer.U_CAMERA_TRANSFORM);
		this.shaderProgram.declareUniform(Renderer.U_OBJECT_TRANSFORM);
		this.shaderProgram.addShader(new Shader("shaders/scene/scene.vert", GL46.GL_VERTEX_SHADER));
		this.shaderProgram.addShader(new Shader("shaders/scene/scene.frag", GL46.GL_FRAGMENT_SHADER));
		this.shaderProgram.init();
		
			// GUI shaders
		this.shaderProgramGUI = new ShaderProgram();
		this.shaderProgramGUI.declareUniform(Renderer.U_PROJECTION_GUI);
		this.shaderProgramGUI.declareUniform(Renderer.U_DIFFUSE_SAMPLER_GUI);
		this.shaderProgramGUI.declareUniform(Renderer.U_OBJECT_TRANSFORM_GUI);
		this.shaderProgramGUI.declareUniform(Renderer.U_TEXT_COLOR_GUI);
		this.shaderProgramGUI.addShader(new Shader("shaders/gui/gui.vert", GL46.GL_VERTEX_SHADER));
		this.shaderProgramGUI.addShader(new Shader("shaders/gui/gui.frag", GL46.GL_FRAGMENT_SHADER));
		this.shaderProgramGUI.init();
		
			// Initialize scene graphics assets
		this.scene.init();
		this.vaoCache = new VAOCache();
		this.textureCache = new TextureCache();
	}
		
	public void render() {
		GL46.glClear(GL46.GL_COLOR_BUFFER_BIT | GL46.GL_DEPTH_BUFFER_BIT);
		
			/////////////////////////////////// Scene render pass ///////////////////////////////////
		GL46.glEnable(GL46.GL_DEPTH_TEST);
		GL46.glDisable(GL46.GL_BLEND);
		this.shaderProgram.bind();
		this.shaderProgram.setInteger1Uniform(Renderer.U_DIFFUSE_SAMPLER, 0);
		
		Camera activeCamera = this.scene.getActiveCamera();
		activeCamera.getProjection().update(
			this.clientWindow.getWidth(), this.clientWindow.getHeight()
		);
		
		this.shaderProgram.setMatrix4fUniform(
			Renderer.U_PROJECTION, activeCamera.getProjection().getMatrix()
		);
		
		this.shaderProgram.setMatrix4fUniform(
			Renderer.U_CAMERA_TRANSFORM, activeCamera.getCameraTransform()
		);
			
			// Width and height are not yet updated when resizing the window
		GL46.glViewport(
			0, 0, this.clientWindow.getWidth(), this.clientWindow.getHeight()
		);
		
		for( ASceneObject object : this.scene.getObjects() ) {
				// Determine the appropriate way of rendering the object
				// (THIS MUST BE CHANGED TO A MORE DYNAMIC APPROACH)
			if( object instanceof Model ) {
				object.updateTransformMatrix();
				this.shaderProgram.setMatrix4fUniform(
					Renderer.U_OBJECT_TRANSFORM, object.getTransformMatrix()
				);
				
				Model model = (Model) object;
				Texture texture = model.getTexture();
				Mesh mesh = model.getMesh();
				
				this.textureCache.generateIfNotEncountered(texture);
				GL46.glActiveTexture(GL46.GL_TEXTURE0);
				texture.bind();
				
				VAO vao = this.vaoCache.getOrGenerate(mesh);
				vao.bind();
				GL46.glDrawElements(
					GL46.GL_TRIANGLES, vao.getVertexCount() * 3, GL46.GL_UNSIGNED_INT, 0
				);
			}
		}
		
		this.shaderProgram.unbind();
	
		
			/////////////////////////////////// GUI render pass ///////////////////////////////////
		GL46.glDisable(GL46.GL_DEPTH_TEST);
		GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendFunc(GL46.GL_SRC_ALPHA, GL46.GL_ONE_MINUS_SRC_ALPHA);
		this.shaderProgramGUI.bind();
		this.shaderProgramGUI.setInteger1Uniform(Renderer.U_DIFFUSE_SAMPLER, 0);
		
		this.shaderProgramGUI.setMatrix4fUniform(
			Renderer.U_PROJECTION_GUI, 
			this.scene.getGUI().calculateAndGetProjection()
		);
		
		float lineHeight = 22.0f;
		float baseLine = 16.0f;
		for( AGUIElement element : this.scene.getGUI().getElements() ) {
			
				// Determine the appropriate way of rendering the element
				// (THIS MUST BE CHANGED TO A MORE DYNAMIC APPROACH)
			if( element instanceof Text ) {
				float textX = element.getPosition().x;
				float textY = element.getPosition().y;
				Text text = (Text) element;
				Font font = text.getFont();
				Texture texture = font.getTexture();
				Vector4f color = text.getTextColor();
				
				this.shaderProgramGUI.setVector4fUniform(Renderer.U_TEXT_COLOR_GUI, color);
				this.textureCache.generateIfNotEncountered(texture);
				GL46.glActiveTexture(GL46.GL_TEXTURE0);
				texture.bind();

				for( String line : text.getContent().split("\n") ) {
					for( int i = 0; i < line.length(); i++ ) {
						Font.Glyph glyph = font.getGlyph(line.charAt(i));
						this.shaderProgramGUI.setMatrix4fUniform(
							Renderer.U_OBJECT_TRANSFORM_GUI, 
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
						
						VAO vao = this.vaoCache.getOrGenerate(glyph.getMesh());	
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
		
		this.shaderProgramGUI.unbind();
	}
	
	public Window getClientWindow() {
		return this.clientWindow;
	}
}

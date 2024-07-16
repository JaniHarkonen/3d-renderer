package project.opengl;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL46;

import project.Window;
import project.asset.Font;
import project.asset.Mesh;
import project.component.Attenuation;
import project.component.Material;
import project.gui.AGUIElement;
import project.gui.Text;
import project.scene.ASceneObject;
import project.scene.AmbientLight;
import project.scene.Camera;
import project.scene.Model;
import project.scene.PointLight;
import project.scene.Scene;
import project.shader.Shader;
import project.shader.ShaderProgram;
import project.utils.DebugUtils;

public class Renderer {
	
		// Uniform names
	private static final String U_DIFFUSE_SAMPLER = "uDiffuseSampler";
	private static final String U_PROJECTION = "uProjection";
	private static final String U_CAMERA_TRANSFORM = "uCameraTransform";
	private static final String U_OBJECT_TRANSFORM = "uObjectTransform";
	
	private static final String U_MATERIAL_AMBIENT = "uMaterial.ambient";
	private static final String U_MATERIAL_DIFFUSE = "uMaterial.diffuse";
	private static final String U_MATERIAL_SPECULAR = "uMaterial.specular";
	private static final String U_MATERIAL_REFLECTANCE = "uMaterial.reflectance";
	private static final String U_AMBIENT_LIGHT_FACTOR = "uAmbientLight.factor";
	private static final String U_AMBIENT_LIGHT_COLOR = "uAmbientLight.color";
	private static final String U_POINT_LIGHTS = "uPointLights";
	
		// Spot light uniform names here
	
	
	private static final String U_PROJECTION_GUI = "uProjection";
	private static final String U_DIFFUSE_SAMPLER_GUI = "uDiffuseSampler";
	private static final String U_OBJECT_TRANSFORM_GUI = "uObjectTransform";
	private static final String U_TEXT_COLOR_GUI = "uTextColor";
	
		// Light settings
	private static final int MAX_POINT_LIGHTS = 5;
    //private static final int MAX_SPOT_LIGHTS = 5; // UNUSED AS OF NOW
	
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
		//GL46.glClearColor(0.85f, 0.85f, 0.85f, 0.0f);
		GL46.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
			// Scene shaders
		this.shaderProgram = new ShaderProgram();
		this.shaderProgram.declareUniform(Renderer.U_DIFFUSE_SAMPLER);
		this.shaderProgram.declareUniform(Renderer.U_PROJECTION);
		this.shaderProgram.declareUniform(Renderer.U_CAMERA_TRANSFORM);
		this.shaderProgram.declareUniform(Renderer.U_OBJECT_TRANSFORM);
		
		this.shaderProgram.declareUniform(Renderer.U_MATERIAL_AMBIENT);
		this.shaderProgram.declareUniform(Renderer.U_MATERIAL_DIFFUSE);
		this.shaderProgram.declareUniform(Renderer.U_MATERIAL_SPECULAR);
		this.shaderProgram.declareUniform(Renderer.U_MATERIAL_REFLECTANCE);
		this.shaderProgram.declareUniform(Renderer.U_AMBIENT_LIGHT_FACTOR);
		this.shaderProgram.declareUniform(Renderer.U_AMBIENT_LIGHT_COLOR);
		
		for( int i = 0; i < Renderer.MAX_POINT_LIGHTS; i++ ) {
			this.shaderProgram.declareUniform(Renderer.U_POINT_LIGHTS + "[" + i + "].position");
			this.shaderProgram.declareUniform(Renderer.U_POINT_LIGHTS + "[" + i + "].color");
			this.shaderProgram.declareUniform(Renderer.U_POINT_LIGHTS + "[" + i + "].intensity");
			this.shaderProgram.declareUniform(Renderer.U_POINT_LIGHTS + "[" + i + "].att.constant");
			this.shaderProgram.declareUniform(Renderer.U_POINT_LIGHTS + "[" + i + "].att.linear");
			this.shaderProgram.declareUniform(Renderer.U_POINT_LIGHTS + "[" + i + "].att.exponent");
		}

			// Spot light uniform declarations here
			
		this.shaderProgram.addShader(new Shader("shaders/scene/scene.vert", GL46.GL_VERTEX_SHADER));
		this.shaderProgram.addShader(new Shader("shaders/scene/scene.frag", GL46.GL_FRAGMENT_SHADER));
		this.shaderProgram.init();
		
			// Set point light uniforms to default values, RIGHT NOW LIGHTS CANNOT BE REMOVED 
		for( int i = 0; i < Renderer.MAX_POINT_LIGHTS; i++ ) {
			this.shaderProgram.setVector3fUniform(Renderer.U_POINT_LIGHTS + "[" + i + "].position", new Vector3f(0.0f));
			this.shaderProgram.setVector3fUniform(Renderer.U_POINT_LIGHTS + "[" + i + "].color", new Vector3f(0.0f));
			this.shaderProgram.setFloat1Uniform(Renderer.U_POINT_LIGHTS + "[" + i + "].intensity", 0.0f);
			this.shaderProgram.setFloat1Uniform(Renderer.U_POINT_LIGHTS + "[" + i + "].att.constant", 0.0f);
			this.shaderProgram.setFloat1Uniform(Renderer.U_POINT_LIGHTS + "[" + i + "].att.linear", 0.0f);
			this.shaderProgram.setFloat1Uniform(Renderer.U_POINT_LIGHTS + "[" + i + "].att.exponent", 0.0f);
		}
		
			// Spot light uniform default value settings here
		
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
	
	private void updatePointLight(PointLight pointLight, int index) {
        Vector4f aux = new Vector4f();
        /*Vector3f lightPosition = new Vector3f();
        Vector3f color = new Vector3f();
        float intensity = 0.0f;
        float constant = 0.0f;
        float linear = 0.0f;
        float exponent = 0.0f;*/
        
        Matrix4f cameraTransform = this.scene.getActiveCamera().getCameraTransform();
        aux.set(pointLight.getPosition(), 1);
        aux.mul(cameraTransform);
        
        Vector3f lightPosition = new Vector3f();
        lightPosition.set(aux.x, aux.y, aux.z);
        
        Vector3f color = new Vector3f();
        color.set(pointLight.getColor());
        
        Attenuation attenuation = pointLight.getAttenuation();
        float intensity = pointLight.getIntensity();
        float constant = attenuation.getConstant();
        float linear = attenuation.getLinear();
        float exponent = attenuation.getExponent();
        
		this.shaderProgram.setVector3fUniform(Renderer.U_POINT_LIGHTS + "[" + index + "].position", lightPosition);
		this.shaderProgram.setVector3fUniform(Renderer.U_POINT_LIGHTS + "[" + index + "].color", color);
		this.shaderProgram.setFloat1Uniform(Renderer.U_POINT_LIGHTS + "[" + index + "].intensity", intensity);
		this.shaderProgram.setFloat1Uniform(Renderer.U_POINT_LIGHTS + "[" + index + "].att.constant", constant);
		this.shaderProgram.setFloat1Uniform(Renderer.U_POINT_LIGHTS + "[" + index + "].att.linear", linear);
		this.shaderProgram.setFloat1Uniform(Renderer.U_POINT_LIGHTS + "[" + index + "].att.exponent", exponent);
        /*uniformsMap.setUniform(prefix + ".position", lightPosition);
        uniformsMap.setUniform(prefix + ".color", color);
        uniformsMap.setUniform(prefix + ".intensity", intensity);
        uniformsMap.setUniform(prefix + ".att.constant", constant);
        uniformsMap.setUniform(prefix + ".att.linear", linear);
        uniformsMap.setUniform(prefix + ".att.exponent", exponent);*/
	}
	
		// UNUSED AS OF NOW
	/*
    private void updateSpotLight() {
        PointLight pointLight = null;
        Vector3f coneDirection = new Vector3f();
        float cutoff = 0.0f;
        if (spotLight != null) {
            coneDirection = spotLight.getConeDirection();
            cutoff = spotLight.getCutOff();
            pointLight = spotLight.getPointLight();
        }

        uniformsMap.setUniform(prefix + ".conedir", coneDirection);
        uniformsMap.setUniform(prefix + ".cutoff", cutoff);
        updatePointLight(pointLight, prefix + ".pl", viewMatrix);
    }*/
		
	public void render() {
		GL46.glClear(GL46.GL_COLOR_BUFFER_BIT | GL46.GL_DEPTH_BUFFER_BIT);
		ShaderProgram activeShaderProgram;
		
			/////////////////////////////////// Scene render pass ///////////////////////////////////
		GL46.glEnable(GL46.GL_DEPTH_TEST);
		GL46.glDisable(GL46.GL_BLEND);
		activeShaderProgram = this.shaderProgram;
		activeShaderProgram.bind();
		activeShaderProgram.setInteger1Uniform(Renderer.U_DIFFUSE_SAMPLER, 0);
		
		Camera activeCamera = this.scene.getActiveCamera();
		activeCamera.getProjection().update(
			this.clientWindow.getWidth(), this.clientWindow.getHeight()
		);
		
		activeShaderProgram.setMatrix4fUniform(
			Renderer.U_PROJECTION, activeCamera.getProjection().getMatrix()
		);
		
		activeShaderProgram.setMatrix4fUniform(
			Renderer.U_CAMERA_TRANSFORM, activeCamera.getCameraTransform()
		);
			
			// Width and height are not yet updated when resizing the window
		GL46.glViewport(
			0, 0, this.clientWindow.getWidth(), this.clientWindow.getHeight()
		);
		
		for( ASceneObject object : this.scene.getObjects() ) {
				// Determine the appropriate way of rendering the object
				// (THIS MUST BE CHANGED TO A MORE DYNAMIC APPROACH)
			if( object instanceof AmbientLight ) {
				AmbientLight ambientLight = (AmbientLight) object;
				activeShaderProgram.setFloat1Uniform(
					Renderer.U_AMBIENT_LIGHT_FACTOR, ambientLight.getIntensity()
				);
				activeShaderProgram.setVector3fUniform(
					Renderer.U_AMBIENT_LIGHT_COLOR, ambientLight.getColor()
				);
			} else if( object instanceof PointLight ) {
					// WARNING! Using default point light index 0 here, other point lights are not calculated as of now
				this.updatePointLight((PointLight) object, 0);
			} else if( object instanceof Model ) {
				object.updateTransformMatrix();
				activeShaderProgram.setMatrix4fUniform(
					Renderer.U_OBJECT_TRANSFORM, object.getTransformMatrix()
				);
				
				Model model = (Model) object;
				Material material = model.getMaterial();
				Mesh mesh = model.getMesh();
				
				for( int i = 0; i < material.getTextures().length; i++ ) {
					Texture texture = material.getTextures()[i];
					if( texture == null ) {
						DebugUtils.log(this, "WARNING: Rejected texture indexed at " + i + "!");
						continue;
					}
					
					this.textureCache.generateIfNotEncountered(texture);
					GL46.glActiveTexture(GL46.GL_TEXTURE0 + i);
					texture.bind();
				}
				
				activeShaderProgram.setVector4fUniform(
					Renderer.U_MATERIAL_AMBIENT, material.getAmbientColor()
				);
				activeShaderProgram.setVector4fUniform(
					Renderer.U_MATERIAL_DIFFUSE, material.getDiffuseColor()
				);
				activeShaderProgram.setVector4fUniform(
					Renderer.U_MATERIAL_SPECULAR, material.getSpecularColor()
				);
				activeShaderProgram.setFloat1Uniform(
					Renderer.U_MATERIAL_REFLECTANCE, material.getReflectance()
				);
				
				VAO vao = this.vaoCache.getOrGenerate(mesh);
				vao.bind();
				GL46.glDrawElements(
					GL46.GL_TRIANGLES, vao.getVertexCount() * 3, GL46.GL_UNSIGNED_INT, 0
				);
			}
		}
		
		activeShaderProgram.unbind();
	
		
			/////////////////////////////////// GUI render pass ///////////////////////////////////
		GL46.glDisable(GL46.GL_DEPTH_TEST);
		GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendFunc(GL46.GL_SRC_ALPHA, GL46.GL_ONE_MINUS_SRC_ALPHA);
        activeShaderProgram = this.shaderProgramGUI;
        activeShaderProgram.bind();
        activeShaderProgram.setInteger1Uniform(Renderer.U_DIFFUSE_SAMPLER, 0);
		
        activeShaderProgram.setMatrix4fUniform(
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
				
				activeShaderProgram.setVector4fUniform(Renderer.U_TEXT_COLOR_GUI, color);
				this.textureCache.generateIfNotEncountered(texture);
				GL46.glActiveTexture(GL46.GL_TEXTURE0);
				texture.bind();

				for( String line : text.getContent().split("\n") ) {
					for( int i = 0; i < line.length(); i++ ) {
						Font.Glyph glyph = font.getGlyph(line.charAt(i));
						activeShaderProgram.setMatrix4fUniform(
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
		
		activeShaderProgram.unbind();
	}
	
	public Window getClientWindow() {
		return this.clientWindow;
	}
}

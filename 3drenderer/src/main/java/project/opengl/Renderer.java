package project.opengl;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL46;

import project.Window;
import project.asset.AnimationData;
import project.asset.Font;
import project.asset.Mesh;
import project.component.Attenuation;
import project.component.CascadeShadow;
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
	private static final String U_NORMAL_SAMPLER = "uNormalSampler";
	private static final String U_PROJECTION = "uProjection";
	private static final String U_CAMERA_TRANSFORM = "uCameraTransform";
	private static final String U_OBJECT_TRANSFORM = "uObjectTransform";
	
	private static final String U_MATERIAL_AMBIENT = "uMaterial.ambient";
	private static final String U_MATERIAL_DIFFUSE = "uMaterial.diffuse";
	private static final String U_MATERIAL_SPECULAR = "uMaterial.specular";
	private static final String U_MATERIAL_REFLECTANCE = "uMaterial.reflectance";
	private static final String U_MATERIAL_HAS_NORMAL_MAP = "uMaterial.hasNormalMap";
	private static final String U_AMBIENT_LIGHT_FACTOR = "uAmbientLight.factor";
	private static final String U_AMBIENT_LIGHT_COLOR = "uAmbientLight.color";
	private static final String U_POINT_LIGHTS = "uPointLights";
	private static final String U_BONE_MATRICES = "uBoneMatrices";
	
	private static final String U_SHADOW_MAP = "uShadowMap";
	private static final String U_CASCADE_SHADOWS = "uCascadeShadows";
	
	private static final String U_LIGHT_VIEW_SHADOWS = "uLightView";
	private static final String U_OBJECT_TRANSFORM_SHADOWS = "uObjectTransform";
	private static final String U_BONE_MATRICES_SHADOWS = "uBoneMatrices";
	
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
	private ShaderProgram shaderProgramShadows;
	private ShaderProgram shaderProgramGUI;
	
	private List<CascadeShadow> cascadeShadows;
	private ShadowBuffer shadowBuffer;
	
	private VAOCache vaoCache;
	private TextureCache textureCache;
	private Scene scene;
	
	public Renderer(Window clientWindow, Scene scene) {
		this.clientWindow = clientWindow;
		this.shaderProgram = null;
		this.shaderProgramShadows = null;
		this.shaderProgramGUI = null;
		this.cascadeShadows = null;
		this.shadowBuffer = null;
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
		this.shaderProgram.declareUniform(U_DIFFUSE_SAMPLER);
		this.shaderProgram.declareUniform(U_NORMAL_SAMPLER);
		this.shaderProgram.declareUniform(U_PROJECTION);
		this.shaderProgram.declareUniform(U_CAMERA_TRANSFORM);
		this.shaderProgram.declareUniform(U_OBJECT_TRANSFORM);
		
		this.shaderProgram.declareUniform(U_MATERIAL_AMBIENT);
		this.shaderProgram.declareUniform(U_MATERIAL_DIFFUSE);
		this.shaderProgram.declareUniform(U_MATERIAL_SPECULAR);
		this.shaderProgram.declareUniform(U_MATERIAL_REFLECTANCE);
		this.shaderProgram.declareUniform(U_MATERIAL_HAS_NORMAL_MAP);
		this.shaderProgram.declareUniform(U_AMBIENT_LIGHT_FACTOR);
		this.shaderProgram.declareUniform(U_AMBIENT_LIGHT_COLOR);
		this.shaderProgram.declareUniform(U_BONE_MATRICES);
		
		for( int i = 0; i < MAX_POINT_LIGHTS; i++ ) {
			this.shaderProgram.declareUniform(
				U_POINT_LIGHTS + "[" + i + "].position"
			);
			this.shaderProgram.declareUniform(
				U_POINT_LIGHTS + "[" + i + "].color"
			);
			this.shaderProgram.declareUniform(
				U_POINT_LIGHTS + "[" + i + "].intensity"
			);
			this.shaderProgram.declareUniform(
				U_POINT_LIGHTS + "[" + i + "].att.constant"
			);
			this.shaderProgram.declareUniform(
				U_POINT_LIGHTS + "[" + i + "].att.linear"
			);
			this.shaderProgram.declareUniform(
				U_POINT_LIGHTS + "[" + i + "].att.exponent"
			);
		}
		
		for( int i = 0; i < CascadeShadow.SHADOW_MAP_CASCADE_COUNT; i++ ) {
			this.shaderProgram.declareUniform(U_SHADOW_MAP + "[" + i + "]");
			this.shaderProgram.declareUniform(U_CASCADE_SHADOWS + "[" + i + "].lightView");
			this.shaderProgram.declareUniform(U_CASCADE_SHADOWS + "[" + i + "].splitDistance");
		}

			// Spot light uniform declarations here
		this.shaderProgram.addShader(
			new Shader("shaders/scene/scene.vert", GL46.GL_VERTEX_SHADER)
		);
		this.shaderProgram.addShader(
			new Shader("shaders/scene/scene.frag", GL46.GL_FRAGMENT_SHADER)
		);
		this.shaderProgram.init();
		
			// Set point light uniforms to default values, RIGHT NOW LIGHTS CANNOT BE REMOVED 
		for( int i = 0; i < MAX_POINT_LIGHTS; i++ ) {
			this.shaderProgram.setVector3fUniform(
				U_POINT_LIGHTS + "[" + i + "].position", new Vector3f(0.0f)
			);
			this.shaderProgram.setVector3fUniform(
				U_POINT_LIGHTS + "[" + i + "].color", new Vector3f(0.0f)
			);
			this.shaderProgram.setFloat1Uniform(
				U_POINT_LIGHTS + "[" + i + "].intensity", 0.0f
			);
			this.shaderProgram.setFloat1Uniform(
				U_POINT_LIGHTS + "[" + i + "].att.constant", 0.0f
			);
			this.shaderProgram.setFloat1Uniform(
				U_POINT_LIGHTS + "[" + i + "].att.linear", 0.0f
			);
			this.shaderProgram.setFloat1Uniform(
				U_POINT_LIGHTS + "[" + i + "].att.exponent", 0.0f
			);
		}
		
			// Spot light uniform default value settings here
		
			// Cascade shadow shaders
		this.shaderProgramShadows = new ShaderProgram();
		this.shaderProgramShadows.addShader(
			new Shader("shaders/cshadow/cshadow.vert", GL46.GL_VERTEX_SHADER)
		);
		this.shaderProgramShadows.declareUniform(U_LIGHT_VIEW_SHADOWS);
		this.shaderProgramShadows.declareUniform(U_OBJECT_TRANSFORM_SHADOWS);
		this.shaderProgramShadows.declareUniform(U_BONE_MATRICES_SHADOWS);
		this.shaderProgramShadows.init();
		
		this.cascadeShadows = new ArrayList<>();
		this.shadowBuffer = new ShadowBuffer();
		this.shadowBuffer.init();
		
		for( int i = 0; i < CascadeShadow.SHADOW_MAP_CASCADE_COUNT; i++ ) {
			this.cascadeShadows.add(new CascadeShadow());
		}
		
			// GUI shaders
		this.shaderProgramGUI = new ShaderProgram();
		this.shaderProgramGUI.declareUniform(U_PROJECTION_GUI);
		this.shaderProgramGUI.declareUniform(U_DIFFUSE_SAMPLER_GUI);
		this.shaderProgramGUI.declareUniform(U_OBJECT_TRANSFORM_GUI);
		this.shaderProgramGUI.declareUniform(U_TEXT_COLOR_GUI);
		this.shaderProgramGUI.addShader(
			new Shader("shaders/gui/gui.vert", GL46.GL_VERTEX_SHADER)
		);
		this.shaderProgramGUI.addShader(
			new Shader("shaders/gui/gui.frag", GL46.GL_FRAGMENT_SHADER)
		);
		this.shaderProgramGUI.init();
		
			// Initialize scene graphics assets
		this.scene.init();
		this.vaoCache = new VAOCache();
		this.textureCache = new TextureCache();
	}
	
	private void updatePointLight(PointLight pointLight, int index) {
        Vector4f aux = new Vector4f();
        
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
        
		this.shaderProgram.setVector3fUniform(
			U_POINT_LIGHTS + "[" + index + "].position", lightPosition
		);
		this.shaderProgram.setVector3fUniform(
			U_POINT_LIGHTS + "[" + index + "].color", color
		);
		this.shaderProgram.setFloat1Uniform(
			U_POINT_LIGHTS + "[" + index + "].intensity", intensity
		);
		this.shaderProgram.setFloat1Uniform(
			U_POINT_LIGHTS + "[" + index + "].att.constant", constant
		);
		this.shaderProgram.setFloat1Uniform(
			U_POINT_LIGHTS + "[" + index + "].att.linear", linear
		);
		this.shaderProgram.setFloat1Uniform(
			U_POINT_LIGHTS + "[" + index + "].att.exponent", exponent
		);
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
		
			/////////////////////////////////// Cascade shadow render pass ///////////////////////////////////
		GL46.glEnable(GL46.GL_DEPTH_TEST);
		GL46.glEnable(GL46.GL_BLEND);
		GL46.glBlendFunc(GL46.GL_SRC_ALPHA, GL46.GL_ONE_MINUS_SRC_ALPHA);
		//GL46.glEnable(GL46.GL_MULTISAMPLE);
        activeShaderProgram = this.shaderProgramShadows;
        activeShaderProgram.bind();
        
        CascadeShadow.updateCascadeShadows(this.cascadeShadows, this.scene.getActiveCamera());
        GL46.glBindFramebuffer(GL46.GL_FRAMEBUFFER, this.shadowBuffer.getDepthMapFBO());
        GL46.glViewport(
    		0, 0, ShadowBuffer.DEFAULT_SHADOW_MAP_WIDTH, ShadowBuffer.DEFAULT_SHADOW_MAP_HEIGHT
		);

        for (int i = 0; i < CascadeShadow.SHADOW_MAP_CASCADE_COUNT; i++) {
        	GL46.glFramebufferTexture2D(
    			GL46.GL_FRAMEBUFFER, 
    			GL46.GL_DEPTH_ATTACHMENT, 
    			GL46.GL_TEXTURE_2D, 
    			this.shadowBuffer.getDepthMap().getTextureHandles()[i], 
    			0
			);
        	
        	
        	GL46.glClear(GL46.GL_DEPTH_BUFFER_BIT);

            CascadeShadow shadowCascade = this.cascadeShadows.get(i);
            activeShaderProgram.setMatrix4fUniform(
        		U_LIGHT_VIEW_SHADOWS, shadowCascade.getLightViewMatrix()
    		);

            for( ASceneObject object : this.scene.getObjects() ) {
            	if( object instanceof Model ) {
            		Model model = (Model) object;
            		for( int m = 0; m < model.getMeshCount(); m++ ) {
            			Mesh mesh = model.getMesh(m);
                		VAO vao = this.vaoCache.getOrGenerate(mesh);
                		vao.bind();
                		
                		activeShaderProgram.setMatrix4fUniform(
            				U_OBJECT_TRANSFORM_SHADOWS, model.getTransformMatrix()
        				);
                		AnimationData animationData = model.getAnimationData();
    					if( animationData == null ) {
    						activeShaderProgram.setMatrix4fArrayUniform(
    							U_BONE_MATRICES_SHADOWS, AnimationData.DEFAULT_BONE_TRANSFORMS
    						);
    						DebugUtils.log(this, "it was null SHADOW");
    						
    					} else {
    						activeShaderProgram.setMatrix4fArrayUniform(
    							U_BONE_MATRICES_SHADOWS, animationData.getCurrentFrame().getBoneTransforms()
    						);
    						
    						
    						DebugUtils.log(this, "not null animation data SHADOW", animationData.getCurrentFrame().getBoneTransforms());
    					}
                		DebugUtils.log(this, "drawing shadow");
                		GL46.glDrawElements(
            				GL46.GL_TRIANGLES, vao.getVertexCount() * 3, GL46.GL_UNSIGNED_INT, 0
        				);
                		
                		DebugUtils.log(this, "drawing shadow success");
            		}
            	}
            }
        }

        activeShaderProgram.unbind();
        GL46.glBindFramebuffer(GL46.GL_FRAMEBUFFER, 0);
		//GL46.glDisable(GL46.GL_DEPTH_TEST);
		//GL46.glDisable(GL46.GL_BLEND);
        //GL46.glBlendFunc(GL46.GL_SRC_ALPHA, GL46.GL_ONE_MINUS_SRC_ALPHA);
        //GL46.glDisable(GL46.GL_MULTISAMPLE);
        
			/////////////////////////////////// Scene render pass ///////////////////////////////////
        GL46.glDisable(GL46.GL_BLEND);
		GL46.glEnable(GL46.GL_DEPTH_TEST);
		//GL46.glEnable(GL46.GL_BLEND);
		//GL46.glBlendFunc(GL46.GL_SRC_ALPHA, GL46.GL_ONE_MINUS_SRC_ALPHA);
		//GL46.glEnable(GL46.GL_MULTISAMPLE);
		activeShaderProgram = this.shaderProgram;
		activeShaderProgram.bind();
		
		final int DIFFUSE_SAMPLER = 0;
		final int NORMAL_SAMPLER = 1;
		final int SHADOW_MAP_FIRST = 2;
		
		activeShaderProgram.setInteger1Uniform(U_DIFFUSE_SAMPLER, DIFFUSE_SAMPLER);
		activeShaderProgram.setInteger1Uniform(U_NORMAL_SAMPLER, NORMAL_SAMPLER);
		
		for( int i = 0; i < CascadeShadow.SHADOW_MAP_CASCADE_COUNT; i++ ) {
			CascadeShadow cascadeShadow = this.cascadeShadows.get(i);
			activeShaderProgram.setInteger1Uniform(
				U_SHADOW_MAP + "[" + i + "]", SHADOW_MAP_FIRST + i
			);
			activeShaderProgram.setMatrix4fUniform(
				U_CASCADE_SHADOWS + "[" + i + "].lightView", cascadeShadow.getLightViewMatrix()
			);
			activeShaderProgram.setFloat1Uniform(
				U_CASCADE_SHADOWS + "[" + i + "].splitDistance", cascadeShadow.getSplitDistance()
			);
		}
		
		this.shadowBuffer.bindTextures(GL46.GL_TEXTURE2);
		
		Camera activeCamera = this.scene.getActiveCamera();
		activeCamera.getProjection().update(
			this.clientWindow.getWidth(), this.clientWindow.getHeight()
		);
		
		activeShaderProgram.setMatrix4fUniform(
			U_PROJECTION, activeCamera.getProjection().getMatrix()
		);
		
		activeShaderProgram.setMatrix4fUniform(
			U_CAMERA_TRANSFORM, activeCamera.getCameraTransform()
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
					U_AMBIENT_LIGHT_FACTOR, ambientLight.getIntensity()
				);
				activeShaderProgram.setVector3fUniform(
					U_AMBIENT_LIGHT_COLOR, ambientLight.getColor()
				);
			} else if( object instanceof PointLight ) {
					// WARNING! Using default point light index 0 here, other point lights are not calculated as of now
				this.updatePointLight((PointLight) object, 0);
			} else if( object instanceof Model ) {
				object.updateTransformMatrix();
				activeShaderProgram.setMatrix4fUniform(
					U_OBJECT_TRANSFORM, object.getTransformMatrix()
				);
				
				Model model = (Model) object;
				
				for( int m = 0; m < model.getMeshCount(); m++ ) {
					Material material = model.getMaterial(m);
					Mesh mesh = model.getMesh(m);
					
					for( int i = 0; i < material.getTextures().length; i++ ) {
						DebugUtils.log(this, "texture" + i);
						Texture texture = material.getTextures()[i];
						if( texture == null ) {
							continue;
						}
						
						this.textureCache.generateIfNotEncountered(texture);
						GL46.glActiveTexture(GL46.GL_TEXTURE0 + i);
						texture.bind();
					}
					
					if( material.getTexture(1) != null ) {
						activeShaderProgram.setInteger1Uniform(
							U_MATERIAL_HAS_NORMAL_MAP, 1
						);
					} else {
						activeShaderProgram.setInteger1Uniform(
							U_MATERIAL_HAS_NORMAL_MAP, 0
						);
					}
					
					activeShaderProgram.setVector4fUniform(
						U_MATERIAL_AMBIENT, material.getAmbientColor()
					);
					activeShaderProgram.setVector4fUniform(
						U_MATERIAL_DIFFUSE, material.getDiffuseColor()
					);
					activeShaderProgram.setVector4fUniform(
						U_MATERIAL_SPECULAR, material.getSpecularColor()
					);
					activeShaderProgram.setFloat1Uniform(
						U_MATERIAL_REFLECTANCE, material.getReflectance()
					);
					
					VAO vao = this.vaoCache.getOrGenerate(mesh);
					vao.bind();
					
					AnimationData animationData = model.getAnimationData();
					
					if( animationData == null ) {
						activeShaderProgram.setMatrix4fArrayUniform(
							U_BONE_MATRICES, AnimationData.DEFAULT_BONE_TRANSFORMS
						);
						DebugUtils.log(this, "it was null");
						
					} else {
						activeShaderProgram.setMatrix4fArrayUniform(
							U_BONE_MATRICES, animationData.getCurrentFrame().getBoneTransforms()
						);
						
						
						DebugUtils.log(this, "not null animation data", animationData.getCurrentFrame().getBoneTransforms());
					}
					
					DebugUtils.log(this, "draw", object);
					
					
					GL46.glDrawElements(
						GL46.GL_TRIANGLES, vao.getVertexCount() * 3, GL46.GL_UNSIGNED_INT, 0
					);
					
					DebugUtils.log(this, "draw success", object);
				}
			}
		}
		
		//GL46.glBindVertexArray(0); // may not be needed
		//GL46.glDisable(GL46.GL_DEPTH_TEST);
		//GL46.glDisable(GL46.GL_BLEND);
        //GL46.glBlendFunc(GL46.GL_SRC_ALPHA, GL46.GL_ONE_MINUS_SRC_ALPHA);
        //GL46.glDisable(GL46.GL_MULTISAMPLE);
		activeShaderProgram.unbind();
		
		DebugUtils.log(this, "gui");
		
			/////////////////////////////////// GUI render pass ///////////////////////////////////
		if( this.scene.getGUI() != null ) {
			DebugUtils.log(this, "gui pass starts");
		  	GL46.glDisable(GL46.GL_DEPTH_TEST);
			//GL46.glEnable(GL46.GL_BLEND);
			//GL46.glBlendFunc(GL46.GL_SRC_ALPHA, GL46.GL_ONE_MINUS_SRC_ALPHA);
	        activeShaderProgram = this.shaderProgramGUI;
	        activeShaderProgram.bind();
	        activeShaderProgram.setInteger1Uniform(U_DIFFUSE_SAMPLER_GUI, 0);
			
	        activeShaderProgram.setMatrix4fUniform(
				U_PROJECTION_GUI, 
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
					
					activeShaderProgram.setVector4fUniform(U_TEXT_COLOR_GUI, color);
					DebugUtils.log(this, "text element generating texture", texture.getPath());
					this.textureCache.generateIfNotEncountered(texture);
					DebugUtils.log(this, "text element generating texture success");
					GL46.glActiveTexture(GL46.GL_TEXTURE0);
					texture.bind();

					for( String line : text.getContent().split("\n") ) {
						for( int i = 0; i < line.length(); i++ ) {
							Font.Glyph glyph = font.getGlyph(line.charAt(i));
							activeShaderProgram.setMatrix4fUniform(
								U_OBJECT_TRANSFORM_GUI, 
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
							DebugUtils.log(this, "drawing gui element");
							GL46.glDrawElements(
								GL46.GL_TRIANGLES, 
								vao.getVertexCount() * 3, 
								GL46.GL_UNSIGNED_INT, 
								0
							);
							
							DebugUtils.log(this, "drawing gui element success");
							
							textX += glyph.getWidth();
						}
						
						textX = 0.0f;
						textY += lineHeight;
					}
				}
			}
			
			//GL46.glBindVertexArray(0); // may not be needed
		  	GL46.glDisable(GL46.GL_DEPTH_TEST);
			GL46.glDisable(GL46.GL_BLEND);
			activeShaderProgram.unbind();
		}
		DebugUtils.log(this, "gui end");
	}
	
	public Window getClientWindow() {
		return this.clientWindow;
	}
}

package project.pass;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL46;

import project.asset.AnimationData;
import project.asset.Mesh;
import project.component.Attenuation;
import project.component.CascadeShadow;
import project.component.Material;
import project.opengl.Renderer;
import project.opengl.Texture;
import project.opengl.TextureCache;
import project.opengl.VAO;
import project.opengl.VAOCache;
import project.scene.ASceneObject;
import project.scene.AmbientLight;
import project.scene.Camera;
import project.scene.Model;
import project.scene.PointLight;
import project.scene.Scene;
import project.shader.Shader;
import project.shader.ShaderProgram;

public class SceneRenderPass implements IRenderPass {
	public static final int DEFAULT_FIRST_FREE_TEXTURE_INDEX = 2;
	
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
	
	private static final int MAX_POINT_LIGHTS = 5;
	
	private ShaderProgram shaderProgram;
	private CascadeShadowRenderPass cascadeShadowRenderPass;
	
	public SceneRenderPass() {
		this.shaderProgram = new ShaderProgram();
		this.cascadeShadowRenderPass = null;
	}
	
	
	@Override
	public boolean init() {
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
		
		return true;
	}

	@Override
	public void render(Renderer renderer) {
		Scene scene = renderer.getActiveScene();
		ShaderProgram activeShaderProgram = this.shaderProgram;
		VAOCache vaoCache = renderer.getVAOCache();
		TextureCache textureCache = renderer.getTextureCache();
		
		activeShaderProgram.bind();
		
		final int DIFFUSE_SAMPLER = 0;
		final int NORMAL_SAMPLER = 1;
		final int SHADOW_MAP_FIRST = DEFAULT_FIRST_FREE_TEXTURE_INDEX;
		
		activeShaderProgram.setInteger1Uniform(U_DIFFUSE_SAMPLER, DIFFUSE_SAMPLER);
		activeShaderProgram.setInteger1Uniform(U_NORMAL_SAMPLER, NORMAL_SAMPLER);
		
		for( int i = 0; i < CascadeShadow.SHADOW_MAP_CASCADE_COUNT; i++ ) {
			CascadeShadow cascadeShadow = this.cascadeShadowRenderPass.getCascadeShadow(i);
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
		
		this.cascadeShadowRenderPass.getShadowBuffer().bindTextures(GL46.GL_TEXTURE2);
		
		Camera activeCamera = scene.getActiveCamera();
		activeShaderProgram.setMatrix4fUniform(
			U_PROJECTION, activeCamera.getProjection().getMatrix()
		);
		
		activeShaderProgram.setMatrix4fUniform(
			U_CAMERA_TRANSFORM, activeCamera.getCameraTransform()
		);
		
		for( ASceneObject object : scene.getObjects() ) {
			
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
				this.updatePointLight(scene, (PointLight) object, 0);
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
						Texture texture = material.getTextures()[i];
						if( texture == null ) {
							continue;
						}
						
						textureCache.generateIfNotEncountered(texture);
						GL46.glActiveTexture(GL46.GL_TEXTURE0 + i);
						texture.bind();
					}
					
					if( material.getTexture(1) != null && scene.DEBUGareNormalsActive() ) {
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
					
					VAO vao = vaoCache.getOrGenerate(mesh);
					vao.bind();
					
					AnimationData animationData = model.getAnimationData();
					
					if( animationData == null ) {
						activeShaderProgram.setMatrix4fArrayUniform(
							U_BONE_MATRICES, AnimationData.DEFAULT_BONE_TRANSFORMS
						);
						
					} else {
						activeShaderProgram.setMatrix4fArrayUniform(
							U_BONE_MATRICES, animationData.getCurrentFrame().getBoneTransforms()
						);
					}

					GL46.glDrawElements(
						GL46.GL_TRIANGLES, vao.getVertexCount() * 3, GL46.GL_UNSIGNED_INT, 0
					);
				}
			}
		}
		activeShaderProgram.unbind();
	}
	
	private void updatePointLight(Scene scene, PointLight pointLight, int index) {
        Vector4f aux = new Vector4f();
        
        Matrix4f cameraTransform = scene.getActiveCamera().getCameraTransform();
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
	
	public void setCascadeShadowRenderPass(CascadeShadowRenderPass cascadeShadowRenderPass) {
		this.cascadeShadowRenderPass = cascadeShadowRenderPass;
	}
}

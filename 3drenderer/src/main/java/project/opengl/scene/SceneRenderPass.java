package project.opengl.scene;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL46;

import project.component.Attenuation;
import project.component.CascadeShadow;
import project.core.renderer.IRenderPass;
import project.core.renderer.IRenderer;
import project.core.renderer.NullRenderStrategy;
import project.core.renderer.RenderStrategyManager;
import project.opengl.RendererGL;
import project.opengl.cshadow.CascadeShadowRenderPass;
import project.opengl.shader.Shader;
import project.opengl.shader.ShaderProgram;
import project.scene.ASceneObject;
import project.scene.AmbientLight;
import project.scene.Camera;
import project.scene.Model;
import project.scene.PointLight;
import project.scene.Scene;

public class SceneRenderPass implements IRenderPass {
	public static final int DEFAULT_FIRST_FREE_TEXTURE_INDEX = 2;
	
	static final String U_DIFFUSE_SAMPLER = "uDiffuseSampler";
	static final String U_NORMAL_SAMPLER = "uNormalSampler";
	static final String U_PROJECTION = "uProjection";
	static final String U_CAMERA_TRANSFORM = "uCameraTransform";
	static final String U_OBJECT_TRANSFORM = "uObjectTransform";
	
	static final String U_MATERIAL_AMBIENT = "uMaterial.ambient";
	static final String U_MATERIAL_DIFFUSE = "uMaterial.diffuse";
	static final String U_MATERIAL_SPECULAR = "uMaterial.specular";
	static final String U_MATERIAL_REFLECTANCE = "uMaterial.reflectance";
	static final String U_MATERIAL_HAS_NORMAL_MAP = "uMaterial.hasNormalMap";
	static final String U_AMBIENT_LIGHT_FACTOR = "uAmbientLight.factor";
	static final String U_AMBIENT_LIGHT_COLOR = "uAmbientLight.color";
	static final String U_POINT_LIGHTS = "uPointLights";
	static final String U_BONE_MATRICES = "uBoneMatrices";
	
	static final String U_SHADOW_MAP = "uShadowMap";
	static final String U_CASCADE_SHADOWS = "uCascadeShadows";
	
	static final int MAX_POINT_LIGHTS = 5;
	
	ShaderProgram shaderProgram;
	CascadeShadowRenderPass cascadeShadowRenderPass;
	
	private RenderStrategyManager<SceneRenderPass> renderStrategyManager;
	
	public SceneRenderPass() {
		this.shaderProgram = new ShaderProgram();
		this.cascadeShadowRenderPass = null;
		
		this.renderStrategyManager = new RenderStrategyManager<>(new NullRenderStrategy<SceneRenderPass>())
		.addStrategy(Model.class, new RenderModel())
		.addStrategy(PointLight.class, new RenderPointLight())
		.addStrategy(AmbientLight.class, new RenderAmbientLight());
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
	public void render(IRenderer renderer) {
		final int DIFFUSE_SAMPLER = 0;
		final int NORMAL_SAMPLER = 1;
		final int SHADOW_MAP_FIRST = DEFAULT_FIRST_FREE_TEXTURE_INDEX;
		
		Scene scene = ((RendererGL) renderer).getActiveScene();
		ShaderProgram activeShaderProgram = this.shaderProgram;
		
		activeShaderProgram.bind();
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
		activeCamera.updateTransformMatrix();
		
		activeShaderProgram.setMatrix4fUniform(
			U_PROJECTION, activeCamera.getProjection().getMatrix()
		);
		
		activeShaderProgram.setMatrix4fUniform(
			U_CAMERA_TRANSFORM, activeCamera.getTransformMatrix()
		);
		
		for( ASceneObject object : scene.getObjects() ) {
			this.recursiveRender(renderer, object);
		}
		
		activeShaderProgram.unbind();
	}
	
	private void recursiveRender(IRenderer renderer, ASceneObject object) {
		for( ASceneObject child : object.getChildren() ) {
			this.recursiveRender(renderer, child);
		}
		
		this.renderStrategyManager.getStrategy(object.getClass()).execute(renderer, this, object);
	}
	
	void updatePointLight(Scene scene, PointLight pointLight, int index) {
        Vector4f aux = new Vector4f();
        
        Matrix4f cameraTransform = scene.getActiveCamera().getTransformMatrix();
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

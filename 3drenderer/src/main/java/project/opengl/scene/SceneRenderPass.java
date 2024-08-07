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
import project.opengl.shader.custom.amlight.UAmbientLight;
import project.opengl.shader.custom.attenuation.SSAttenuation;
import project.opengl.shader.custom.cshadow.SSCascadeShadow;
import project.opengl.shader.custom.cshadow.UCascadeShadow;
import project.opengl.shader.custom.material.UMaterial;
import project.opengl.shader.custom.ptlight.SSPointLight;
import project.opengl.shader.custom.ptlight.UPointLight;
import project.opengl.shader.uniform.UAMatrix4f;
import project.opengl.shader.uniform.UArray;
import project.opengl.shader.uniform.UInteger1;
import project.scene.ASceneObject;
import project.scene.AmbientLight;
import project.scene.Camera;
import project.scene.Model;
import project.scene.PointLight;
import project.scene.Scene;

public class SceneRenderPass implements IRenderPass {
	public static final int DEFAULT_FIRST_FREE_TEXTURE_INDEX = 2;
	static final int MAX_POINT_LIGHTS = 5;
	
		// Shared with render passes
	ShaderProgram shaderProgram;
	CascadeShadowRenderPass cascadeShadowRenderPass;
	
		// Cache frequently used uniforms
	private UInteger1 uDiffuseSampler;
	private UInteger1 uNormalSampler;
	private UAMatrix4f uProjection;
	private UAMatrix4f uCameraTransform;
	private UArray<Integer> uShadowMap;
	private UArray<SSPointLight> uPointLights;
	private UArray<SSCascadeShadow> uCascadeShadows;
	
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
		this.uDiffuseSampler = new UInteger1("uDiffuseSampler");
		this.uNormalSampler = new UInteger1("uNormalSampler");
		this.uProjection = new UAMatrix4f("uProjection");
		this.uCameraTransform = new UAMatrix4f("uCameraTransform");
		
		this.uShadowMap = new UArray<>("uShadowMap", new UInteger1[CascadeShadow.SHADOW_MAP_CASCADE_COUNT]);
		this.uShadowMap.fill(() -> new UInteger1());
		
		this.uPointLights = new UArray<>("uPointLights", new UPointLight[MAX_POINT_LIGHTS]);
		this.uPointLights.fill(() -> new UPointLight());
		
		this.uCascadeShadows = new UArray<>("uCascadeShadows", new UCascadeShadow[CascadeShadow.SHADOW_MAP_CASCADE_COUNT]);
		this.uCascadeShadows.fill(() -> new UCascadeShadow());
		
		this.shaderProgram
		.declareUniform(this.uDiffuseSampler)
		.declareUniform(this.uNormalSampler)
		.declareUniform(this.uProjection)
		.declareUniform(this.uCameraTransform)
		.declareUniform(this.uShadowMap)
		.declareUniform(this.uPointLights)
		.declareUniform(this.uCascadeShadows)
		.declareUniform(new UAMatrix4f("uObjectTransform"))
		.declareUniform(new UMaterial("uMaterial"))
		.declareUniform(new UAmbientLight("uAmbientLight"))
		.declareUniform(new UAMatrix4f("uBoneMatrices"));

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
			SSAttenuation attenuationStruct = new SSAttenuation();
			attenuationStruct.constant = 0.0f;
			attenuationStruct.linear = 0.0f;
			attenuationStruct.exponent = 0.0f;
			
			SSPointLight pointLightStruct = new SSPointLight();
			pointLightStruct.position = new Vector3f(0.0f);
			pointLightStruct.color = new Vector3f(0.0f);
			pointLightStruct.intensity = 0.0f;
			pointLightStruct.att = attenuationStruct;
			
			this.uPointLights.update(pointLightStruct, i);
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
		this.uDiffuseSampler.update(DIFFUSE_SAMPLER);
		this.uNormalSampler.update(NORMAL_SAMPLER);
		
		for( int i = 0; i < CascadeShadow.SHADOW_MAP_CASCADE_COUNT; i++ ) {
			CascadeShadow cascadeShadow = this.cascadeShadowRenderPass.getCascadeShadow(i);
			this.uShadowMap.update(SHADOW_MAP_FIRST + i, i);
			
			SSCascadeShadow cascadeShadowStruct = new SSCascadeShadow();
			cascadeShadowStruct.lightView = cascadeShadow.getLightViewMatrix();
			cascadeShadowStruct.splitDistance = cascadeShadow.getSplitDistance();
			this.uCascadeShadows.update(cascadeShadowStruct, i);
		}
		
		this.cascadeShadowRenderPass.getShadowBuffer().bindTextures(GL46.GL_TEXTURE2);
		
		Camera activeCamera = scene.getActiveCamera();
		activeCamera.updateTransformMatrix();
		
		this.uProjection.update(activeCamera.getProjection().getMatrix());
		this.uCameraTransform.update(activeCamera.getTransformMatrix());
		
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
        
		SSAttenuation attenuationStruct = new SSAttenuation();
		attenuationStruct.constant = attenuation.getConstant();
		attenuationStruct.linear = attenuation.getLinear();
		attenuationStruct.exponent = attenuation.getExponent();
		
		SSPointLight pointLightStruct = new SSPointLight();
		pointLightStruct.position = lightPosition;
		pointLightStruct.color = color;
		pointLightStruct.intensity = pointLight.getIntensity();
		pointLightStruct.att = attenuationStruct;
		
		this.uPointLights.update(pointLightStruct, index);
	}
	
	public void setCascadeShadowRenderPass(CascadeShadowRenderPass cascadeShadowRenderPass) {
		this.cascadeShadowRenderPass = cascadeShadowRenderPass;
	}
}

package project.opengl.scene;

import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL46;

import project.Window;
import project.component.Attenuation;
import project.component.CascadeShadow;
import project.core.GameState;
import project.core.renderer.IRenderPass;
import project.core.renderer.IRenderer;
import project.core.renderer.NullRenderStrategy;
import project.core.renderer.RenderStrategyManager;
import project.opengl.cshadow.CascadeShadowRenderPass;
import project.opengl.shader.Shader;
import project.opengl.shader.ShaderProgram;
import project.opengl.shader.uniform.UAMatrix4f;
import project.opengl.shader.uniform.UArray;
import project.opengl.shader.uniform.UInteger1;
import project.opengl.shader.uniform.object.amlight.UAmbientLight;
import project.opengl.shader.uniform.object.attenuation.SSAttenuation;
import project.opengl.shader.uniform.object.cshadow.SSCascadeShadow;
import project.opengl.shader.uniform.object.cshadow.UCascadeShadow;
import project.opengl.shader.uniform.object.material.UMaterial;
import project.opengl.shader.uniform.object.ptlight.SSPointLight;
import project.opengl.shader.uniform.object.ptlight.UPointLight;
import project.scene.ASceneObject;
import project.scene.AmbientLight;
import project.scene.Camera;
import project.scene.Model;
import project.scene.PointLight;
import project.testing.TestDebugDataHandles;

public class SceneRenderPass implements IRenderPass {
	static final int MAX_POINT_LIGHTS = 5;
	
		// Shared with render passes
	ShaderProgram shaderProgram;
	CascadeShadowRenderPass cascadeShadowRenderPass;
	
		// Cache frequently used uniforms
	UAmbientLight uAmbientLight;
	UAMatrix4f uObjectTransform;
	UMaterial uMaterial;
	UAMatrix4f uBoneMatrices;
	
	private UInteger1 uDiffuseSampler;
	private UInteger1 uNormalSampler;
	private UInteger1 uRoughnessSampler;
	private UAMatrix4f uProjection;
	private UAMatrix4f uCameraTransform;
	private UArray<Integer> uShadowMap;
	private UArray<SSPointLight> uPointLights;
	private UArray<SSCascadeShadow> uCascadeShadows;
	private UInteger1 uDebugShowShadowCascades;
	
	private GameState gameState;
	private Camera activeCamera;
	private RenderStrategyManager<SceneRenderPass> renderStrategyManager;
	
	public SceneRenderPass() {
		this.gameState = null;
		this.shaderProgram = new ShaderProgram();
		this.cascadeShadowRenderPass = null;
		this.activeCamera = null;
		
		this.renderStrategyManager = 
			new RenderStrategyManager<>(new NullRenderStrategy<SceneRenderPass>())
		.addStrategy(Model.class, new RenderModel())
		.addStrategy(PointLight.class, new RenderPointLight())
		.addStrategy(AmbientLight.class, new RenderAmbientLight());
	}
	
	
	@Override
	public boolean initialize() {
		this.uDiffuseSampler = new UInteger1(Uniforms.DIFFUSE_SAMPLER);
		this.uNormalSampler = new UInteger1(Uniforms.NORMAL_SAMPLER);
		this.uRoughnessSampler = new UInteger1(Uniforms.ROUGHNESS_SAMPLER);
		this.uProjection = new UAMatrix4f(Uniforms.PROJECTION);
		this.uCameraTransform = new UAMatrix4f(Uniforms.CAMERA_TRANSFORM);
		this.uDebugShowShadowCascades = new UInteger1(Uniforms.DEBUG_SHOW_SHADOW_CASCADES);
		
		this.uShadowMap = new UArray<>(
			Uniforms.SHADOW_MAP, new UInteger1[CascadeShadow.SHADOW_MAP_CASCADE_COUNT]
		);
		this.uShadowMap.fill(() -> new UInteger1());
		
		this.uPointLights = new UArray<>(
			Uniforms.POINT_LIGHTS, new UPointLight[MAX_POINT_LIGHTS]
		);
		this.uPointLights.fill(() -> new UPointLight());
		
		this.uCascadeShadows = new UArray<>(
			Uniforms.CASCADE_SHADOWS, new UCascadeShadow[CascadeShadow.SHADOW_MAP_CASCADE_COUNT]
		);
		this.uCascadeShadows.fill(() -> new UCascadeShadow());
		
		this.uAmbientLight = new UAmbientLight(Uniforms.AMBIENT_LIGHT);
		this.uObjectTransform = new UAMatrix4f(Uniforms.OBJECT_TRANSFORM);
		this.uMaterial = new UMaterial(Uniforms.MATERIAL);
		this.uBoneMatrices = new UAMatrix4f(Uniforms.BONE_MATRICES);
		
		this.shaderProgram
		.declareUniform(this.uDiffuseSampler)
		.declareUniform(this.uNormalSampler)
		.declareUniform(this.uRoughnessSampler)
		.declareUniform(this.uProjection)
		.declareUniform(this.uCameraTransform)
		.declareUniform(this.uShadowMap)
		.declareUniform(this.uPointLights)
		.declareUniform(this.uCascadeShadows)
		.declareUniform(this.uDebugShowShadowCascades)
		.declareUniform(this.uObjectTransform)
		.declareUniform(this.uMaterial)
		.declareUniform(this.uAmbientLight)
		.declareUniform(this.uBoneMatrices);

			// Spot light uniform declarations here
		this.shaderProgram.addShader(
			new Shader("shaders/scene/scene.vert", GL46.GL_VERTEX_SHADER)
		);
		this.shaderProgram.addShader(
			new Shader("shaders/scene/scene.frag", GL46.GL_FRAGMENT_SHADER)
		);
		this.shaderProgram.initialize();
		
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
			pointLightStruct.attenuation = attenuationStruct;
			
			this.uPointLights.update(pointLightStruct, i);
		}
		
		return true;
	}

	@Override
	public void render(IRenderer renderer, GameState gameState) {
		final int DIFFUSE_SAMPLER = 0;
		final int NORMAL_SAMPLER = 1;
		final int ROUGHNESS_SAMPLER = 2;
		final int LAST_SAMPLER = ROUGHNESS_SAMPLER;
		final int SHADOW_MAP_FIRST = LAST_SAMPLER + 1;
		
		this.gameState = gameState;
		ShaderProgram activeShaderProgram = this.shaderProgram;
		
		activeShaderProgram.bind();
		this.uDiffuseSampler.update(DIFFUSE_SAMPLER);
		this.uNormalSampler.update(NORMAL_SAMPLER);
		this.uRoughnessSampler.update(ROUGHNESS_SAMPLER);
		
		for( int i = 0; i < CascadeShadow.SHADOW_MAP_CASCADE_COUNT; i++ ) {
			CascadeShadow cascadeShadow = this.cascadeShadowRenderPass.getCascadeShadow(i);
			this.uShadowMap.update(SHADOW_MAP_FIRST + i, i);
			
			SSCascadeShadow cascadeShadowStruct = new SSCascadeShadow();
			cascadeShadowStruct.lightView = cascadeShadow.getLightViewMatrix();
			cascadeShadowStruct.splitDistance = cascadeShadow.getSplitDistance();
			this.uCascadeShadows.update(cascadeShadowStruct, i);
		}
		
		this.cascadeShadowRenderPass.getShadowBuffer().bindTextures(GL46.GL_TEXTURE0 + SHADOW_MAP_FIRST);
		this.activeCamera = this.gameState.getActiveCamera();
		
		Window window = renderer.getClientWindow();
		this.activeCamera.getProjection().update(window.getWidth(), window.getHeight());
		this.uProjection.update(this.activeCamera.getProjection().getMatrix());
		this.uCameraTransform.update(this.activeCamera.getTransform().getAsMatrix());
		
		this.uDebugShowShadowCascades.update(
			(boolean) gameState.getDebugData(TestDebugDataHandles.CASCADE_SHADOW_ENABLED) ? 1 : 0
		);
		
        for( Map.Entry<Long, ASceneObject> en : gameState.getActiveScene().entrySet() ) {
        	ASceneObject object = en.getValue();
        	this.renderStrategyManager.getStrategy(object.getClass()).execute(renderer, this, object);
        }
		
		activeShaderProgram.unbind();
	}
	
	void updatePointLight(PointLight pointLight, int index) {
        Vector4f aux = new Vector4f();
        
        Matrix4f cameraTransform = this.activeCamera.getTransform().getAsMatrix();
        aux.set(pointLight.getTransform().getPosition(), 1);
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
		pointLightStruct.attenuation = attenuationStruct;
		
		this.uPointLights.update(pointLightStruct, index);
	}
	
	public void setCascadeShadowRenderPass(CascadeShadowRenderPass cascadeShadowRenderPass) {
		this.cascadeShadowRenderPass = cascadeShadowRenderPass;
	}
	
	@Override
	public GameState getGameState() {
		return this.gameState;
	}
}

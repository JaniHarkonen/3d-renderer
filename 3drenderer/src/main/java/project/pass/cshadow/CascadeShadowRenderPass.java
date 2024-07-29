package project.pass.cshadow;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL46;

import project.component.CascadeShadow;
import project.opengl.RenderStrategyManager;
import project.opengl.Renderer;
import project.opengl.ShadowBuffer;
import project.pass.IRenderPass;
import project.pass.NullRenderStrategy;
import project.scene.ASceneObject;
import project.scene.Model;
import project.scene.Scene;
import project.shader.Shader;
import project.shader.ShaderProgram;

public class CascadeShadowRenderPass implements IRenderPass {
	static final String U_LIGHT_VIEW = "uLightView";
	static final String U_OBJECT_TRANSFORM = "uObjectTransform";
	static final String U_BONE_MATRICES = "uBoneMatrices";
	
	ShaderProgram shaderProgram;
	List<CascadeShadow> cascadeShadows;
	ShadowBuffer shadowBuffer;
	
	private RenderStrategyManager<CascadeShadowRenderPass> renderStrategyManager;
	
	public CascadeShadowRenderPass() {
		this.shaderProgram = new ShaderProgram();
		this.cascadeShadows = new ArrayList<>();
		this.shadowBuffer = new ShadowBuffer();
		
		this.renderStrategyManager = new RenderStrategyManager<>(new NullRenderStrategy<CascadeShadowRenderPass>())
		.addStrategy(Model.class, new RenderModel());
	}
	
	
	@Override
	public boolean init() {
		this.shaderProgram.addShader(
			new Shader("shaders/cshadow/cshadow.vert", GL46.GL_VERTEX_SHADER)
		);
		this.shaderProgram.declareUniform(U_LIGHT_VIEW);
		this.shaderProgram.declareUniform(U_OBJECT_TRANSFORM);
		this.shaderProgram.declareUniform(U_BONE_MATRICES);
		this.shaderProgram.init();
		this.shadowBuffer.init();
		
		for( int i = 0; i < CascadeShadow.SHADOW_MAP_CASCADE_COUNT; i++ ) {
			this.cascadeShadows.add(new CascadeShadow());
		}
		
		return true;
	}
	
	@Override
	public void render(Renderer renderer) {
		Scene scene = renderer.getActiveScene();
		ShaderProgram activeShaderProgram = this.shaderProgram;
	    activeShaderProgram.bind();
	    
	    CascadeShadow.updateCascadeShadows(
    		this.cascadeShadows, scene.getActiveCamera(), scene.getShadowLightPosition()
		);
	    GL46.glBindFramebuffer(GL46.GL_FRAMEBUFFER, this.shadowBuffer.getDepthMapFBO());
	    GL46.glViewport(
			0, 0, ShadowBuffer.DEFAULT_SHADOW_MAP_WIDTH, ShadowBuffer.DEFAULT_SHADOW_MAP_HEIGHT
		);
	
	    for( int i = 0; i < CascadeShadow.SHADOW_MAP_CASCADE_COUNT; i++ ) {
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
	    		U_LIGHT_VIEW, shadowCascade.getLightViewMatrix()
			);
	
	        for( ASceneObject object : scene.getObjects() ) {
	        	this.recursiveRender(renderer, object);
	        }
	    }
	
	    activeShaderProgram.unbind();
	    GL46.glBindFramebuffer(GL46.GL_FRAMEBUFFER, 0);
	}
	
	private void recursiveRender(Renderer renderer, ASceneObject object) {
		for( ASceneObject child : object.getChildren() ) {
			this.recursiveRender(renderer, child);
		}
		
		this.renderStrategyManager.getStrategy(object.getClass()).execute(renderer, this, object);
	}
	
	public CascadeShadow getCascadeShadow(int cascadeShadowIndex) {
		return this.cascadeShadows.get(cascadeShadowIndex);
	}
	
	public ShadowBuffer getShadowBuffer() {
		return this.shadowBuffer;
	}
}

package project.opengl.cshadow;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL46;

import project.component.CascadeShadow;
import project.core.GameState;
import project.core.GameState.SceneState;
import project.core.renderer.IRenderPass;
import project.core.renderer.IRenderer;
import project.core.renderer.NullRenderStrategy;
import project.core.renderer.RenderStrategyManager;
import project.opengl.shader.Shader;
import project.opengl.shader.ShaderProgram;
import project.opengl.shader.uniform.UAMatrix4f;
import project.scene.ASceneObject;
import project.scene.Model;
import project.testing.TestDebugDataHandles;

public class CascadeShadowRenderPass implements IRenderPass {
	ShaderProgram shaderProgram;
	List<CascadeShadow> cascadeShadows;
	ShadowBuffer shadowBuffer;
	
	private GameState gameState;
	private UAMatrix4f uLightView;
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
		this.uLightView = new UAMatrix4f(Uniforms.LIGHT_VIEW);
		
		this.shaderProgram.declareUniform(this.uLightView)
		.declareUniform(new UAMatrix4f(Uniforms.OBJECT_TRANSFORM))
		.declareUniform(new UAMatrix4f(Uniforms.BONE_MATRICES));
		this.shaderProgram.init();
		this.shadowBuffer.init();
		
		for( int i = 0; i < CascadeShadow.SHADOW_MAP_CASCADE_COUNT; i++ ) {
			this.cascadeShadows.add(new CascadeShadow());
		}
		
		return true;
	}
	
	@Override
	public void render(IRenderer renderer, GameState gameState) {
		this.gameState = gameState;
		ShaderProgram activeShaderProgram = this.shaderProgram;
	    activeShaderProgram.bind();
	    
	    CascadeShadow.updateCascadeShadows(
    		this.cascadeShadows, 
    		this.gameState.getActiveCamera(), 
    		(Vector3f) this.gameState.getDebugData(TestDebugDataHandles.CASCADE_SHADOW_LIGHT)
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
	        this.uLightView.update(this.cascadeShadows.get(i).getLightViewMatrix());
	        
	        SceneState.SceneIterator iterator = gameState.getSceneIterator();
	        while( iterator.hasNext() ) {
	        	this.recursiveRender(renderer, iterator.next());
	        }
	    }
	
	    activeShaderProgram.unbind();
	    GL46.glBindFramebuffer(GL46.GL_FRAMEBUFFER, 0);
	}
	
	private void recursiveRender(IRenderer renderer, ASceneObject object) {
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
	
	@Override
	public GameState getGameState() {
		return this.gameState;
	}
}

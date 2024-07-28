package project.pass.cshadow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL46;

import project.asset.AnimationData;
import project.asset.Mesh;
import project.component.CascadeShadow;
import project.opengl.Renderer;
import project.opengl.ShadowBuffer;
import project.opengl.VAO;
import project.opengl.VAOCache;
import project.pass.IRenderPass;
import project.pass.IRenderStrategy;
import project.scene.ASceneObject;
import project.scene.Model;
import project.scene.Scene;
import project.shader.Shader;
import project.shader.ShaderProgram;
import project.testing.TestDummy;

public class CascadeShadowRenderPass implements IRenderPass {
	private static final String U_LIGHT_VIEW = "uLightView";
	private static final String U_OBJECT_TRANSFORM = "uObjectTransform";
	private static final String U_BONE_MATRICES = "uBoneMatrices";
	
	private ShaderProgram shaderProgram;
	private List<CascadeShadow> cascadeShadows;
	private ShadowBuffer shadowBuffer;
	private Map<Class<?>, IRenderStrategy<CascadeShadowRenderPass>> renderStrategies;
	
	public CascadeShadowRenderPass() {
		this.shaderProgram = new ShaderProgram();
		this.cascadeShadows = new ArrayList<>();
		this.shadowBuffer = new ShadowBuffer();
		this.renderStrategies = new HashMap<>();
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
		VAOCache vaoCache = renderer.getVAOCache();
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
	        	if( object instanceof TestDummy ) {
	        		Model model = ((TestDummy) object).getModel();
	        		for( int j = 0; j < model.getMeshCount(); j++ ) {
	        			Mesh mesh = model.getMesh(j);
	            		VAO vao = vaoCache.getOrGenerate(mesh);
	            		vao.bind();
	            		
	            		activeShaderProgram.setMatrix4fUniform(
	        				U_OBJECT_TRANSFORM, object.getTransformMatrix()
	    				);
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
	    }
	
	    activeShaderProgram.unbind();
	    GL46.glBindFramebuffer(GL46.GL_FRAMEBUFFER, 0);
	}
	
	public CascadeShadow getCascadeShadow(int cascadeShadowIndex) {
		return this.cascadeShadows.get(cascadeShadowIndex);
	}
	
	public ShadowBuffer getShadowBuffer() {
		return this.shadowBuffer;
	}
}

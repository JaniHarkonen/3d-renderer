package project.opengl;

import org.lwjgl.opengl.GL46;

import project.component.CascadeShadow;
import project.utils.DebugUtils;

public class ShadowBuffer {

	public static final int DEFAULT_SHADOW_MAP_WIDTH = 4096;
	public static final int DEFAULT_SHADOW_MAP_HEIGHT = 4096;
	
	private DepthTexture depthMap;
	private int depthMapFBO;
	
	public ShadowBuffer() {
		this.depthMapFBO = -1;
		this.depthMap = null;
		
	}
	
	
	public void init() {
		this.depthMapFBO = GL46.glGenFramebuffers();
		this.depthMap = new DepthTexture(
			CascadeShadow.SHADOW_MAP_CASCADE_COUNT, 
			ShadowBuffer.DEFAULT_SHADOW_MAP_WIDTH, 
			ShadowBuffer.DEFAULT_SHADOW_MAP_HEIGHT, 
			GL46.GL_DEPTH_COMPONENT
		);
		this.depthMap.init();
		
		GL46.glBindFramebuffer(GL46.GL_FRAMEBUFFER, this.depthMapFBO);
		GL46.glFramebufferTexture2D(
			GL46.GL_FRAMEBUFFER, 
			GL46.GL_DEPTH_ATTACHMENT, 
			GL46.GL_TEXTURE_2D, 
			this.depthMap.getTextureHandles()[0], 
			0
		);
		
		GL46.glDrawBuffer(GL46.GL_NONE);
		GL46.glReadBuffer(GL46.GL_NONE);
		
		if( GL46.glCheckFramebufferStatus(GL46.GL_FRAMEBUFFER) != GL46.GL_FRAMEBUFFER_COMPLETE ) {
			DebugUtils.log(this, "ERROR: Unable to create framebuffer for cascade shadows!");
			return;
		}
		
		GL46.glBindFramebuffer(GL46.GL_FRAMEBUFFER, 0);
	}
	
	public void bindTextures(int firstTextureIndex) {
		for( int i = 0; i < this.depthMap.getTextureHandles().length; i++ ) {
			GL46.glActiveTexture(firstTextureIndex + i);
			GL46.glBindTexture(GL46.GL_TEXTURE_2D, this.depthMap.getTextureHandles()[i]);
		}
	}
	
	public int getDepthMapFBO() {
		return this.depthMapFBO;
	}
	
	public DepthTexture getDepthMap() {
		return this.depthMap;
	}
}

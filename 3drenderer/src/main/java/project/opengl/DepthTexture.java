package project.opengl;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL46;

public class DepthTexture {

	private final int[] textureHandles;
	
	private int width;
	private int height;
	private int pixelFormat;
	
	public DepthTexture(int textureCount, int width, int height, int pixelFormat) {
		this.textureHandles = new int[textureCount];
		this.width = width;
		this.height = height;
		this.pixelFormat = pixelFormat;
	}
	
	
	public void initialize() {
		int target = GL46.GL_TEXTURE_2D;
		GL46.glGenTextures(this.textureHandles);
		
		for( int handle : this.textureHandles ) {
			GL46.glBindTexture(target, handle);
			GL46.glTexImage2D(
				target, 
				0, 
				GL46.GL_DEPTH_COMPONENT, 
				this.width, 
				this.height, 
				0, 
				this.pixelFormat, 
				GL46.GL_FLOAT, 
				(ByteBuffer) null
			);
			GL46.glTexParameteri(target, GL46.GL_TEXTURE_MIN_FILTER, GL46.GL_LINEAR);
			GL46.glTexParameteri(target, GL46.GL_TEXTURE_MAG_FILTER, GL46.GL_LINEAR);
			GL46.glTexParameteri(target, GL46.GL_TEXTURE_COMPARE_MODE, GL46.GL_NONE);
			GL46.glTexParameteri(target, GL46.GL_TEXTURE_WRAP_S, GL46.GL_CLAMP_TO_EDGE);
			GL46.glTexParameteri(target, GL46.GL_TEXTURE_WRAP_T, GL46.GL_CLAMP_TO_EDGE);
		}
	}
	
	public int[] getTextureHandles() {
		return this.textureHandles;
	}
}

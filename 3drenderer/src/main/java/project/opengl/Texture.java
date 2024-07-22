package project.opengl;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryStack;

public class Texture {

	private int handle;
	private ByteBuffer pixels;
	private int width;
	private int height;
	
	public Texture() {
		this.handle = -1;
		this.pixels = null;
		this.width = 0;
		this.height = 0;
	}
	
	public void init() {
		try( MemoryStack stack = MemoryStack.stackPush() ) {
			this.handle = GL46.glGenTextures();
			int target = GL46.GL_TEXTURE_2D;
			this.bind();
			GL46.glPixelStorei(GL46.GL_UNPACK_ALIGNMENT, 1);
			GL46.glTexParameteri(target, GL46.GL_TEXTURE_MIN_FILTER, GL46.GL_NEAREST);
			GL46.glTexParameteri(target, GL46.GL_TEXTURE_MAG_FILTER, GL46.GL_NEAREST);
			GL46.glTexImage2D(
				target, 
				0, 
				GL46.GL_RGBA, 
				this.width, 
				this.height, 
				0, 
				GL46.GL_RGBA, 
				GL46.GL_UNSIGNED_BYTE, 
				this.pixels
			);
			GL46.glGenerateMipmap(target);
			this.unbind();
		}
	}
	
	public void bind() {
		GL46.glBindTexture(GL46.GL_TEXTURE_2D, this.handle);
	}
	
	public void unbind() {
		GL46.glBindTexture(GL46.GL_TEXTURE_2D, this.handle);
	}
	
	public void setPixels(ByteBuffer pixels, int width, int height) {
		this.pixels = pixels;
		this.width = width;
		this.height = height;
	}
}

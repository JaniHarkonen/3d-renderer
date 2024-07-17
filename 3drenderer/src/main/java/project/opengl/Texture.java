package project.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL46;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import project.utils.DebugUtils;

public class Texture {

	private String texturePath;
	private int handle;
	
	public Texture(String texturePath) {
		this.handle = -1;
		this.texturePath = texturePath;
	}
	
	public void init() {
		try( MemoryStack stack = MemoryStack.stackPush() ) {
			IntBuffer widthBuffer = stack.mallocInt(1);
			IntBuffer heightBuffer = stack.mallocInt(1);
			IntBuffer channelsBuffer = stack.mallocInt(1);
			
			ByteBuffer textureBuffer = STBImage.stbi_load(
				this.texturePath, widthBuffer, heightBuffer, channelsBuffer, 4
			);
			
			if( textureBuffer == null ) {
				DebugUtils.log(
					this,
					"ERROR: Unable to load texture: ",
					this.texturePath,
					"Reason: ",
					STBImage.stbi_failure_reason()
				);
				
				return;
			}
			
			int width = widthBuffer.get();
			int height = heightBuffer.get();
			
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
				width, 
				height, 
				0, 
				GL46.GL_RGBA, 
				GL46.GL_UNSIGNED_BYTE, 
				textureBuffer
			);
			GL46.glGenerateMipmap(target);
			
			STBImage.stbi_image_free(textureBuffer);
			this.unbind();
		}
	}
	
	public void bind() {
		GL46.glBindTexture(GL46.GL_TEXTURE_2D, this.handle);
	}
	
	public void unbind() {
		GL46.glBindTexture(GL46.GL_TEXTURE_2D, this.handle);
	}
}

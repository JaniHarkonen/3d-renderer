package project.asset;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import project.opengl.Texture;
import project.utils.DebugUtils;

public class TextureLoadTask {

	private String texturePath;
	private Texture targetTexture;
	
	public TextureLoadTask(String texturePath, Texture targetTexture) {
		this.texturePath = texturePath;
		this.targetTexture = targetTexture;
	}
	
	
	public void load() {
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
			
			this.targetTexture.setPixels(textureBuffer, width, height);
			//STBImage.stbi_image_free(textureBuffer);
		}
	}
}

package project.asset.texture;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import project.Application;
import project.core.asset.ILoadTask;
import project.utils.DebugUtils;

public class TextureLoadTask implements ILoadTask {

	private String texturePath;
	private Texture targetTexture;
	
	public TextureLoadTask(String texturePath, Texture targetTexture) {
		this.texturePath = texturePath;
		this.targetTexture = targetTexture;
	}
	
	
	@Override
	public boolean load() {
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
				
				return false;
			}
			
			Texture.Data data = new Texture.Data();
			data.targetTexture = this.targetTexture;
			data.width = widthBuffer.get();
			data.height = heightBuffer.get();
			data.pixels = textureBuffer;
			
			Application.getApp().getAssetManager().notifyResult(this.targetTexture, data, Application.getApp().getRenderer());
			
			return true;
		}
	}
}

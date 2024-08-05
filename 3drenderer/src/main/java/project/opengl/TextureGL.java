package project.opengl;

import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryStack;

import project.Application;
import project.asset.IGraphics;
import project.asset.IGraphicsAsset;
import project.asset.Texture;

public class TextureGL implements IGraphics {

	private Texture targetTexture;
	private int handle;
	
	public TextureGL(Texture targetTexture) {
		this.targetTexture = targetTexture;
		this.handle = -1;
	}
	
	private TextureGL(TextureGL src) {
		this.targetTexture = src.targetTexture;
		this.handle = src.handle;
	}

	
	@Override
	public boolean generate() {
		try( MemoryStack stack = MemoryStack.stackPush() ) {
			this.handle = GL46.glGenTextures();
			int target = GL46.GL_TEXTURE_2D;
			this.bind();
			GL46.glPixelStorei(GL46.GL_UNPACK_ALIGNMENT, 1);
			GL46.glTexParameteri(target, GL46.GL_TEXTURE_MIN_FILTER, GL46.GL_LINEAR);
			GL46.glTexParameteri(target, GL46.GL_TEXTURE_MAG_FILTER, GL46.GL_LINEAR);
			GL46.glTexImage2D(
				target, 
				0, 
				GL46.GL_RGBA, 
				this.targetTexture.getWidth(),
				this.targetTexture.getHeight(),
				0, 
				GL46.GL_RGBA, 
				GL46.GL_UNSIGNED_BYTE, 
				this.targetTexture.getPixels()
			);
			GL46.glGenerateMipmap(target);
			this.unbind();
			this.targetTexture.setGraphics(this);
		}
		
		return true;
	}
	
	@Override
	public boolean regenerate() {
		if( !this.isNull() ) {
			this.dispose();
		}
		
		return new TextureGL(this.targetTexture).generate();
	}

	@Override
	public TextureGL createReference(IGraphicsAsset graphicsAsset) {
		TextureGL reference = new TextureGL(this);
		reference.targetTexture = (Texture) graphicsAsset;
		graphicsAsset.setGraphics(reference);
		return reference;
	}
	
	@Override
	public boolean dispose() {
		GL46.glDeleteTextures(this.handle);
		return true;
	}
	
	public void bind() {
		GL46.glBindTexture(GL46.GL_TEXTURE_2D, this.handle);
	}
	
	public void unbind() {
		GL46.glBindTexture(GL46.GL_TEXTURE_2D, this.handle);
	}
	
	@Override
	public void dropGraphicsAsset() {
		this.targetTexture = null;
	}

	@Override
	public boolean isNull() {
		TextureGL defaultTextureGL = (TextureGL) Application.getApp().getRenderer().getDefaultTextureGraphics();
		return (this.handle == defaultTextureGL.handle);
	}
	
	public int getHandle() {
		return this.handle;
	}

	@Override
	public IGraphicsAsset getGraphicsAsset() {
		return this.targetTexture;
	}
	
	@Override
	public boolean isGenerated() {
		return (this.handle >= 0);
	}
}

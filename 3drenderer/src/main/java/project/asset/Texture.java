package project.asset;

import java.nio.ByteBuffer;

import org.lwjgl.system.MemoryUtil;

import project.Default;

public class Texture implements IGraphicsAsset {
	
	public static class Data implements IAssetData {
		Texture targetTexture;
		int width;
		int height;
		ByteBuffer pixels;

		@Override
		public void assign(long timestamp) {
			this.targetTexture.populate(this.width, this.height, this.pixels);
			this.targetTexture.lastUpdateTimestamp = timestamp;
		}
	}
	

	public static Texture createTexture(String name, String bytes) {
        int width = 16;
        int height = 16;
        int channelCount = 4;
        ByteBuffer pixels = MemoryUtil.memAlloc(bytes.length() * channelCount);
        
            // Populate pixels upside-down due to OpenGL
        for( int i = bytes.length() - 1; i >= 0; i-- )
        {
            byte value = (bytes.charAt(i) == '1') ? (byte) 255 : 0;
            
            pixels.put((byte) value);
            pixels.put((byte) value);
            pixels.put((byte) value);
            pixels.put((byte) 255);
        }
        
        pixels.flip();
        
        Texture texture = new Texture(name, true);
        texture.populate(width, height, pixels);
        return texture;
	}
	
	private final String name;
	private long lastUpdateTimestamp;
	private IGraphics graphics;
	private ByteBuffer pixels;
	private int width;
	private int height;
	
	public Texture(String name) {
		this(name, false);
	}
	
	private Texture(String name, boolean isDefault) {
		this.name = name;
		this.lastUpdateTimestamp = -1;
		this.graphics = null;
		this.width = 0;
		this.height = 0;
		this.pixels = null;
		
		if( !isDefault ) {
			Texture defaultTexture = Default.TEXTURE;
			this.graphics = defaultTexture.graphics;
			this.populate(
				defaultTexture.width, 
				defaultTexture.height, 
				defaultTexture.pixels
			);
		}
	}

	
	public void populate(int width, int height, ByteBuffer pixels) {
		this.width = width;
		this.height = height;
		this.pixels = pixels;
	}
	
	public String getName() {
		return this.name;
	}

	
	@Override
	public void setGraphics(IGraphics graphics) {
		this.graphics = graphics;
	}

	@Override
	public IGraphics getGraphics() {
		return this.graphics;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public ByteBuffer getPixels() {
		return this.pixels;
	}
	
	@Override
	public long getLastUpdateTimestamp() {
		return this.lastUpdateTimestamp;
	}
}

package project.asset;

import java.nio.ByteBuffer;

import project.Globals;

public class Texture implements IGraphicsAsset {
	
	/************************* Data-class **************************/
	
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
	
	
	/************************* Texture-class **************************/

	public static Texture createTexture(String name, String bytes) {
        int width = 16;
        int height = 16;
        int channelCount = 4;
        
        Texture texture = new Texture(name, false);
        texture.populate(width, height, AssetUtils.stringToPixels(bytes, width, height, channelCount));
        return texture;
	}
	
	public static final Texture DEFAULT = new Texture("tex-default", true);
	static {
		DEFAULT.populate(
			16, 16, 
			AssetUtils.stringToPixels(
				"0000000000000000" + 
		        "0000000000000000" + 
		        "0000000000000000" + 
		        "0111110000111110" + 
		        "0001000000001000" + 
		        "0001000000001000" + 
		        "0001000000001000" + 
		        "0001000000001000" + 
		        "0001000000001000" + 
		        "0001000000001000" + 
		        "0001000000001000" + 
		        "0001000000001000" + 
		        "0000011111100000" + 
		        "0000000000000000" + 
		        "0000000000000000" + 
		        "0000000000000000", 
				16, 16, 4
			)
		);
	}
	
	
	/************************* Class body **************************/
	
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
		this.width = 0;
		this.height = 0;
		this.pixels = null;
		
		if( !isDefault ) {
			Globals.RENDERER.getDefaultTextureGraphics().createReference(this);
		} else {
			this.graphics = null;
		}
	}

	
	public void populate(int width, int height, ByteBuffer pixels) {
		this.width = width;
		this.height = height;
		this.pixels = pixels;
	}
	
	@Override
	public boolean deload() {
		if( this != DEFAULT ) {
			this.pixels = null;
			Globals.RENDERER.assetDeloaded(this);
			return true;
		}
		return false;
	}
	
	@Override
	public void setGraphics(IGraphics graphics) {
		this.graphics = graphics;
	}
	
	@Override
	public String getName() {
		return this.name;
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

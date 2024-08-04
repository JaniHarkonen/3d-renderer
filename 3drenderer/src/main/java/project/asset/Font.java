package project.asset;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class Font implements IAsset {
	
	/************************* Glyph-class *************************/
    
    public static class Glyph {
        private final char character;
        private final float x;
        private final float y;
        private final float width;
        private final float height;
        private final float originX;
        private final float originY;
        private final float advance;
        
        private Mesh mesh;
        private Font font;
        private float u0;
        private float v0;
        private float u1;
        private float v1;
        
        public Glyph(
            char character,
            float x,
            float y,
            float width,
            float height,
            float originX,
            float originY,
            float advance
        ) {
            this.character = character;
            this.mesh = Mesh.DEFAULT;
            this.font = null;
            
            this.x = x;
            this.y = y;
            
            this.width = width;
            this.height = height;
            
            this.originX = originX;
            this.originY = originY;
            this.advance = advance;
            
            this.u0 = 0.0f;
            this.v0 = 0.0f;
            this.u1 = 0.0f;
            this.v1 = 0.0f;
        }
        
        public Glyph(char character) {
            this(character, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
        }
        
        
        public char getCharacter() {
            return this.character;
        }
        
        public Mesh getMesh() {
            return this.mesh;
        }
        
        public Font getFont() {
            return this.font;
        }
        
        public float getX() {
            return this.x;
        }
        
        public float getY() {
            return this.y;
        }
        
        public float getWidth() {
            return this.width;
        }
        
        public float getHeight() {
            return this.height;
        }
        
        public float getOriginX() {
            return this.originX;
        }
        
        public float getOriginY() {
            return this.originY;
        }
        
        public float getAdvance() {
            return this.advance;
        }
        
        public float getU0() {
            return this.u0;
        }
        
        public float getV0() {
            return this.v0;
        }
        
        public float getU1() {
            return this.u1;
        }
        
        public float getV1() {
            return this.v1;
        }
    }
    
    
    /************************* Data-class *************************/
    
    public static class Data implements IAssetData {
    	Font targetFont;
    	JSONObject charactersJson;

		@Override
		public void assign(long timestamp) {
			for( String characterKey : this.charactersJson.keySet() ) {
				JSONObject characterJson = this.charactersJson.getJSONObject(characterKey);
				char character = characterKey.charAt(0);
				Font.Glyph glyph = new Font.Glyph(
					character,
					characterJson.getFloat("x"),
					characterJson.getFloat("y"),
					characterJson.getFloat("width"),
					characterJson.getFloat("height"),
					characterJson.getFloat("originX"),
					characterJson.getFloat("originY"),
					characterJson.getFloat("advance")
				);
				this.targetFont.addGlyph(character, glyph);
			}
			
			this.targetFont.initialize();
			this.targetFont.lastUpdateTimestamp = timestamp;
		}
    }
    
    
    /************************* Font-class *************************/
    
    
    
    private final String name;
    
    private long lastUpdateTimestamp;
    private Map<Character, Glyph> glyphs;
    private Glyph defaultGlyph;
    private Texture fontTexture;
    private float textureWidth;
    private float textureHeight;
    
    public Font(
		String name,
        Texture fontTexture,
        float textureWidth,
        float textureHeight
    ) {
    	this.name = name;
    	this.lastUpdateTimestamp = -1;
    	this.glyphs = new HashMap<>();
    	this.defaultGlyph = new Glyph((char) 0, 0, 0, 16, 16, 0, 0, 16);
        this.fontTexture = fontTexture;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }
    
    
    public void initialize() {
        for( Map.Entry<Character, Glyph> en : this.glyphs.entrySet() ) {
            Font.Glyph glyph = en.getValue();
            
            float glyphX = glyph.getX();
            float glyphY = glyph.getY();
            float glyphWidth = glyph.getWidth();
            float glyphHeight = glyph.getHeight();
            
            float x = 0.0f;
            float y = 0.0f;
            float w = glyphWidth;
            float h = glyphHeight;
            
            float u0 = glyphX / this.textureWidth;
            float v0 = glyphY / this.textureHeight;
            float u1 = u0 + glyphWidth / this.textureWidth;
            float v1 = v0 + glyphHeight / this.textureHeight;
            
            glyph.u0 = u0;
            glyph.v0 = v0;
            glyph.u1 = u1;
            glyph.v1 = v1;
            
            glyph.mesh = AssetUtils.createPlaneMesh(
        		"font-mesh-" + this.name, x, y, w, h, u0, v0, u1, v1
    		);
            glyph.font = this;
        }
    }
    
    public void addGlyph(char glyphCharacter, Font.Glyph glyph) {
        glyph.font = this;
        this.glyphs.put(glyphCharacter, glyph);
    }
    
    /************************ GETTERS ************************/
    
    public Font.Glyph getGlyph(char character) {
        return this.glyphs.getOrDefault(character, this.defaultGlyph);
    }
    
    public Texture getTexture() {
        return this.fontTexture;
    }
    
    @Override
    public String getName() {
    	return this.name;
    }
    
    @Override
    public long getLastUpdateTimestamp() {
    	return this.lastUpdateTimestamp;
    }
}

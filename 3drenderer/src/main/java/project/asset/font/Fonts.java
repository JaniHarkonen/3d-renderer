package project.asset.font;

import java.util.HashMap;
import java.util.Map;

import project.core.asset.IAsset;
import project.core.asset.ISystem;
import project.shared.logger.Logger;

public final class Fonts implements ISystem {

	private final Map<String, Map<Float, Font>> fontTable;
	
	public Fonts() {
		this.fontTable = new HashMap<>();
	}
	
	
	@Override
	public void assetLoaded(IAsset asset) {
		if( !(asset instanceof Font) ) {
			Logger.get().error(this, "Attempting to list a non-font asset to the font table.");
			return;
		}
		
		Font font = (Font) asset;
		Map<Float, Font> sizeTable = this.fontTable.getOrDefault(font.getName(), new HashMap<>());
		sizeTable.put(font.getSize(), font);
	}

	@Override
	public void assetDeloaded(IAsset asset) {
		if( !(asset instanceof Font) ) {
			Logger.get().warn(this, "Attempting to de-list a non-font asset from the font table.");
			return;
		}
		
		Font font = (Font) asset;
		String fontName = font.getName();
		float fontSize = font.getSize();
		Map<Float, Font> sizeTable = this.fontTable.get(fontName);
		
		if( sizeTable == null ) {
			Logger.get().error(this, "Unable to de-list font '" + fontName + "'. Font doesn't exist.");
			return;
		}
		
		sizeTable.remove(fontSize);
		
			// Also remove font entry from the font table if no size entries exist under that font
		if( sizeTable.size() == 0 ) {
			this.fontTable.remove(fontName);
		}
	}
	
	public Font getFont(String name, float size) {
		Map<Float, Font> sizeTable = this.fontTable.get(name);
		if( sizeTable == null ) {
			Logger.get().error(this, "Unable to find font '" + name + "'!");
			return null;
		}
		
		Font font = sizeTable.get(size);
		if( font == null ) {
			Logger.get().error(this, "Unable to find font '" + name + "' with size '" + size + "'!");
		}
		return font;
	}
}

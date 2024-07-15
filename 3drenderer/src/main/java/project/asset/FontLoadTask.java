package project.asset;

import org.json.JSONObject;

import project.utils.FileUtils;

public class FontLoadTask {

	private String fontConfigurationPath;
	private Font targetFont;
	
	public FontLoadTask(String fontConfigurationPath, Font targetFont) {
		this.fontConfigurationPath = fontConfigurationPath;
		this.targetFont = targetFont;
	}
	
	
	public void load() {
		String jsonString = FileUtils.readTextFile(this.fontConfigurationPath);
		JSONObject fontJson = new JSONObject(jsonString);
		JSONObject charactersJson = fontJson.getJSONObject("characters");
		
		for( String characterKey : charactersJson.keySet() ) {
			JSONObject characterJson = charactersJson.getJSONObject(characterKey);
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
	}
}

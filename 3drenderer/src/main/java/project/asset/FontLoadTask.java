package project.asset;

import org.json.JSONObject;

import project.utils.DebugUtils;
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
		DebugUtils.log(this, "Loaded font JSON", fontJson.toString());
	}
}

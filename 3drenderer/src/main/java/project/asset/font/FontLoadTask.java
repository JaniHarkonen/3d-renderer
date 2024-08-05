package project.asset.font;

import org.json.JSONObject;

import project.Application;
import project.core.asset.ILoadTask;
import project.utils.FileUtils;

public class FontLoadTask implements ILoadTask {

	private String fontConfigurationPath;
	private Font targetFont;
	
	public FontLoadTask(String fontConfigurationPath, Font targetFont) {
		this.fontConfigurationPath = fontConfigurationPath;
		this.targetFont = targetFont;
	}
	
	
	@Override
	public boolean load() {
		String jsonString = FileUtils.readTextFile(this.fontConfigurationPath);
		JSONObject fontJson = new JSONObject(jsonString);
		
		Font.Data data = new Font.Data();
		data.targetFont = this.targetFont;
		data.charactersJson = fontJson.getJSONObject("characters");
		Application.getApp().getAssetManager().notifyResult(this.targetFont, data);
		
		return true;
	}
}

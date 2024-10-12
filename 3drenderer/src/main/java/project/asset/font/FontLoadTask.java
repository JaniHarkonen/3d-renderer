package project.asset.font;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import project.Application;
import project.core.asset.IAsset;
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
		data.size = fontJson.getFloat("size");
		data.targetFont = this.targetFont;
		data.charactersJson = fontJson.getJSONObject("characters");
		Application.getApp().getAssetManager().notifyResult(
			this.targetFont, data, Application.getApp().getFontTable()
		);
		return true;
	}
	
	@Override
	public List<IAsset> getTargetAssets() {
		List<IAsset> assets = new ArrayList<>();
		assets.add(this.targetFont);
		return assets;
	}
}

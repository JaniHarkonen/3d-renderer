package project.testing;

import project.asset.Font;
import project.asset.FontLoadTask;
import project.opengl.Texture;
import project.utils.FileUtils;

public class Fonts {

	public static Font ARIAL_20;
	static {
		Texture fontTexture;
		
		fontTexture = new Texture(FileUtils.getResourcePath("font_arial20.png"));
		Fonts.ARIAL_20 = new Font(fontTexture, 230, 89);
		FontLoadTask task = new FontLoadTask(FileUtils.getResourcePath("arial20.json"), Fonts.ARIAL_20);
		task.load();
		Fonts.ARIAL_20.init();
	}
}

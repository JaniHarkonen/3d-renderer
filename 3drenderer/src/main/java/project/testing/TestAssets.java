package project.testing;

import project.asset.Font;
import project.asset.FontLoadTask;
import project.asset.Mesh;
import project.asset.SceneAssetLoadTask;
import project.opengl.Texture;
import project.utils.FileUtils;

public final class TestAssets {

		// Fonts
	public static Font FONT_ARIAL_20;
	static {
		Texture fontTexture;
		
		fontTexture = new Texture(FileUtils.getResourcePath("fonts/arial/font_arial20.png"));
		TestAssets.FONT_ARIAL_20 = new Font(fontTexture, 230, 89);
		FontLoadTask task = new FontLoadTask(FileUtils.getResourcePath("fonts/arial/arial20.json"), TestAssets.FONT_ARIAL_20);
		task.load();
		TestAssets.FONT_ARIAL_20.init();
	}
	
		// Meshes
	public static Mesh MESH_BRICK;
	static {
		TestAssets.MESH_BRICK = new Mesh();
		SceneAssetLoadTask task = new SceneAssetLoadTask(FileUtils.getResourcePath("models/Brick.fbx"));
		task.expectMesh(TestAssets.MESH_BRICK);
		task.load();
			// Will be initialized by VAOCache of Renderer
	}
	
		// Textures
	public static Texture TEXTURE_CREEP;
	public static Texture TEXTURE_BRICK;
	static {
		TestAssets.TEXTURE_CREEP = new Texture(FileUtils.getResourcePath("textures/creep.png"));
		TestAssets.TEXTURE_BRICK = new Texture(FileUtils.getResourcePath("textures/brick/Bricks082B_4K_Color.jpg"));
			// Will be initialized by TextureCache of Renderer
	}
}

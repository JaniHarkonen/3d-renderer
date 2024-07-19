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
	public static Font FONT_ARIAL_16;
	static {
		Texture fontTexture;
		
			// Arial, normal, size 20
		fontTexture = new Texture(
			FileUtils.getResourcePath("fonts/arial/size20/font_arial20.png")
		);
		FONT_ARIAL_20 = new Font(fontTexture, 230, 89);
		FontLoadTask task = new FontLoadTask(
			FileUtils.getResourcePath("fonts/arial/size20/arial20.json"), 
			FONT_ARIAL_20
		);
		task.load();
		FONT_ARIAL_20.init();
		
			// Arial, normal, size 16
		fontTexture = new Texture(
			FileUtils.getResourcePath("fonts/arial/size16/arial16.png")
		);
		FONT_ARIAL_16 = new Font(fontTexture, 178, 76);
		task = new FontLoadTask(
			FileUtils.getResourcePath("fonts/arial/size16/arial16.json"), 
			FONT_ARIAL_16
		);
		task.load();
		FONT_ARIAL_16.init();
	}
	
		// Meshes
	public static Mesh MESH_BRICK;
	public static Mesh[] MESH_OUTSIDE_PLACE;
	public static Mesh MESH_MAN;
	static {
		MESH_BRICK = new Mesh();
		SceneAssetLoadTask task = new SceneAssetLoadTask(
			FileUtils.getResourcePath("models/Brick.fbx")
		);
		task.expectMesh(MESH_BRICK);
		task.load();
		
		/*MESH_OUTSIDE_PLACE = new Mesh[29];
		for( int i = 0; i < MESH_OUTSIDE_PLACE.length; i++ ) {
			MESH_OUTSIDE_PLACE[i] = new Mesh();
		}
		
		task = new SceneAssetLoadTask(
			FileUtils.getResourcePath("models/Outside.fbx")
		);
		task.expectMesh(MESH_OUTSIDE_PLACE);
		task.load();*/
		
		MESH_MAN = new Mesh();
		task = new SceneAssetLoadTask(
			FileUtils.getResourcePath("models/man.fbx")
		);
		task.expectMesh(MESH_MAN);
		task.load();
			// Will be initialized by VAOCache of Renderer
	}
	
		// Textures
	public static Texture TEXTURE_CREEP;
	public static Texture TEXTURE_BRICK;
	public static Texture TEXTURE_BRICK_NORMAL;
	public static Texture TEXTURE_OUTSIDE_PAVEMENT1;
	public static Texture TEXTURE_OUTSIDE_CONCRETE_BLOCK1;
	public static Texture TEXTURE_OUTSIDE_METAL_DIRTYRUST;
	static {
		TestAssets.TEXTURE_CREEP = new Texture(
			FileUtils.getResourcePath("textures/creep.png")
		);
		TestAssets.TEXTURE_BRICK = new Texture(
			FileUtils.getResourcePath("textures/brick/Bricks082B_4K_Color.jpg")
		);
		TestAssets.TEXTURE_BRICK_NORMAL = new Texture(
			FileUtils.getResourcePath("textures/brick/Bricks082B_4K_NormalDX.jpg")
		);
		TestAssets.TEXTURE_OUTSIDE_PAVEMENT1 = new Texture(
			FileUtils.getResourcePath("textures/outside/pavement1_diff.png")
		); 
		TestAssets.TEXTURE_OUTSIDE_CONCRETE_BLOCK1 = new Texture(
			FileUtils.getResourcePath("textures/outside/conrete_block1_diff.png")
		);
		TestAssets.TEXTURE_OUTSIDE_METAL_DIRTYRUST = new Texture(
			FileUtils.getResourcePath("textures/outside/metal_dirtyrust_diff.png")
		); 
			// Will be initialized by TextureCache of Renderer
	}
}

package project.testing;

import project.asset.Animation;
import project.asset.Font;
import project.asset.FontLoadTask;
import project.asset.Mesh;
import project.asset.SceneAssetLoadTask;
import project.asset.TextureLoadTask;
import project.opengl.Texture;
import project.utils.FileUtils;

public final class TestAssets {

		// Fonts
	public static Font FONT_ARIAL_20;
	public static Font FONT_ARIAL_16;
	static {
		
			// Arial, normal, size 20
		FONT_ARIAL_20 = new Font(loadTexture("fonts/arial/size20/font_arial20.png"), 230, 89);
		FontLoadTask fontTask = new FontLoadTask(
			FileUtils.getResourcePath("fonts/arial/size20/arial20.json"), 
			FONT_ARIAL_20
		);
		fontTask.load();
		FONT_ARIAL_20.init();
		
			// Arial, normal, size 16
		FONT_ARIAL_16 = new Font(loadTexture("fonts/arial/size16/arial16.png"), 178, 76);
		fontTask = new FontLoadTask(
			FileUtils.getResourcePath("fonts/arial/size16/arial16.json"), 
			FONT_ARIAL_16
		);
		fontTask.load();
		FONT_ARIAL_16.init();
	}
	
		// Meshes
	public static Mesh MESH_BRICK;
	public static Mesh[] MESH_OUTSIDE_PLACE;
	public static Mesh MESH_MAN;
	public static Mesh MESH_JUMP;
	
	public static Animation ANIM_RUN;
	public static Animation ANIM_JUMP;
	static {
		MESH_BRICK = new Mesh();
		SceneAssetLoadTask task = new SceneAssetLoadTask(
			FileUtils.getResourcePath("models/Brick.fbx")
		);
		task.expectMesh(MESH_BRICK);
		task.load();
		/*
		MESH_OUTSIDE_PLACE = new Mesh[29];
		for( int i = 0; i < MESH_OUTSIDE_PLACE.length; i++ ) {
			MESH_OUTSIDE_PLACE[i] = new Mesh();
		}
		
		task = new SceneAssetLoadTask(
			FileUtils.getResourcePath("models/Outside.fbx")
		);
		task.expectMesh(MESH_OUTSIDE_PLACE);
		task.load();
		*/
		MESH_MAN = new Mesh();
		task = new SceneAssetLoadTask(
			FileUtils.getResourcePath("models/man.fbx")
		);
		task.expectMesh(MESH_MAN);
		ANIM_RUN = new Animation();
		task.expectAnimation(ANIM_RUN);
		task.load();
		
		//DebugUtils.log("[TestAssets::static]", man.getFrames().size());
		
		
		MESH_JUMP = new Mesh();
		task = new SceneAssetLoadTask(
			FileUtils.getResourcePath("models/JUmp.fbx")
		);
		task.expectMesh(MESH_JUMP);
		ANIM_JUMP = new Animation();
		task.expectAnimation(ANIM_JUMP);
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
		TEXTURE_CREEP = loadTexture("textures/creep.png");
		TEXTURE_BRICK = loadTexture("textures/brick/Bricks082B_4K_Color.jpg");
		TEXTURE_BRICK_NORMAL = loadTexture("textures/brick/Bricks082B_4K_NormalDX.jpg");
		
		/*
		TestAssets.TEXTURE_OUTSIDE_PAVEMENT1 = new Texture(
			FileUtils.getResourcePath("textures/outside/pavement1_diff.png")
		); 
		TestAssets.TEXTURE_OUTSIDE_CONCRETE_BLOCK1 = new Texture(
			FileUtils.getResourcePath("textures/outside/conrete_block1_diff.png")
		);
		TestAssets.TEXTURE_OUTSIDE_METAL_DIRTYRUST = new Texture(
			FileUtils.getResourcePath("textures/outside/metal_dirtyrust_diff.png")
		); */
			// Will be initialized by TextureCache of Renderer
	}
	
	private static Texture loadTexture(String relativePath) {
		String texturePath = FileUtils.getResourcePath(relativePath);
		Texture result = new Texture();
		TextureLoadTask task = new TextureLoadTask(texturePath, result);
		task.load();
		
		return result;
	}
}

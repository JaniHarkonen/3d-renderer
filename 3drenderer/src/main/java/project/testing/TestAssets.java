package project.testing;

import project.asset.Animation;
import project.asset.Font;
import project.asset.FontLoadTask;
import project.asset.Mesh;
import project.asset.SceneAssetLoadTask;
import project.asset.TextureLoadTask;
import project.component.Material;
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
		/*MESH_BRICK = new Mesh();
		SceneAssetLoadTask task = new SceneAssetLoadTask(
			FileUtils.getResourcePath("models/Brick.fbx")
		);
		task.expectMesh(MESH_BRICK);
		task.load();*/
		
		MESH_OUTSIDE_PLACE = new Mesh[29];
		for( int i = 0; i < MESH_OUTSIDE_PLACE.length; i++ ) {
			MESH_OUTSIDE_PLACE[i] = new Mesh();
		}
		loadSceneAsset("models/Outside.fbx", MESH_OUTSIDE_PLACE);
		
		/*MESH_MAN = new Mesh();
		task = new SceneAssetLoadTask(
			FileUtils.getResourcePath("models/man.fbx")
		);
		task.expectMesh(MESH_MAN);
		ANIM_RUN = new Animation();
		task.expectAnimation(ANIM_RUN);
		task.load();
		
		MESH_JUMP = new Mesh();
		task = new SceneAssetLoadTask(
			FileUtils.getResourcePath("models/JUmp.fbx")
		);
		task.expectMesh(MESH_JUMP);
		ANIM_JUMP = new Animation();
		task.expectAnimation(ANIM_JUMP);
		task.load();*/
			// Will be initialized by VAOCache of Renderer
	}
	
		// Materials
	public static Material MAT_TEST_RED;
	public static Material MAT_CREEP;
	public static Material MAT_BRICK;
	public static Material MAT_BRICK_NORMAL;
	public static Material MAT_OUTSIDE_PAVEMENT1;
	public static Material MAT_OUTSIDE_CONCRETE_BLOCK1;
	public static Material MAT_OUTSIDE_METAL_DIRTYRUST;
	public static Material MAT_OUTSIDE_CONCRETE_WALL1;
	public static Material MAT_OUTSIDE_CONCRETE_BARRIER_DIRTY;
	public static Material MAT_OUTSIDE_LAMPBOX_METAL;
	public static Material MAT_OUTSIDE_METAL_DOOR1;
	public static Material MAT_OUTSIDE_CONCRETE_WALL2;
	public static Material MAT_OUTSIDE_METAL_SKIRTING;
	public static Material MAT_OUTSIDE_WINDOW_BLINDS1;
	public static Material MAT_OUTSIDE_WOOD_PLANKS1;
	public static Material MAT_OUTSIDE_CONCRETE_BARRIER;
	public static Material MAT_OUTSIDE_BRICKS1;
	public static Material MAT_OUTSIDE_CONCRETE_MIX;
	public static Material MAT_OUTSIDE_GRAVEL1;
	public static Material MAT_OUTSIDE_ASPHALT1;
	public static Material MAT_OUTSIDE_LAMPPOST_METAL;
	public static Material MAT_OUTSIDE_WIRES;
	public static Material MAT_OUTSIDE_TREE1;
	public static Material MAT_OUTSIDE_GRAFFITI3;
	public static Material MAT_OUTSIDE_GRAFFITI1;
	public static Material MAT_OUTSIDE_GRAFFITI2;
	public static Material MAT_OUTSIDE_SNOW;
	public static Material MAT_OUTSIDE_DIRT_DECAL2;
	public static Material MAT_OUTSIDE_DIRT_DECAL1;
	public static Material MAT_OUTSIDE_BARREL_METAL;
	public static Material MAT_OUTSIDE_BARREL_TRASH;
	static {
		MAT_TEST_RED = createMaterial("textures/test.png");
		MAT_OUTSIDE_PAVEMENT1 = createMaterial("textures/outside/pavement1_diff.png");
		MAT_OUTSIDE_CONCRETE_BLOCK1 = createMaterial("textures/outside/conrete_block1_diff.png");
		MAT_OUTSIDE_METAL_DIRTYRUST = createMaterial("textures/outside/metal_dirtyrust_diff.png");
		MAT_OUTSIDE_CONCRETE_WALL1 = createMaterial("textures/outside/concrete_wall1_diff.png");
		MAT_OUTSIDE_CONCRETE_BARRIER_DIRTY = createMaterial("textures/outside/concrete_barrier_dirty_diff.png");
		MAT_OUTSIDE_LAMPBOX_METAL = createMaterial("textures/outside/lampbox_metal_diff.png");
		MAT_OUTSIDE_METAL_DOOR1 = createMaterial("textures/outside/metal_door1_diff.png");
		MAT_OUTSIDE_CONCRETE_WALL2 = createMaterial("textures/outside/concrete_wall2_diff.png");
		MAT_OUTSIDE_METAL_SKIRTING = createMaterial("textures/outside/metal_skirting_diff.png");
		MAT_OUTSIDE_WINDOW_BLINDS1 = createMaterial("textures/outside/window_blinds1.png");
		MAT_OUTSIDE_WOOD_PLANKS1 = createMaterial("textures/outside/wood_planks1_diff.png");
		MAT_OUTSIDE_CONCRETE_BARRIER = createMaterial("textures/outside/concrete_barrier_diff.png");
		MAT_OUTSIDE_BRICKS1 = createMaterial("textures/outside/bricks01_diff.png");
		MAT_OUTSIDE_CONCRETE_MIX = createMaterial("textures/outside/concrete_mix_diff.png");
		MAT_OUTSIDE_GRAVEL1 = createMaterial("textures/outside/gravel1_diff.png");
		MAT_OUTSIDE_ASPHALT1 = createMaterial("textures/outside/asphalt1_diff.png");
		MAT_OUTSIDE_LAMPPOST_METAL = createMaterial("textures/outside/lamppost_metal_diff.png");
		MAT_OUTSIDE_WIRES = createMaterial("textures/outside/wires_diff.png");
		MAT_OUTSIDE_TREE1 = createMaterial("textures/outside/tree1_diff.png");
		MAT_OUTSIDE_GRAFFITI3 = createMaterial("textures/outside/graffiti3_alpha.png");
		MAT_OUTSIDE_GRAFFITI1 = createMaterial("textures/outside/graffiti1_alpha.png");
		MAT_OUTSIDE_GRAFFITI2 = createMaterial("textures/outside/graffiti2_alpha.png");
		MAT_OUTSIDE_SNOW = createMaterial("textures/outside/snow_diff.png");
		MAT_OUTSIDE_DIRT_DECAL2 = createMaterial("textures/outside/dirt_decal2_diff.png");
		MAT_OUTSIDE_DIRT_DECAL1 = createMaterial("textures/outside/dirt_decal1_diff.png");
		MAT_OUTSIDE_BARREL_METAL = createMaterial("textures/outside/barrel_metal_diff.png");
		MAT_OUTSIDE_BARREL_TRASH = createMaterial("textures/outside/barrel_trash_diff.png");
	}
	
		// Textures
	public static Texture TEXTURE_CREEP;
	public static Texture TEXTURE_BRICK;
	public static Texture TEXTURE_BRICK_NORMAL;
	static {
		/*TEXTURE_CREEP = loadTexture("textures/creep.png");
		TEXTURE_BRICK = loadTexture("textures/brick/Bricks082B_4K_Color.jpg");
		TEXTURE_BRICK_NORMAL = loadTexture("textures/brick/Bricks082B_4K_NormalDX.jpg");*/
		
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
	
		// Materials
	//public static Material MAT_
	
	private static Material createMaterial(String relativeDiffusePath, String relativeNormalPath) {
		Texture diffuse = loadTexture(relativeDiffusePath);
		Texture normal = relativeNormalPath != null ? loadTexture(relativeNormalPath) : null;
		Material result = new Material();
		result.setTexture(0, diffuse);
		result.setTexture(1, normal);
		return result;
	}
	
	private static Material createMaterial(String relativeDiffusePath) {
		return createMaterial(relativeDiffusePath, null);
	}
	
	private static Texture loadTexture(String relativePath) {
		String texturePath = FileUtils.getResourcePath(relativePath);
		Texture result = new Texture();
		TextureLoadTask task = new TextureLoadTask(texturePath, result);
		task.load();
		
		return result;
	}
	
	private static void loadSceneAsset(
		String relativePath, Mesh[] expectedMeshes, Animation[] expectedAnimations
	) {
		SceneAssetLoadTask task = new SceneAssetLoadTask(
			FileUtils.getResourcePath(relativePath)
		);
		task.expectMesh(expectedMeshes);
		task.expectAnimation(expectedAnimations);
		task.load();
	}
	
	private static void loadSceneAsset(String relativePath, Mesh[] expectedMeshes) {
		loadSceneAsset(relativePath, expectedMeshes, new Animation[0]);
	}
	
	private static Mesh loadMesh(String relativePath) {
		Mesh result = new Mesh();
		loadSceneAsset(relativePath, new Mesh[] {result});
		return result;
	}
}

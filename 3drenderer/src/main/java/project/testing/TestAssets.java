package project.testing;

import project.asset.Animation;
import project.asset.AnimationData;
import project.asset.Font;
import project.asset.FontLoadTask;
import project.asset.Mesh;
import project.asset.SceneAssetLoadTask;
import project.asset.TextureLoadTask;
import project.component.Material;
import project.opengl.Texture;
import project.scene.Model;
import project.scene.Scene;
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
	public static Mesh[] MESH_SOLDIER;
	
	public static Animation ANIM_RUN;
	public static Animation ANIM_JUMP;
	public static Animation ANIM_SOLDIER_IDLE;
	static {
		MESH_OUTSIDE_PLACE = createMeshArray(29);
		loadSceneAsset("models/Outside.fbx", MESH_OUTSIDE_PLACE);
		
		ANIM_SOLDIER_IDLE = new Animation();
		MESH_SOLDIER = createMeshArray(8);
		loadSceneAsset("models/soldier.fbx", MESH_SOLDIER, new Animation[] {ANIM_SOLDIER_IDLE});
		
		Mesh[] array = createMeshArray(1);
		ANIM_RUN = new Animation();
		loadSceneAsset("models/man.fbx", array, new Animation[] {ANIM_RUN});
		MESH_MAN = array[0];
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
	public static Material MAT_SOLDIER_EYELASH;
	
	public static Material MAT_SOLDIER_HEAD;
	public static Material MAT_SOLDIER_BODY;
	public static Material MAT_SOLDIER_VEST;
	public static Material MAT_SOLDIER_HELMET;
	public static Material MAT_SOLDIER_GADGETS;
	public static Material MAT_SOLDIER_EYES;
	static {
		MAT_SOLDIER_HEAD = createMaterialWithNormal(
			"textures/soldier",
			"head"
		);
		MAT_SOLDIER_BODY = createMaterial(
			"textures/soldier/diffuse/body_diff.png",
			"textures/soldier/normal/body_normal.png"
		);
		MAT_SOLDIER_VEST = createMaterialWithNormal(
			"textures/soldier",
			"vest"
		);
		MAT_SOLDIER_HELMET = createMaterialWithNormal(
			"textures/soldier/",
			"helmet"
		);
		MAT_SOLDIER_GADGETS = createMaterialWithNormal(
			"textures/soldier",
			"gadgets"
		);
		MAT_SOLDIER_EYES = createMaterialWithNormal(
			"textures/soldier",
			"eyes"
		);
		MAT_SOLDIER_EYELASH = createMaterial(
			"textures/soldier/diffuse/eyelash_diff.png"
		);
		
		MAT_TEST_RED = createMaterial("textures/test.png");
		MAT_OUTSIDE_PAVEMENT1 = createMaterialWithNormal(
			"textures/outside",
			"pavement1"
		);
		MAT_OUTSIDE_CONCRETE_BLOCK1 = createMaterialWithNormal(
			"textures/outside",
			"conrete_block1"
		);
		MAT_OUTSIDE_METAL_DIRTYRUST = createMaterialWithNormal(
			"textures/outside",
			"metal_dirtyrust"
		);
		MAT_OUTSIDE_CONCRETE_WALL1 = createMaterialWithNormal(
			"textures/outside/",
			"concrete_wall1"
		);
		MAT_OUTSIDE_CONCRETE_BARRIER_DIRTY = createMaterial("textures/outside/diffuse/concrete_barrier_dirty_diff.png");
		MAT_OUTSIDE_LAMPBOX_METAL = createMaterial("textures/outside/diffuse/lampbox_metal_diff.png");
		MAT_OUTSIDE_METAL_DOOR1 = createMaterialWithNormal(
			"textures/outside",
			"metal_door1"
		);
		MAT_OUTSIDE_CONCRETE_WALL2 = createMaterial("textures/outside/diffuse/concrete_wall2_diff.png");
		MAT_OUTSIDE_METAL_SKIRTING = createMaterial("textures/outside/diffuse/metal_skirting_diff.png");
		MAT_OUTSIDE_WINDOW_BLINDS1 = createMaterial("textures/outside/diffuse/window_blinds1.png");
		MAT_OUTSIDE_WOOD_PLANKS1 = createMaterialWithNormal(
			"textures/outside",
			"wood_planks1"
		);
		MAT_OUTSIDE_CONCRETE_BARRIER = createMaterialWithNormal(
			"textures/outside",
			"concrete_barrier"
		);
		MAT_OUTSIDE_BRICKS1 = createMaterialWithNormal(
			"textures/outside",
			"bricks01"
		);
		MAT_OUTSIDE_CONCRETE_MIX = createMaterialWithNormal(
			"textures/outside",
			"concrete_mix"
		);
		MAT_OUTSIDE_GRAVEL1 = createMaterialWithNormal(
			"textures/outside",
			"gravel1"
		);
		MAT_OUTSIDE_ASPHALT1 = createMaterialWithNormal(
			"textures/outside",
			"asphalt1"
		);
		MAT_OUTSIDE_LAMPPOST_METAL = createMaterialWithNormal(
			"textures/outside",
			"lamppost_metal"
		);
		MAT_OUTSIDE_WIRES = createMaterial("textures/outside/diffuse/wires_diff.png");
		MAT_OUTSIDE_TREE1 = createMaterialWithNormal(
			"textures/outside",
			"tree1"
		);
		MAT_OUTSIDE_GRAFFITI3 = createMaterial("textures/outside/diffuse/graffiti3_alpha.png");
		MAT_OUTSIDE_GRAFFITI1 = createMaterial("textures/outside/diffuse/graffiti1_alpha.png");
		MAT_OUTSIDE_GRAFFITI2 = createMaterial("textures/outside/diffuse/graffiti2_alpha.png");
		MAT_OUTSIDE_SNOW = createMaterialWithNormal(
			"textures/outside",
			"snow"
		);
		MAT_OUTSIDE_DIRT_DECAL2 = createMaterial("textures/outside/diffuse/dirt_decal2_diff.png");
		MAT_OUTSIDE_DIRT_DECAL1 = createMaterial("textures/outside/diffuse/dirt_decal1_diff.png");
		MAT_OUTSIDE_BARREL_METAL = createMaterialWithNormal(
			"textures/outside",
			"barrel_metal"
		);
		MAT_OUTSIDE_BARREL_TRASH = createMaterialWithNormal(
			"textures/outside",
			"barrel_trash"
		); 
	}
	
	public static Texture TEX_GUI_CROSSHAIR;
	static {
		TEX_GUI_CROSSHAIR = TestAssets.loadTexture("textures/crosshair.png");
	}
	
	public static Model createTestSceneOutside(Scene scene) {
		Model model = new Model(scene);
		model.addMesh(TestAssets.MESH_OUTSIDE_PLACE[0], TestAssets.MAT_OUTSIDE_PAVEMENT1);
		model.addMesh(TestAssets.MESH_OUTSIDE_PLACE[1], TestAssets.MAT_OUTSIDE_CONCRETE_BLOCK1);
		model.addMesh(TestAssets.MESH_OUTSIDE_PLACE[2], TestAssets.MAT_OUTSIDE_METAL_DIRTYRUST);
		model.addMesh(TestAssets.MESH_OUTSIDE_PLACE[3], TestAssets.MAT_OUTSIDE_CONCRETE_WALL1);
		model.addMesh(TestAssets.MESH_OUTSIDE_PLACE[4], TestAssets.MAT_OUTSIDE_CONCRETE_BARRIER_DIRTY);
		model.addMesh(TestAssets.MESH_OUTSIDE_PLACE[5], TestAssets.MAT_OUTSIDE_LAMPBOX_METAL);
		model.addMesh(TestAssets.MESH_OUTSIDE_PLACE[6], TestAssets.MAT_OUTSIDE_METAL_DOOR1);
		model.addMesh(TestAssets.MESH_OUTSIDE_PLACE[7], TestAssets.MAT_OUTSIDE_CONCRETE_WALL2);
		model.addMesh(TestAssets.MESH_OUTSIDE_PLACE[8], TestAssets.MAT_OUTSIDE_METAL_SKIRTING);
		model.addMesh(TestAssets.MESH_OUTSIDE_PLACE[9], TestAssets.MAT_OUTSIDE_WINDOW_BLINDS1);
		model.addMesh(TestAssets.MESH_OUTSIDE_PLACE[10], TestAssets.MAT_OUTSIDE_WOOD_PLANKS1);
		model.addMesh(TestAssets.MESH_OUTSIDE_PLACE[11], TestAssets.MAT_OUTSIDE_CONCRETE_BARRIER);
		model.addMesh(TestAssets.MESH_OUTSIDE_PLACE[12], TestAssets.MAT_TEST_RED);
		model.addMesh(TestAssets.MESH_OUTSIDE_PLACE[13], TestAssets.MAT_TEST_RED);
		model.addMesh(TestAssets.MESH_OUTSIDE_PLACE[14], TestAssets.MAT_OUTSIDE_BRICKS1);
		model.addMesh(TestAssets.MESH_OUTSIDE_PLACE[15], TestAssets.MAT_OUTSIDE_CONCRETE_MIX);
		model.addMesh(TestAssets.MESH_OUTSIDE_PLACE[16], TestAssets.MAT_OUTSIDE_GRAVEL1);
		model.addMesh(TestAssets.MESH_OUTSIDE_PLACE[17], TestAssets.MAT_OUTSIDE_ASPHALT1);
		model.addMesh(TestAssets.MESH_OUTSIDE_PLACE[18], TestAssets.MAT_OUTSIDE_LAMPPOST_METAL);
		model.addMesh(TestAssets.MESH_OUTSIDE_PLACE[19], TestAssets.MAT_OUTSIDE_WIRES);
		model.addMesh(TestAssets.MESH_OUTSIDE_PLACE[20], TestAssets.MAT_OUTSIDE_TREE1);
		model.addMesh(TestAssets.MESH_OUTSIDE_PLACE[21], TestAssets.MAT_OUTSIDE_GRAFFITI3);
		model.addMesh(TestAssets.MESH_OUTSIDE_PLACE[22], TestAssets.MAT_OUTSIDE_GRAFFITI1);
		model.addMesh(TestAssets.MESH_OUTSIDE_PLACE[23], TestAssets.MAT_OUTSIDE_GRAFFITI1);
		model.addMesh(TestAssets.MESH_OUTSIDE_PLACE[24], TestAssets.MAT_OUTSIDE_SNOW);
		model.addMesh(TestAssets.MESH_OUTSIDE_PLACE[25], TestAssets.MAT_OUTSIDE_DIRT_DECAL2);
		model.addMesh(TestAssets.MESH_OUTSIDE_PLACE[26], TestAssets.MAT_OUTSIDE_DIRT_DECAL1);
		model.addMesh(TestAssets.MESH_OUTSIDE_PLACE[27], TestAssets.MAT_OUTSIDE_BARREL_METAL);
		model.addMesh(TestAssets.MESH_OUTSIDE_PLACE[28], TestAssets.MAT_OUTSIDE_BARREL_TRASH);
		return model;
	}
	
	public static Model createTestSoldier(Scene scene) {
		Model model = new Model(scene);
		//model.addMesh(TestAssets.MESH_SOLDIER[0], TestAssets.MAT_TEST_RED);
		model.addMesh(TestAssets.MESH_SOLDIER[1], TestAssets.MAT_SOLDIER_HEAD);
		model.addMesh(TestAssets.MESH_SOLDIER[2], TestAssets.MAT_SOLDIER_BODY);
		model.addMesh(TestAssets.MESH_SOLDIER[3], TestAssets.MAT_SOLDIER_VEST);
		model.addMesh(TestAssets.MESH_SOLDIER[4], TestAssets.MAT_SOLDIER_HELMET);
		model.addMesh(TestAssets.MESH_SOLDIER[5], TestAssets.MAT_SOLDIER_GADGETS);
		model.addMesh(TestAssets.MESH_SOLDIER[6], TestAssets.MAT_SOLDIER_EYES);
		model.addMesh(TestAssets.MESH_SOLDIER[7], TestAssets.MAT_SOLDIER_EYELASH);
		model.setAnimationData(new AnimationData(TestAssets.ANIM_SOLDIER_IDLE));
		return model;
	}
	
	private static Mesh[] createMeshArray(int meshCount) {
		Mesh[] result = new Mesh[meshCount];
		for( int i = 0; i < result.length; i++ ) {
			result[i] = new Mesh();
		}
		return result;
	}
	
	private static Material createMaterial(String relativeDiffusePath, String relativeNormalPath) {
		Texture diffuse = loadTexture(relativeDiffusePath);
		Texture normal = relativeNormalPath != null ? loadTexture(relativeNormalPath) : null;
		Material result = new Material();
		result.setTexture(0, diffuse);
		result.setTexture(1, normal);
		return result;
	}
	
	private static Material createMaterialWithNormal(String relativePath, String name) {
		return createMaterial(
			relativePath + "/diffuse/" + name + "_diff.png", 
			relativePath + "/normal/" + name + "_norm.png"
		);
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

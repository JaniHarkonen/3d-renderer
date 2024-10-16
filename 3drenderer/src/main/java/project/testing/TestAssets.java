package project.testing;

import project.Application;
import project.asset.font.Font;
import project.asset.font.FontLoadTask;
import project.asset.sceneasset.Animation;
import project.asset.sceneasset.Mesh;
import project.asset.sceneasset.SceneAssetLoadTask;
import project.asset.texture.Texture;
import project.asset.texture.TextureLoadTask;
import project.component.Material;
import project.scene.Model;
import project.scene.Scene;
import project.utils.FileUtils;

public final class TestAssets {
	
		// Fonts
	public static Font FONT_ARIAL_20;
	public static Font FONT_ARIAL_16;
	
		// Meshes
	public static Mesh MESH_BRICK;
	public static Mesh[] MESH_OUTSIDE_PLACE;
	public static Mesh MESH_MAN;
	public static Mesh MESH_JUMP;
	public static Mesh[] MESH_SOLDIER;
	
	public static Animation ANIM_RUN;
	public static Animation ANIM_JUMP;
	public static Animation ANIM_SOLDIER_IDLE;
	
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
	
	public static Texture TEX_UI_CROSSHAIR;
	
	public static void initialize() {
		initFonts();
		initMeshes();
		initMaterials();
		initTextures();
	}
	
	public static void initFonts() {
		FONT_ARIAL_20 = loadFont("font-arial-20", "fonts/arial/size20/arial20", 230, 89, 20);
		FONT_ARIAL_16 = loadFont("font-arial-16", "fonts/arial/size16/arial16", 178, 76, 16);
	}
	
	public static void initMeshes() {
		MESH_OUTSIDE_PLACE = createMeshArray("mesh-outside-place", 29);
		loadSceneAsset("models/Outside.fbx", MESH_OUTSIDE_PLACE);
		
		ANIM_SOLDIER_IDLE = new Animation("anim-soldier-idle", 39);
		MESH_SOLDIER = createMeshArray("mesh-soldier", 8);
		loadSceneAsset("models/soldier.fbx", MESH_SOLDIER, new Animation[] {ANIM_SOLDIER_IDLE});
		
		//Mesh[] array = createMeshArray("mesh-man", 1);
		//ANIM_RUN = new Animation();
		//loadSceneAsset("models/man.fbx", array, new Animation[] {ANIM_RUN});
		//MESH_MAN = array[0];
	}
	
	
	public static void initMaterials() {
		MAT_SOLDIER_HEAD = createMaterialWithNormal(
			"mat-soldier-head",
			"tex-soldier-head",
			"textures/soldier",
			"head"
		);
		MAT_SOLDIER_HEAD.setTexture(2, loadTexture("tex-soldier-head-rough", "textures/soldier/rough/head_rough.png"));
		MAT_SOLDIER_BODY = createMaterial(
			"mat-soldier-body",
			"tex-soldier-body-diffuse",
			"tex-soldier-body-normal",
			"textures/soldier/diffuse/body_diff.png",
			"textures/soldier/normal/body_normal.png"
		);
		MAT_SOLDIER_BODY.setTexture(2, loadTexture("tex-soldier-body-rough", "textures/soldier/rough/body_rough.png"));
		MAT_SOLDIER_VEST = createMaterialWithNormal(
			"mat-soldier-vest",
			"tex-soldier-vest",
			"textures/soldier",
			"vest"
		);
		MAT_SOLDIER_VEST.setTexture(2, loadTexture("tex-soldier-vest-rough", "textures/soldier/rough/vest_rough.png"));
		MAT_SOLDIER_HELMET = createMaterialWithNormal(
			"mat-soldier-helmet",
			"tex-soldier-helmet",
			"textures/soldier/",
			"helmet"
		);
		MAT_SOLDIER_HELMET.setTexture(2, loadTexture("tex-soldier-helmet-rough", "textures/soldier/rough/helmet_rough.png"));
		MAT_SOLDIER_GADGETS = createMaterialWithNormal(
			"mat-soldier-gadgets",
			"tex-soldier-gadgets",
			"textures/soldier",
			"gadgets"
		);
		MAT_SOLDIER_GADGETS.setTexture(2, loadTexture("tex-soldier-gadgets-rough", "textures/soldier/rough/gadgets_rough.png"));
		MAT_SOLDIER_EYES = createMaterialWithNormal(
			"mat-soldier-eyes",
			"tex-soldier-eyes",
			"textures/soldier",
			"eyes"
		);
		MAT_SOLDIER_EYELASH = createMaterial(
			"mat-soldier-eyelash",
			"tex-soldier-eyelash",
			"textures/soldier/diffuse/eyelash_diff.png"
		);
		
		MAT_TEST_RED = createMaterial(
			"mat-test-red",
			"tex-test-red",
			"textures/test.png"
		);
		MAT_OUTSIDE_PAVEMENT1 = createMaterialWithNormal(
			"mat-outside-pavement1",
			"tex-outside-pavement1",
			"textures/outside",
			"pavement1"
		);
		MAT_OUTSIDE_CONCRETE_BLOCK1 = createMaterialWithNormal(
			"mat-outside-concrete-block1",
			"tex-outside-concrete-block1",
			"textures/outside",
			"conrete_block1"
		);
		MAT_OUTSIDE_METAL_DIRTYRUST = createMaterialWithNormal(
			"mat-outside-metal-dirtyrust",
			"tex-outside-metal-dirtyrust",
			"textures/outside",
			"metal_dirtyrust"
		);
		MAT_OUTSIDE_CONCRETE_WALL1 = createMaterialWithNormal(
			"mat-outside-concrete-wall1",
			"tex-outside-concrete-wall1",
			"textures/outside/",
			"concrete_wall1"
		);
		MAT_OUTSIDE_CONCRETE_BARRIER_DIRTY = createMaterial(
			"mat-outside-concrete-barrier-dirty",
			"tex-outside-concrete-barrier-dirty",
			"textures/outside/diffuse/concrete_barrier_dirty_diff.png"
		);
		MAT_OUTSIDE_LAMPBOX_METAL = createMaterial(
			"mat-outside-lampbox-metal",
			"tex-outside-lampbox-metal",
			"textures/outside/diffuse/lampbox_metal_diff.png"
		);
		MAT_OUTSIDE_LAMPBOX_METAL.setTexture(2, loadTexture("tex-outside-lampbox-metal-rough", "textures/outside/rough/lampbox_metal_rough.png"));
		MAT_OUTSIDE_METAL_DOOR1 = createMaterialWithNormal(
			"mat-outside-metal-door1",
			"tex-outside-metal-door1",
			"textures/outside",
			"metal_door1"
		);
		MAT_OUTSIDE_METAL_DOOR1.setTexture(2, loadTexture("tex-outside-metal-door1-rough", "textures/outside/rough/metal_door1_rough.png"));
		MAT_OUTSIDE_CONCRETE_WALL2 = createMaterial(
			"mat-outside-concrete-wall2",
			"tex-outside-concrete-wall2",
			"textures/outside/diffuse/concrete_wall2_diff.png"
		);
		MAT_OUTSIDE_METAL_SKIRTING = createMaterial(
			"mat-outside-metal-skirting",
			"tex-outside-metal-skirting",
			"textures/outside/diffuse/metal_skirting_diff.png"
		);
		MAT_OUTSIDE_WINDOW_BLINDS1 = createMaterial(
			"mat-outside-window-blinds1",
			"tex-outside-window-blinds1",
			"textures/outside/diffuse/window_blinds1.png"
		);
		MAT_OUTSIDE_WOOD_PLANKS1 = createMaterialWithNormal(
			"mat-outside-wood-planks1",
			"tex-outside-wood-planks1",
			"textures/outside",
			"wood_planks1"
		);
		MAT_OUTSIDE_WOOD_PLANKS1.setTexture(2, loadTexture("tex-outside-wood-planks1-rough", "textures/outside/rough/wood_planks1_rough.png"));
		MAT_OUTSIDE_CONCRETE_BARRIER = createMaterialWithNormal(
			"mat-outside-wood-planks1",
			"tex-outside-wood-planks1",
			"textures/outside",
			"concrete_barrier"
		);
		MAT_OUTSIDE_BRICKS1 = createMaterialWithNormal(
			"mat-outside-bricks1",
			"tex-outside-bricks1",
			"textures/outside",
			"bricks01"
		);
		MAT_OUTSIDE_BRICKS1.setTexture(2, loadTexture("tex-outside-bricks1-rough", "textures/outside/rough/bricks01_rough.png"));
		MAT_OUTSIDE_CONCRETE_MIX = createMaterialWithNormal(
			"mat-outside-concrete-mix",
			"tex-outside-concrete-mix",
			"textures/outside",
			"concrete_mix"
		);
		MAT_OUTSIDE_GRAVEL1 = createMaterialWithNormal(
			"mat-outside-gravel1",
			"tex-outside-gravel1",
			"textures/outside",
			"gravel1"
		);
		MAT_OUTSIDE_ASPHALT1 = createMaterialWithNormal(
			"mat-outside-asphalt1",
			"tex-outside-asphalt1",
			"textures/outside",
			"asphalt1"
		);
		MAT_OUTSIDE_ASPHALT1.setTexture(2, loadTexture("tex-outside-asphalt1-rough", "textures/outside/rough/asphalt1_rough.png"));
		MAT_OUTSIDE_LAMPPOST_METAL = createMaterialWithNormal(
			"mat-outside-lamppost-metal",
			"tex-outside-lamppost-metal",
			"textures/outside",
			"lamppost_metal"
		);
		MAT_OUTSIDE_WIRES = createMaterial(
			"mat-outside-wires",
			"tex-outside-wires",
			"textures/outside/diffuse/wires_diff.png"
		);
		MAT_OUTSIDE_TREE1 = createMaterialWithNormal(
			"mat-outside-tree1",
			"tex-outside-tree1",
			"textures/outside",
			"tree1"
		);
		MAT_OUTSIDE_GRAFFITI3 = createMaterial(
			"mat-outside-graffiti3",
			"tex-outside-graffiti3",
			"textures/outside/diffuse/graffiti3_alpha.png"
		);
		MAT_OUTSIDE_GRAFFITI1 = createMaterial(
			"mat-outside-graffiti1",
			"tex-outside-graffiti1",
			"textures/outside/diffuse/graffiti1_alpha.png"
		);
		MAT_OUTSIDE_GRAFFITI2 = createMaterial(
			"mat-outside-graffiti2",
			"tex-outside-graffiti2",
			"textures/outside/diffuse/graffiti2_alpha.png"
		);
		MAT_OUTSIDE_SNOW = createMaterialWithNormal(
			"mat-outside-snow",
			"tex-outside-snow",
			"textures/outside",
			"snow"
		);
		MAT_OUTSIDE_SNOW.setTexture(2, loadTexture("tex-outside-snow-rough", "textures/outside/rough/snow_rough.png"));
		MAT_OUTSIDE_DIRT_DECAL2 = createMaterial(
			"mat-outside-dirt-decal2",
			"tex-outside-dirt-decal2",
			"textures/outside/diffuse/dirt_decal2_diff.png"
		);
		MAT_OUTSIDE_DIRT_DECAL1 = createMaterial(
			"mat-outside-dirt-decal1",
			"tex-outside-dirt-decal1",
			"textures/outside/diffuse/dirt_decal1_diff.png"
		);
		MAT_OUTSIDE_BARREL_METAL = createMaterialWithNormal(
			"mat-outside-barrel-metal",
			"tex-outside-barrel-metal",
			"textures/outside",
			"barrel_metal"
		);
		MAT_OUTSIDE_BARREL_METAL.setTexture(2, loadTexture("tex-outside-barrel-metal-rough", "textures/outside/rough/barrel_metal_rough.png"));
		MAT_OUTSIDE_BARREL_TRASH = createMaterialWithNormal(
			"mat-outside-barrel-trash",
			"tex-outside-barrel-trash",
			"textures/outside",
			"barrel_trash"
		); 
	}
	
	public static void initTextures() {
		TEX_UI_CROSSHAIR = loadTexture("tex-crosshair", "textures/crosshair.png");
	}
	
	public static Model createTestSceneOutside(Scene scene) {
		Model model = new Model(scene);
		model.addMesh(MESH_OUTSIDE_PLACE[0], MAT_OUTSIDE_PAVEMENT1);
		model.addMesh(MESH_OUTSIDE_PLACE[1], MAT_OUTSIDE_CONCRETE_BLOCK1);
		model.addMesh(MESH_OUTSIDE_PLACE[2], MAT_OUTSIDE_METAL_DIRTYRUST);
		model.addMesh(MESH_OUTSIDE_PLACE[3], MAT_OUTSIDE_CONCRETE_WALL1);
		model.addMesh(MESH_OUTSIDE_PLACE[4], MAT_OUTSIDE_CONCRETE_BARRIER_DIRTY);
		model.addMesh(MESH_OUTSIDE_PLACE[5], MAT_OUTSIDE_LAMPBOX_METAL);
		model.addMesh(MESH_OUTSIDE_PLACE[6], MAT_OUTSIDE_METAL_DOOR1);
		model.addMesh(MESH_OUTSIDE_PLACE[7], MAT_OUTSIDE_CONCRETE_WALL2);
		model.addMesh(MESH_OUTSIDE_PLACE[8], MAT_OUTSIDE_METAL_SKIRTING);
		model.addMesh(MESH_OUTSIDE_PLACE[9], MAT_OUTSIDE_WINDOW_BLINDS1);
		model.addMesh(MESH_OUTSIDE_PLACE[10], MAT_OUTSIDE_WOOD_PLANKS1);
		model.addMesh(MESH_OUTSIDE_PLACE[11], MAT_OUTSIDE_CONCRETE_BARRIER);
		model.addMesh(MESH_OUTSIDE_PLACE[12], MAT_TEST_RED);
		model.addMesh(MESH_OUTSIDE_PLACE[13], MAT_TEST_RED);
		model.addMesh(MESH_OUTSIDE_PLACE[14], MAT_OUTSIDE_BRICKS1);
		model.addMesh(MESH_OUTSIDE_PLACE[15], MAT_OUTSIDE_CONCRETE_MIX);
		model.addMesh(MESH_OUTSIDE_PLACE[16], MAT_OUTSIDE_GRAVEL1);
		model.addMesh(MESH_OUTSIDE_PLACE[17], MAT_OUTSIDE_ASPHALT1);
		model.addMesh(MESH_OUTSIDE_PLACE[18], MAT_OUTSIDE_LAMPPOST_METAL);
		model.addMesh(MESH_OUTSIDE_PLACE[19], MAT_OUTSIDE_WIRES);
		model.addMesh(MESH_OUTSIDE_PLACE[20], MAT_OUTSIDE_TREE1);
		model.addMesh(MESH_OUTSIDE_PLACE[21], MAT_OUTSIDE_GRAFFITI3);
		model.addMesh(MESH_OUTSIDE_PLACE[22], MAT_OUTSIDE_GRAFFITI1);
		model.addMesh(MESH_OUTSIDE_PLACE[23], MAT_OUTSIDE_GRAFFITI1);
		model.addMesh(MESH_OUTSIDE_PLACE[24], MAT_OUTSIDE_SNOW);
		model.addMesh(MESH_OUTSIDE_PLACE[25], MAT_OUTSIDE_DIRT_DECAL2);
		model.addMesh(MESH_OUTSIDE_PLACE[26], MAT_OUTSIDE_DIRT_DECAL1);
		model.addMesh(MESH_OUTSIDE_PLACE[27], MAT_OUTSIDE_BARREL_METAL);
		model.addMesh(MESH_OUTSIDE_PLACE[28], MAT_OUTSIDE_BARREL_TRASH);
		return model;
	}
	
	public static Model createTestSoldier(Scene scene) {
		Model model = new Model(scene);
		//model.addMesh(MESH_SOLDIER[0], TestAssets.MAT_TEST_RED);
		model.addMesh(MESH_SOLDIER[1], MAT_SOLDIER_HEAD);
		model.addMesh(MESH_SOLDIER[2], MAT_SOLDIER_BODY);
		model.addMesh(MESH_SOLDIER[3], MAT_SOLDIER_VEST);
		model.addMesh(MESH_SOLDIER[4], MAT_SOLDIER_HELMET);
		model.addMesh(MESH_SOLDIER[5], MAT_SOLDIER_GADGETS);
		model.addMesh(MESH_SOLDIER[6], MAT_SOLDIER_EYES);
		model.addMesh(MESH_SOLDIER[7], MAT_SOLDIER_EYELASH);
		model.getAnimator().setAnimation(ANIM_SOLDIER_IDLE);
		return model;
	}
	
	private static Mesh[] createMeshArray(String namePrefix, int meshCount) {
		Mesh[] result = new Mesh[meshCount];
		for( int i = 0; i < result.length; i++ ) {
			result[i] = new Mesh(namePrefix + "-" + i);
		}
		return result;
	}
	
	private static Material createMaterial(
		String materialName, 
		String diffuseName, 
		String normalName, 
		String relativeDiffusePath, 
		String relativeNormalPath
	) {
		Texture diffuse = loadTexture(diffuseName, relativeDiffusePath);
		Texture normal = relativeNormalPath != null ? loadTexture(normalName, relativeNormalPath) : null;
		Material result = new Material(materialName);
		result.setTexture(0, diffuse);
		result.setTexture(1, normal);
		return result;
	}
	
	private static Material createMaterialWithNormal(
		String materialName, String textureNamePrefix, String relativePath, String fileName
	) {
		return createMaterial(
			materialName,
			textureNamePrefix + "-diffuse",
			textureNamePrefix + "-normal",
			relativePath + "/diffuse/" + fileName + "_diff.png", 
			relativePath + "/normal/" + fileName + "_norm.png"
		);
	}
	
	private static Material createMaterial(
		String materialName, String textureNamePrefix, String relativeDiffusePath
	) {
		return createMaterial(materialName, textureNamePrefix + "-diffuse", null, relativeDiffusePath, null);
	}
	
	private static Texture loadTexture(String name, String relativePath) {
		String texturePath = FileUtils.getResourcePath(relativePath);
		Texture result = new Texture(name);
		TextureLoadTask task = new TextureLoadTask(texturePath, result);
		Application.getApp().getAssetManager().scheduleLoadTask(task);
		
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
		Application.getApp().getAssetManager().scheduleLoadTask(task);
	}
	
	private static void loadSceneAsset(String relativePath, Mesh[] expectedMeshes) {
		loadSceneAsset(relativePath, expectedMeshes, new Animation[0]);
	}
	
	@SuppressWarnings("unused")
	private static Mesh loadMesh(String name, String relativePath) {
		Mesh result = new Mesh(name);
		loadSceneAsset(relativePath, new Mesh[] {result});
		return result;
	}
	
	private static Font loadFont(String name, String relativePath, int textureWidth, int textureHeight, float fontSize) {
		Font result = new Font(
			name, 
			"0123456789 !\\\"#$%&'()*+,-./:;<=>?@" + 
			"ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\\\]^_`" + 
			"abcdefghijklmnopqrstuvwxyz{|}~", 
			loadTexture("tex-" + name, relativePath + ".png"), 
			textureWidth, textureHeight, fontSize
		);
		
		FontLoadTask task = new FontLoadTask(
			FileUtils.getResourcePath(relativePath + ".json"), 
			result
		);
		Application.getApp().getAssetManager().scheduleLoadTask(task);
		return result;
	}
}

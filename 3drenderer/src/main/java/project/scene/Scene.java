package project.scene;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import project.Application;
import project.Window;
import project.component.Projection;
import project.controls.Controller;
import project.gui.GUI;
import project.gui.Text;
import project.input.Input;
import project.testing.ActionSet;
import project.testing.DebugModel;
import project.testing.TestAssets;
import project.utils.DebugUtils;

public class Scene {
	private List<ASceneObject> objects;
	private Camera activeCamera;
	private GUI gui;
	private long deltaTimer;
	private long tickDelta;
	private int tickRate;
	private Application app;
	private Text textAppStatistics;
	private PointLight pointLight0;
	private DebugModel floorBrick;
	private Vector3f shadowLightPosition;
	
	public Scene(Application app, int tickRate) {
		this.objects = null;
		this.activeCamera = null;
		this.gui = null;
		this.deltaTimer = System.nanoTime();
		this.setTickRate(tickRate);
		this.app = app;
		this.textAppStatistics = null;
		this.pointLight0 = null;
		this.floorBrick = null;
		this.shadowLightPosition = null;
	}
	
	public void init() {
		
			// Scene
			// WARNING: ORDER IN WHICH SCENE OBJECTS ARE ADDED IS IMPORTANT FOR DRAW CALLS
			// LIGHTS HAVE TO BE ADDED FIRST SO THAT THEY ARE UPDATED BEFORE SCENE RENDERING
		this.objects = new ArrayList<>();
		
		this.activeCamera = new Camera(this, new Projection(75.0f, 0.01f, 100.0f));
		this.objects.add(this.activeCamera);
		
		AmbientLight ambientLight = new AmbientLight(
			this, new Vector3f(1.0f, 1.0f, 1.0f), 0.5f
		);
		this.objects.add(ambientLight);
		DebugUtils.log(this, "Added AmbientLight!");
		
		this.pointLight0 = new PointLight(
			this, new Vector3f(1.0f, 1.0f, 1.0f), 0.5f
		);
		this.pointLight0.setPosition(0.0f, 1.0f, 0.0f);
		this.objects.add(this.pointLight0);
		DebugUtils.log(this, "Added PointLight!");
		
		/*Material brickMaterial = new Material();
		brickMaterial.setTexture(0, TestAssets.TEXTURE_BRICK);
		brickMaterial.setTexture(1, TestAssets.TEXTURE_BRICK_NORMAL);*/
		
		this.floorBrick = new DebugModel(this);
		/*this.floorBrick.addMesh(TestAssets.MESH_BRICK, brickMaterial);
		this.floorBrick.setPosition(0, -0.5f, 0);
		this.floorBrick.setScale(0.1f, 0.01f, 0.1f);
		this.objects.add(this.floorBrick);
		
		Model model = new Model(this);
		model.addMesh(TestAssets.MESH_BRICK, brickMaterial);
		model.setPosition(-0.5f, 0.5f, -0.5f);
		model.setScale(0.01f, 0.01f, 0.01f);
		this.objects.add(model);
		
		AnimationData animationData = new AnimationData(TestAssets.ANIM_RUN);
		Material manMaterial = new Material();
		manMaterial.setTexture(0, TestAssets.TEXTURE_BRICK);
		manMaterial.setTexture(1, TestAssets.TEXTURE_BRICK_NORMAL);
		model = new Model(this);
		model.addMesh(TestAssets.MESH_MAN, manMaterial);
		model.setPosition(0.0f, 0.0f, 0.0f);
		model.setScale(0.01f, 0.01f, 0.01f);
		model.setAnimationData(animationData);
		model.setRotation(0, 0, -1, (float) Math.toRadians(90.0d));
		
		this.objects.add(model);*/
		
		this.shadowLightPosition = new Vector3f(0.0f, 0.5f, 0.5f);
		
		Model model;
		model = new Model(this);
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
		
		model.setPosition(0.0f, -1.0f, 0.0f);
		model.setScale(0.001f, 0.001f, 0.001f);
		/*for( int i = 4; i < TestAssets.MESH_OUTSIDE_PLACE.length; i++ ) {
			model.addMesh(TestAssets.MESH_OUTSIDE_PLACE[i], brickMaterial);
		}*/
		this.objects.add(model);
		
		
			// GUI
		this.createGUI();
		
			// Camera controls here
		Input input = this.app.getWindow().getInput();
		Controller cameraController = new Controller(input, this.activeCamera)
		.addBinding(ActionSet.MOVE_FORWARD, input.new KeyHeld(GLFW.GLFW_KEY_W))
		.addBinding(ActionSet.MOVE_LEFT, input.new KeyHeld(GLFW.GLFW_KEY_A))
		.addBinding(ActionSet.MOVE_BACKWARDS, input.new KeyHeld(GLFW.GLFW_KEY_S))
		.addBinding(ActionSet.MOVE_RIGHT, input.new KeyHeld(GLFW.GLFW_KEY_D))
		.addBinding(ActionSet.LOOK_AROUND, input.new MouseMove());
		this.activeCamera.setController(cameraController);
		
			// Point light controls here
		Controller pointLightController = new Controller(input, this.pointLight0)
		.addBinding(ActionSet.MOVE_FORWARD, input.new KeyHeld(GLFW.GLFW_KEY_UP))
		.addBinding(ActionSet.MOVE_BACKWARDS, input.new KeyHeld(GLFW.GLFW_KEY_DOWN))
		.addBinding(ActionSet.MOVE_LEFT, input.new KeyHeld(GLFW.GLFW_KEY_LEFT))
		.addBinding(ActionSet.MOVE_RIGHT, input.new KeyHeld(GLFW.GLFW_KEY_RIGHT))
		.addBinding(ActionSet.LIGHT_INTENSIFY, input.new KeyHeld(GLFW.GLFW_KEY_KP_ADD))
		.addBinding(ActionSet.LIGHT_DIM, input.new KeyHeld(GLFW.GLFW_KEY_KP_SUBTRACT))
		.addBinding(ActionSet.LIGHT_INCREASE_RED, input.new KeyHeld(GLFW.GLFW_KEY_1))
		.addBinding(ActionSet.LIGHT_DECREASE_RED, input.new KeyHeld(GLFW.GLFW_KEY_2))
		.addBinding(ActionSet.LIGHT_INCREASE_GREEN, input.new KeyHeld(GLFW.GLFW_KEY_3))
		.addBinding(ActionSet.LIGHT_DECREASE_GREEN, input.new KeyHeld(GLFW.GLFW_KEY_4))
		.addBinding(ActionSet.LIGHT_INCREASE_BLUE, input.new KeyHeld(GLFW.GLFW_KEY_5))
		.addBinding(ActionSet.LIGHT_DECREASE_BLUE, input.new KeyHeld(GLFW.GLFW_KEY_6));
		this.pointLight0.setController(pointLightController);
		
			// Floor brick controls
		Controller floorBrickController = new Controller(input, this.floorBrick)
		.addBinding(ActionSet.NORMAL_MAP_TOGGLE, input.new KeyPressed(GLFW.GLFW_KEY_N));
		this.floorBrick.setController(floorBrickController);
	}

	public void update() {
		if( System.nanoTime() - this.deltaTimer < this.tickDelta ) {
			return;
		}
		
		float deltaTime = (System.nanoTime() - this.deltaTimer) / 1000000000.0f;
		this.deltaTimer = System.nanoTime();
		Window appWindow = this.app.getWindow();
		
		appWindow.pollInput();
		for( ASceneObject object : this.objects ) {
			object.tick(deltaTime);
		}
		
		long memoryUsage = Runtime.getRuntime().totalMemory();
		
		if( this.gui != null ) {
			this.textAppStatistics.setContent(
				"FPS: " + appWindow.getFPS() + " / " + appWindow.getMaxFPS() + "\n" +
				"TICK: " + this.tickRate + " (d: " + deltaTime + ")\n" +
				"HEAP: " + this.convertToLargestByte(memoryUsage) + " (" + memoryUsage + " bytes)\n" +
				"pointLight0: \n" +
				"    pos: (" + 
					this.pointLight0.getPosition().x + ", " + 
					this.pointLight0.getPosition().y + ", " + 
					this.pointLight0.getPosition().z + 
				")\n" +
				"    rgb: (" +
					this.pointLight0.getColor().x + ", " +
					this.pointLight0.getColor().y + ", " +
					this.pointLight0.getColor().z +
				")\n" +
				"    intensity: " + this.pointLight0.getIntensity() + "\n" +
				"    normal map: " + (this.floorBrick.isNormalMapActive() ? "ON" : "OFF") + "\n\n" +
				"Controls: \n" + 
				"    WASD to move\n" +
				"    MOUSE to look around\n" +
				"    ARROW KEYS to move point light\n" +
				"    +/- to change point light intensity\n" +
				"    1/2 to change point light red value\n" +
				"    3/4 to change point light green value\n" +
				"    5/6 to change point light blue value\n" + 
				"    N to toggle normal map" +
				"    H to toggle HUD"
			);
		}
		
		if( this.app.getWindow().getInputSnapshot().isKeyPressed(GLFW.GLFW_KEY_H) ) {
			if( this.gui == null ) {
				this.createGUI();
			} else {
				this.gui = null;
			}
		}
		
		if( this.app.getWindow().getInputSnapshot().isKeyHeld(GLFW.GLFW_KEY_KP_8) ) {
			this.shadowLightPosition.add(0,1*deltaTime,0);
		} else if( this.app.getWindow().getInputSnapshot().isKeyHeld(GLFW.GLFW_KEY_KP_2) ) {
			this.shadowLightPosition.sub(0,1*deltaTime,0);
		}
	}
	
	private String convertToLargestByte(long n) {
		String[] units = new String[] {
			" bytes",
			"KB",
			"MB",
			"GB"
		};
		
		if( n <= 0 ) {
			return "0 bytes";
		}
		
		int index = 0;
		while( n / 1000 > 0 && index < units.length - 1 ) {
			n /= 1000;
			index++;
		}
		
		return n + units[index];
	}
	
	private void createGUI() {
		this.textAppStatistics = new Text(this.gui, "");
		this.gui = new GUI(this);
		this.gui.init();
		this.gui.addElement(this.textAppStatistics);
	}
	
	public void setTickRate(int tickRate) {
		this.tickRate = tickRate;
		this.tickDelta = 1000000000 / this.tickRate;
	}
	
	public List<ASceneObject> getObjects() {
		return this.objects;
	}
	
	public Camera getActiveCamera() {
		return this.activeCamera;
	}
	
	public Application getApp() {
		return this.app;
	}
	
	public GUI getGUI() {
		return this.gui;
	}
	
	public Vector3f getShadowLightPosition() {
		return this.shadowLightPosition;
	}
}

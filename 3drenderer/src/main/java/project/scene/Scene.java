package project.scene;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import project.Application;
import project.Window;
import project.asset.AnimationData;
import project.component.Material;
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
	}
	
	public void init() {
		
			// Scene
			// WARNING: ORDER IN WHICH SCENE OBJECTS ARE ADDED IS IMPORTANT FOR DRAW CALLS
			// LIGHTS HAVE TO BE ADDED FIRST SO THAT THEY ARE UPDATED BEFORE SCENE RENDERING
		this.objects = new ArrayList<>();
		
		this.activeCamera = new Camera(this, new Projection(60.0f, 0.01f, 1000.0f));
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
		
		Material brickMaterial = new Material();
		brickMaterial.setTexture(0, TestAssets.TEXTURE_BRICK);
		brickMaterial.setTexture(1, TestAssets.TEXTURE_BRICK_NORMAL);
		
		this.floorBrick = new DebugModel(this);
		this.floorBrick.addMesh(TestAssets.MESH_BRICK, brickMaterial);
		this.floorBrick.setPosition(0, -0.5f, 0);
		this.floorBrick.setScale(0.1f, 0.01f, 0.1f);
		//this.objects.add(this.floorBrick);
		
		Model model = new Model(this);
		model.addMesh(TestAssets.MESH_BRICK, brickMaterial);
		model.setPosition(-0.5f, 0.5f, -0.5f);
		model.setScale(0.01f, 0.01f, 0.01f);
		//this.objects.add(model);
		
		AnimationData animationData = new AnimationData(TestAssets.ANIM_RUN);
		Material manMaterial = new Material();
		manMaterial.setTexture(0, TestAssets.TEXTURE_BRICK);
		manMaterial.setTexture(1, TestAssets.TEXTURE_BRICK_NORMAL);
		model = new Model(this);
		model.addMesh(TestAssets.MESH_MAN, manMaterial);
		model.setPosition(0.0f, 0.0f, 0.0f);
		model.setScale(0.01f, 0.01f, 0.01f);
		model.setAnimationData(animationData);
		
		this.objects.add(model);
		
		/*
		model = new Model(this);
		Material outsideMaterial = new Material();
		outsideMaterial.setTexture(0, TestAssets.TEXTURE_OUTSIDE_PAVEMENT1);
		model.addMesh(TestAssets.MESH_OUTSIDE_PLACE[0], outsideMaterial);
		outsideMaterial = new Material();
		outsideMaterial.setTexture(0, TestAssets.TEXTURE_OUTSIDE_CONCRETE_BLOCK1);
		model.addMesh(TestAssets.MESH_OUTSIDE_PLACE[1], outsideMaterial);
		outsideMaterial = new Material();
		outsideMaterial.setTexture(0, TestAssets.TEXTURE_OUTSIDE_METAL_DIRTYRUST);
		model.addMesh(TestAssets.MESH_OUTSIDE_PLACE[2], outsideMaterial);
		model.addMesh(TestAssets.MESH_OUTSIDE_PLACE[3], brickMaterial);
		model.setPosition(0.0f, -1.0f, 0.0f);
		model.setScale(0.001f, 0.001f, 0.001f);
		for( int i = 4; i < TestAssets.MESH_OUTSIDE_PLACE.length; i++ ) {
			model.addMesh(TestAssets.MESH_OUTSIDE_PLACE[i], brickMaterial);
		}
		this.objects.add(model);
		*/
		
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
}

package project.scene;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import project.Application;
import project.Globals;
import project.Window;
import project.controls.Controller;
import project.gui.GUI;
import project.gui.Image;
import project.gui.Text;
import project.input.Input;
import project.testing.ActionSet;
import project.testing.TestAssets;
import project.testing.TestDummy;
import project.testing.TestPlayer;
import project.testing.TestPointLight;
import project.utils.DebugUtils;

public class Scene {
	private List<ASceneObject> objects;
	private Camera activeCamera;
	private GUI gui;
	private long deltaTimer;
	private long tickDelta;
	private int tickRate;
	private Application app;
	
	private Text DEBUGtextAppStatistics;
	private TestPointLight DEBUGtestPointLight0;
	private Vector3f DEBUGshadowLightPosition;
	private boolean DEBUGareNormalsActive;
	
	public Scene(Application app, int tickRate) {
		this.objects = null;
		this.activeCamera = null;
		this.gui = null;
		this.deltaTimer = System.nanoTime();
		this.setTickRate(tickRate);
		this.app = app;
		
		this.DEBUGtextAppStatistics = null;
		this.DEBUGtestPointLight0 = null;
		this.DEBUGshadowLightPosition = null;
		this.DEBUGareNormalsActive = true;
	}
	
	
	public void init() {
			// Scene
			// WARNING: ORDER IN WHICH SCENE OBJECTS ARE ADDED IS IMPORTANT FOR DRAW CALLS
			// LIGHTS HAVE TO BE ADDED FIRST SO THAT THEY ARE UPDATED BEFORE SCENE RENDERING
		Input input = this.app.getWindow().getInput();
		this.objects = new ArrayList<>();
		
			// Ambient light
		AmbientLight ambientLight = new AmbientLight(
			this, new Vector3f(1.0f, 1.0f, 1.0f), 0.5f
		);
		this.objects.add(ambientLight);
		DebugUtils.log(this, "Added AmbientLight!");
		
			// Point light
		this.DEBUGtestPointLight0 = new TestPointLight(this);
		this.DEBUGtestPointLight0.setPosition(0.0f, 100.0f, 0.0f);
		this.objects.add(this.DEBUGtestPointLight0);
		Controller pointLightController = new Controller(input, this.DEBUGtestPointLight0)
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
		this.DEBUGtestPointLight0.setController(pointLightController);
		DebugUtils.log(this, "Added TestPointLight!");
		
			// Shadow position
		this.DEBUGshadowLightPosition = new Vector3f(0.05f, 0.5f, 0.5f);
		
			// Outside scene
		TestDummy outsideScene = new TestDummy(this, TestAssets.createTestSceneOutside(this));
		this.objects.add(outsideScene);
		DebugUtils.log(this, "Outside place TestDummy added!");
		
			// Soldier
		TestDummy soldier = new TestDummy(this, TestAssets.createTestSoldier(this));
		soldier.setPosition(1, -10, -100);
		//soldier.getRotationComponent().setXAngle((float) Math.toRadians(-85.0d));
		this.objects.add(soldier);
		DebugUtils.log(this, "Soldier TestDummy added!");
		
			// GUI
		this.createGUI();
		DebugUtils.log(this, "GUI created!");

			// Camera
		TestPlayer player = new TestPlayer(this);
		this.objects.add(player);
		this.activeCamera = player.getCamera();
		Controller playerController = new Controller(input, player)
		.addBinding(ActionSet.MOVE_FORWARD, input.new KeyHeld(GLFW.GLFW_KEY_W))
		.addBinding(ActionSet.MOVE_LEFT, input.new KeyHeld(GLFW.GLFW_KEY_A))
		.addBinding(ActionSet.MOVE_BACKWARDS, input.new KeyHeld(GLFW.GLFW_KEY_S))
		.addBinding(ActionSet.MOVE_RIGHT, input.new KeyHeld(GLFW.GLFW_KEY_D))
		.addBinding(ActionSet.LOOK_AROUND, input.new MouseMove());
		player.setController(playerController);
		DebugUtils.log(this, "Added TestPlayer!");
	}

	public void update() {
		if( System.nanoTime() - this.deltaTimer < this.tickDelta ) {
			return;
		}
		
		float deltaTime = (System.nanoTime() - this.deltaTimer) / 1000000000.0f;
		this.deltaTimer = System.nanoTime();
		Window appWindow = this.app.getWindow();
		
		Globals.ASSET_MANAGER.processTaskResults(System.nanoTime());
		
		appWindow.pollInput();
		for( ASceneObject object : this.objects ) {
			object.tick(deltaTime);
		}
		
		long memoryUsage = Runtime.getRuntime().totalMemory();
		
		if( this.gui != null ) {
			this.DEBUGtextAppStatistics.setContent(
				"FPS: " + appWindow.getFPS() + " / " + appWindow.getMaxFPS() + "\n" +
				"TICK: " + this.tickRate + " (d: " + deltaTime + ")\n" +
				"HEAP: " + this.convertToLargestByte(memoryUsage) + " (" + memoryUsage + " bytes)\n" +
				"camera\n" + 
				"   pos: (" + 
					this.activeCamera.getPosition().x + ", " +
					this.activeCamera.getPosition().y + ", " +
					this.activeCamera.getPosition().z +
				")\n" +
				"pointLight0: \n" +
				"    pos: (" + 
					this.DEBUGtestPointLight0.getPosition().x + ", " + 
					this.DEBUGtestPointLight0.getPosition().y + ", " + 
					this.DEBUGtestPointLight0.getPosition().z + 
				")\n" +
				"    rgb: (" +
					this.DEBUGtestPointLight0.getPointLight().getColor().x + ", " +
					this.DEBUGtestPointLight0.getPointLight().getColor().y + ", " +
					this.DEBUGtestPointLight0.getPointLight().getColor().z +
				")\n" +
				"    intensity: " + this.DEBUGtestPointLight0.getPointLight().getIntensity() + "\n" +
				"    normal map: " + (this.DEBUGareNormalsActive ? "ON" : "OFF") + "\n\n" +
				"Controls: \n" + 
				"    WASD to move\n" +
				"    MOUSE to look around\n" +
				"    ARROW KEYS to move point light\n" +
				"    +/- to change point light intensity\n" +
				"    1/2 to change point light red value\n" +
				"    3/4 to change point light green value\n" +
				"    5/6 to change point light blue value\n" + 
				"    N to toggle normal map\n" +
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
			this.DEBUGshadowLightPosition.add(0,1*deltaTime,0);
		} else if( this.app.getWindow().getInputSnapshot().isKeyHeld(GLFW.GLFW_KEY_KP_2) ) {
			this.DEBUGshadowLightPosition.sub(0,1*deltaTime,0);
		}
		
		if( this.app.getWindow().getInputSnapshot().isKeyPressed(GLFW.GLFW_KEY_N) ) {
			this.DEBUGareNormalsActive = !this.DEBUGareNormalsActive;
		}
		
		for( ASceneObject object : this.objects ) {
			object.submitState();
		}
		
		Globals.RENDERER.submitGameState();
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
		this.DEBUGtextAppStatistics = new Text(this.gui, "");
		this.gui = new GUI(this);
		this.gui.init();
		this.gui.addElement(this.DEBUGtextAppStatistics);
		
		Image crosshair = new Image(this.gui, TestAssets.TEX_GUI_CROSSHAIR);
		crosshair.setPosition(400, 300, 0);
		crosshair.setAnchor(8, 8);
		this.gui.addElement(crosshair);
	}
	
	public void addObject(ASceneObject sceneObject) {
		this.objects.add(sceneObject);
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
		return this.DEBUGshadowLightPosition;
	}
	
	public boolean DEBUGareNormalsActive() {
		return this.DEBUGareNormalsActive;
	}
}

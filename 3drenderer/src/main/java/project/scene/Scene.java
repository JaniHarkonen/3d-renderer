package project.scene;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import project.Application;
import project.Window;
import project.controls.Controller;
import project.gui.GUI;
import project.gui.Image;
import project.gui.Theme;
import project.gui.props.Properties;
import project.gui.props.Property;
import project.input.Input;
import project.input.InputSnapshot;
import project.input.KeyHeld;
import project.input.MouseMove;
import project.testing.ActionSet;
import project.testing.TestAssets;
import project.testing.TestDebugDataHandles;
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
	
	//private Image DEBUGcrosshair;
	//private Text DEBUGtextAppStatistics;
	private TestPointLight DEBUGtestPointLight0;
	private Vector3f DEBUGshadowLightPosition;
	private boolean DEBUGareNormalsActive;
	private boolean DEBUGisRoughnessActive;
	private boolean DEBUGcascadeShadowEnabled;
	private TestDummy DEBUGsoldier;
	public TestDummy DEBUGserverSoldier;
	
	public Scene(Application app, int tickRate) {
		this.objects = null;
		this.activeCamera = null;
		this.gui = null;
		this.deltaTimer = System.nanoTime();
		this.setTickRate(tickRate);
		this.app = app;
		
		//this.DEBUGcrosshair = null;
		//this.DEBUGtextAppStatistics = null;
		this.DEBUGtestPointLight0 = null;
		this.DEBUGshadowLightPosition = null;
		this.DEBUGareNormalsActive = true;
		this.DEBUGisRoughnessActive = true;
		this.DEBUGcascadeShadowEnabled = false;
		this.DEBUGsoldier = null;
		this.DEBUGserverSoldier = null;
	}
	
	
	public void initialize() {
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
		this.DEBUGtestPointLight0.getTransform().setPosition(0.0f, 200.0f, 0.0f);
		this.objects.add(this.DEBUGtestPointLight0);
		Controller pointLightController = new Controller(input, this.DEBUGtestPointLight0)
		.addBinding(ActionSet.MOVE_FORWARD, new KeyHeld(input, GLFW.GLFW_KEY_UP))
		.addBinding(ActionSet.MOVE_BACKWARDS, new KeyHeld(input, GLFW.GLFW_KEY_DOWN))
		.addBinding(ActionSet.MOVE_LEFT, new KeyHeld(input, GLFW.GLFW_KEY_LEFT))
		.addBinding(ActionSet.MOVE_RIGHT, new KeyHeld(input, GLFW.GLFW_KEY_RIGHT))
		.addBinding(ActionSet.LIGHT_INTENSIFY, new KeyHeld(input, GLFW.GLFW_KEY_KP_ADD))
		.addBinding(ActionSet.LIGHT_DIM, new KeyHeld(input, GLFW.GLFW_KEY_KP_SUBTRACT))
		.addBinding(ActionSet.LIGHT_INCREASE_RED, new KeyHeld(input, GLFW.GLFW_KEY_1))
		.addBinding(ActionSet.LIGHT_DECREASE_RED, new KeyHeld(input, GLFW.GLFW_KEY_2))
		.addBinding(ActionSet.LIGHT_INCREASE_GREEN, new KeyHeld(input, GLFW.GLFW_KEY_3))
		.addBinding(ActionSet.LIGHT_DECREASE_GREEN, new KeyHeld(input, GLFW.GLFW_KEY_4))
		.addBinding(ActionSet.LIGHT_INCREASE_BLUE, new KeyHeld(input, GLFW.GLFW_KEY_5))
		.addBinding(ActionSet.LIGHT_DECREASE_BLUE, new KeyHeld(input, GLFW.GLFW_KEY_6));
		this.DEBUGtestPointLight0.setController(pointLightController);
		DebugUtils.log(this, "Added TestPointLight!");
		
			// Shadow position
		this.DEBUGshadowLightPosition = new Vector3f(0.05f, 0.5f, 0.5f);
		
			// Outside scene
		TestDummy outsideScene = new TestDummy(this, TestAssets.createTestSceneOutside(this));
		this.objects.add(outsideScene);
		DebugUtils.log(this, "Outside place TestDummy added!");
		
			// Soldier
		this.DEBUGsoldier = new TestDummy(this, TestAssets.createTestSoldier(this));
		this.DEBUGsoldier.getTransform().setPosition(1, -10, -100);
		this.DEBUGsoldier.getTransform().getRotator().setXAngle((float) Math.toRadians(-85.0d));
		//soldier.getRotationComponent().setXAngle((float) Math.toRadians(-85.0d));
		this.objects.add(this.DEBUGsoldier);
		DebugUtils.log(this, "Soldier TestDummy added!");
		
			// Platoon
		/*for( int i = 0; i < 100; i++ ) {
			
			TestDummy randomSoldier = new TestDummy(this, TestAssets.createTestSoldier(this));
			randomSoldier.getTransform().setPosition(((float) Math.random() * 300), -10, -100 - ((float) Math.random() * 300));
			randomSoldier.getTransform().getRotator().setXAngle((float) Math.toRadians(-85.0d));
			//soldier.getRotationComponent().setXAngle((float) Math.toRadians(-85.0d));
			this.objects.add(randomSoldier);
		}*/
		
			// GUI
		this.createGUI();
		DebugUtils.log(this, "GUI created!");

			// Camera
		TestPlayer player = new TestPlayer(this);
		this.objects.add(player);
		this.activeCamera = player.getCamera();
		Controller playerController = new Controller(input, player)
		.addBinding(ActionSet.MOVE_FORWARD, new KeyHeld(input, GLFW.GLFW_KEY_W))
		.addBinding(ActionSet.MOVE_LEFT, new KeyHeld(input, GLFW.GLFW_KEY_A))
		.addBinding(ActionSet.MOVE_BACKWARDS, new KeyHeld(input, GLFW.GLFW_KEY_S))
		.addBinding(ActionSet.MOVE_RIGHT, new KeyHeld(input, GLFW.GLFW_KEY_D))
		.addBinding(ActionSet.LOOK_AROUND, new MouseMove(input));
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
		
		Application.getApp().getAssetManager().processTaskResults(System.nanoTime());
		appWindow.pollInput();
		
		//if( this.DEBUGcrosshair != null ) {
			//this.DEBUGcrosshair.getTransform().setPosition(
				//this.app.getWindow().getWidth() / 2, this.app.getWindow().getHeight() / 2, 0
			//);
		//}
		
		for( ASceneObject object : this.objects ) {
			object.tick(deltaTime);
		}
		
		long memoryUsage = Runtime.getRuntime().totalMemory();
		
		Vector3f cameraPosition = this.activeCamera.getTransform().getPosition();
		Vector3f pl0Position = this.DEBUGtestPointLight0.getTransform().getPosition();
		Vector3f pl0Color = this.DEBUGtestPointLight0.getPointLight().getColor();
		
		if( this.gui != null ) {
			/*this.DEBUGtextAppStatistics.setContent(
				"FPS: " + appWindow.getFPS() + " / " + appWindow.getMaxFPS() + "\n" +
				"TICK: " + this.tickRate + " (d: " + deltaTime + ")\n" +
				"HEAP: " + this.convertToLargestByte(memoryUsage) + " (" + memoryUsage + " bytes)\n" +
				"camera\n" + 
				"   pos: (" + 
					cameraPosition.x + ", " +
					cameraPosition.y + ", " +
					cameraPosition.z +
				")\n" +
				"pointLight0: \n" +
				"    pos: (" + 
					pl0Position.x + ", " + 
					pl0Position.y + ", " + 
					pl0Position.z + 
				")\n" +
				"    rgb: (" +
					pl0Color.x + ", " +
					pl0Color.y + ", " +
					pl0Color.z +
				")\n" +
				"soldier: \n" +
				"    model.animator.currentFrameIndex: " + this.DEBUGsoldier.getModel().getAnimator().getCurrentFrameIndex() + "\n" +
				"    intensity: " + this.DEBUGtestPointLight0.getPointLight().getIntensity() + "\n" +
				"    normal map: " + (this.DEBUGareNormalsActive ? "ON" : "OFF") + " (N to toggle)\n" +
				"    roughness map: " + (DEBUGisRoughnessActive ? "ON" : "OFF") + " (R to toggle)\n" +
				"    shadow map cascades: " + (this.DEBUGcascadeShadowEnabled ? "ON" : "OFF") + " (C to toggle)\n\n" +
				"Controls: \n" + 
				"    WASD to move\n" +
				"    MOUSE to look around\n" +
				"    ARROW KEYS to move point light\n" +
				"    +/- to change point light intensity\n" +
				"    1/2 to change point light red value\n" +
				"    3/4 to change point light green value\n" +
				"    5/6 to change point light blue value\n" +
				"    H to toggle HUD\n"
			);*/
		}
		
		InputSnapshot inputSnapshot = this.app.getWindow().getInputSnapshot();
		
		if( inputSnapshot.isKeyPressed(GLFW.GLFW_KEY_H) ) {
			if( this.gui == null ) {
				this.createGUI();
			} else {
				this.gui = null;
			}
		}
		
		if( inputSnapshot.isKeyHeld(GLFW.GLFW_KEY_KP_8) ) {
			this.DEBUGshadowLightPosition.add(0,1*deltaTime,0);
		} else if( inputSnapshot.isKeyHeld(GLFW.GLFW_KEY_KP_2) ) {
			this.DEBUGshadowLightPosition.sub(0,1*deltaTime,0);
		}
		
			// DEBUG - Toggle normal maps
		if( inputSnapshot.isKeyPressed(GLFW.GLFW_KEY_N) ) {
			this.DEBUGareNormalsActive = !this.DEBUGareNormalsActive;
		}
		
			// DEBUG - Toggle roughness maps
		if( inputSnapshot.isKeyPressed(GLFW.GLFW_KEY_R) ) {
			this.DEBUGisRoughnessActive = !this.DEBUGisRoughnessActive;
		}
		
			// DEBUG - Toggle cascade shadow maps
		if( inputSnapshot.isKeyPressed(GLFW.GLFW_KEY_C) ) {
			this.DEBUGcascadeShadowEnabled = !this.DEBUGcascadeShadowEnabled;
		}
		
			// DEBUG - Send "pong" to server
		//if( inputSnapshot.isKeyPressed(GLFW.GLFW_KEY_T) ) {
			//this.app.getNetworker().queueMessage(new MMessage("TEST pong"));
		//}
		
		long time = System.nanoTime();
		for( ASceneObject object : this.objects ) {
			object.submitToRenderer();
		}
		
		if( this.gui != null ) {
			this.gui.tick(deltaTime);
			this.gui.submitToRenderer();
			//this.gui.getBody().submitToRenderer();
		}
		
		Application.getApp().getRenderer().getBackGameState()
		.setDebugData(TestDebugDataHandles.NORMALS_ACTIVE, this.DEBUGareNormalsActive)
		.setDebugData(TestDebugDataHandles.CASCADE_SHADOW_LIGHT, this.DEBUGshadowLightPosition)
		.setDebugData(TestDebugDataHandles.ROUGHNESS_ACTIVE, this.DEBUGisRoughnessActive)
		.setDebugData(TestDebugDataHandles.CASCADE_SHADOW_ENABLED, this.DEBUGcascadeShadowEnabled);
		Application.getApp().getRenderer().submitGameState();
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
		//this.DEBUGtextAppStatistics = new Text(this.gui, "test-text", "");
		this.gui = new GUI();
		this.gui.initialize();
		
			// Theme
		Theme t = new Theme();
		/*t.setProperty(new Property("epic-prop", "e(rgba(0,0,255,255))", Property.EXPRESSION));
			Theme subt = new Theme();
			//subt.setProperty(new Property("sub-prop", "fdffcd", Property.COLOR_HEX));
			subt.setProperty(new Property("sub-prop", "e(rgba(0,0,255,255))", Property.EXPRESSION));
			//subt.setProperty(new Property("sub-prop", "e(-1+59*612+4/45*41-1-6)", Property.EXPRESSION));
		t.setSection("sub-section", subt);*/
		this.gui.addTheme("epic-theme", t);
		this.gui.setActiveTheme("epic-theme");
		
		Properties props;
		Properties.Style style;
		
		/*Div div = new Div(this.gui, "test-div");
			props = div.getProperties();
			props.getProperty(Properties.WIDTH).set(1, Property.WPC);
			props.getProperty(Properties.HEIGHT).set(0.25f, Property.HPC);
			props.getProperty(Properties.COLS).set(10, Property.NUMBER);
			props.getProperty(Properties.ROWS).set(10, Property.NUMBER);
			props.getProperty(Properties.PRIMARY_COLOR).set(new Vector4f(0, 0, 0, 1/3f), Property.COLOR);
			props.getProperty(Properties.MIN_WIDTH).set(500, Property.PX);
			props.getProperty(Properties.MIN_HEIGHT).set(500, Property.PX);
			
			Div cdiv = new Div(this.gui, "test-div-child");
				props = cdiv.getProperties();
				props.getProperty(Properties.LEFT).set(1, Property.C);
				props.getProperty(Properties.WIDTH).set(8, Property.C);
				props.getProperty(Properties.TOP).set(1, Property.R);
				props.getProperty(Properties.HEIGHT).set(8, Property.R);
				style = props.addResponsiveStyle(new RQuery(400, Float.MAX_VALUE));
				style.addProperty(new Property(Properties.WIDTH, 200, Property.PX));
				
				
			this.gui.addChildTo(div, cdiv);*/
			
			/*Text text = new Text(this.gui, "test-text", "Hello world!");
				props = text.getProperties();
				props.getProperty(Properties.LEFT).set(1, Property.C);
				props.getProperty(Properties.TOP).set(1, Property.R);
				props.getProperty(Properties.WIDTH).set(8, Property.C);
				props.getProperty(Properties.HEIGHT).set(1, Property.R);
				props.getProperty(Properties.PRIMARY_COLOR).set(new Vector4f(1, 1, 1, 1), Property.COLOR);
			this.gui.addChildTo(div, text);*/
			
		//this.gui.addChildTo(this.gui.getBody(), div);
		
		Image image = new Image(this.gui, "img-crosshair", TestAssets.TEX_GUI_CROSSHAIR);
			props = image.getProperties();
			props.getProperty(Properties.LEFT).set(0.5f, Property.WPERCENT);
			props.getProperty(Properties.TOP).set(0.5f, Property.HPERCENT);
			props.getProperty(Properties.WIDTH).set(16, Property.PX);
			props.getProperty(Properties.HEIGHT).set(16, Property.PX);
			props.getProperty(Properties.ANCHOR_X).set(0.5f, Property.WPERCENT);
			props.getProperty(Properties.ANCHOR_Y).set(0.5f, Property.HPERCENT);
			//props.getProperty(Properties.PRIMARY_COLOR).set("sub-section.sub-prop", Property.THEME);
			//props.getProperty(Properties.PRIMARY_COLOR).set("9fd3c7", Property.COLOR_HEX);
		this.gui.addChildTo(this.gui.getBody(), image);
		
		/*Div div = new Div(this.gui, "test-div");
			props = div.getProperties();
			props.getProperty(Properties.LEFT).set(0, Property.PC);
			props.getProperty(Properties.WIDTH).set(0.5f, Property.C);
			props.getProperty(Properties.HEIGHT).set(32, Property.PX);
			props.getProperty(Properties.PRIMARY_COLOR).set(new Vector4f(1, 1, 1, 1), Property.COLOR);
		this.gui.addChildTo(div, this.gui.getBody());
		
		Div div2 = new Div(this.gui, "test-div-2");
			props = div2.getProperties();
			props.getProperty(Properties.LEFT).set(0.5f, Property.PC);
			props.getProperty(Properties.WIDTH).set(0.5f, Property.PC);
			props.getProperty(Properties.HEIGHT).set(32, Property.PX);
		this.gui.addChildTo(div2, this.gui.getBody());*/
			
		//this.gui.addElement(this.DEBUGtextAppStatistics);
		
		//this.DEBUGcrosshair = new Image(this.gui, "test-image", TestAssets.TEX_GUI_CROSSHAIR);
		//this.DEBUGcrosshair.getTransform().setPosition(400, 300, 0);
		//this.DEBUGcrosshair.setAnchor(8, 8);
		//this.gui.addElement(this.DEBUGcrosshair);
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
	
	/*public Camera getActiveCamera() {
		return this.activeCamera;
	}*/
	
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

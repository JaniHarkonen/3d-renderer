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
	
	public Scene(Application app, int tickRate) {
		this.objects = null;
		this.activeCamera = null;
		this.gui = null;
		this.deltaTimer = System.nanoTime();
		this.setTickRate(tickRate);
		this.app = app;
		this.textAppStatistics = null;
	}
	
	public void init() {
		
			// Scene
			// WARNING: ORDER IN WHICH SCENE OBJECTS ARE ADDED IS IMPORTANT FOR DRAW CALLS
			// LIGHTS HAVE TO BE ADDED FIRST SO THAT THEY ARE UPDATED BEFORE SCENE RENDERING
		this.objects = new ArrayList<>();
		
		this.activeCamera = new Camera(this, new Projection(60.0f, 0.01f, 1000.0f));
		this.objects.add(this.activeCamera);
		
		AmbientLight ambientLight = new AmbientLight(
			this, new Vector3f(1.0f, 1.0f, 1.0f), 0.1f
		);
		this.objects.add(ambientLight);
		DebugUtils.log(this, "Added AmbientLight!");
		
		PointLight pointLight = new PointLight(
			this, new Vector3f(1.0f, 0.0f, 0.0f), 1.0f
		);
		pointLight.setPosition(0.0f, 1.0f, 0.0f);
		this.objects.add(pointLight);
		DebugUtils.log(this, "Added PointLight!");
		
		Model model = new Model(this);
		model.setPosition(0, -0.5f, 0);
		model.setScale(0.1f, 0.01f, 0.1f);
		this.objects.add(model);
		
			// GUI
		this.textAppStatistics = new Text(this.gui, "");
		this.gui = new GUI(this);
		this.gui.init();
		this.gui.addElement(this.textAppStatistics);
		
			// Camera controls here
		Input input = this.app.getWindow().getInput();
		Controller cameraController = new Controller(input, this.activeCamera)
		.addBinding(ActionSet.MOVE_FORWARD, input.new KeyHeld(GLFW.GLFW_KEY_W))
		.addBinding(ActionSet.MOVE_LEFT, input.new KeyHeld(GLFW.GLFW_KEY_A))
		.addBinding(ActionSet.MOVE_BACKWARDS, input.new KeyHeld(GLFW.GLFW_KEY_S))
		.addBinding(ActionSet.MOVE_RIGHT, input.new KeyHeld(GLFW.GLFW_KEY_D))
		.addBinding(ActionSet.LOOK_AROUND, input.new MouseMove());
		
		this.activeCamera.setController(cameraController);
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
		this.textAppStatistics.setContent(
			"FPS: " + appWindow.getFPS() + " / " + appWindow.getMaxFPS() + "\n" +
			"TICK: " + this.tickRate + " (d: " + deltaTime + ")" +
			"\nHEAP: " + this.convertToLargestByte(memoryUsage) + " (" + memoryUsage + " bytes)"
		);
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

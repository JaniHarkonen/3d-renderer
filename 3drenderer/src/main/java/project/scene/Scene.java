package project.scene;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import project.Application;
import project.controls.Controller;
import project.geometry.Projection;
import project.input.Input;
import project.testing.ActionSet;

public class Scene {
	private List<ASceneObject> objects;
	private Camera activeCamera;
	private long deltaTimer;
	private long tickDelta;
	private int tickRate;
	private Application app;
	
	public Scene(Application app, int tickRate) {
		this.objects = null;
		this.activeCamera = null;
		this.deltaTimer = System.nanoTime();
		this.setTickRate(tickRate);
		this.app = app;
	}
	
	public void init() {
		
		this.activeCamera = new Camera(this, new Projection(60.0f, 0.01f, 1000.0f));
		this.objects = new ArrayList<>();
		this.objects.add(new Model(this));
		this.objects.add(this.activeCamera);
		
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
		
		this.app.getWindow().pollInput();
		for( ASceneObject object : this.objects ) {
			object.tick(deltaTime);
		}
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
}

package project.scene;

import java.util.ArrayList;
import java.util.List;

import project.geometry.Projection;

public class Scene {
	private List<SceneObject> objects;
	private Camera activeCamera;
	
	public Scene() {
		this.objects = null;
		this.activeCamera = null;
	}
	
	public void init() {
		this.activeCamera = new Camera(new Projection(60.0f, 0.01f, 1000.0f));
		this.objects = new ArrayList<>();
		this.objects.add(new Model());
		this.objects.add(this.activeCamera);
	}

	public void update() {
		
	}
	
	public List<SceneObject> getObjects() {
		return this.objects;
	}
	
	public Camera getActiveCamera() {
		return this.activeCamera;
	}
}

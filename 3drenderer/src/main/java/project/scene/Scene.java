package project.scene;

import java.util.ArrayList;
import java.util.List;

import project.geometry.Projection;

public class Scene {
	private List<SceneObject> objects;
	private Camera activeCamera;
	private long deltaTimer;
	private long tickDelta;
	private int tickRate;
	
	public Scene(int tickRate) {
		this.objects = null;
		this.activeCamera = null;
		this.deltaTimer = System.nanoTime();
		this.setTickRate(tickRate);
	}
	
	public void init() {
		this.activeCamera = new Camera(new Projection(60.0f, 0.01f, 1000.0f));
		this.objects = new ArrayList<>();
		this.objects.add(new Model());
		this.objects.add(this.activeCamera);
	}

	public void update() {
		/*if( System.nanoTime() - this.deltaTimer < this.tickDelta ) {
			return;
		}*/
		
		this.deltaTimer = System.nanoTime();
		
		float deltaTime = (System.nanoTime() - this.deltaTimer) / 1000000000.0f;
		for( SceneObject object : this.objects ) {
			object.tick(deltaTime);
		}
	}
	
	public void setTickRate(int tickRate) {
		this.tickRate = tickRate;
		this.tickDelta = 1000000000 / this.tickRate;
	}
	
	public List<SceneObject> getObjects() {
		return this.objects;
	}
	
	public Camera getActiveCamera() {
		return this.activeCamera;
	}
}

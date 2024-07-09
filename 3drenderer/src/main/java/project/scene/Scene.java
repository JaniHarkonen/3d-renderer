package project.scene;

import java.util.ArrayList;
import java.util.List;

import project.geometry.Projection;
import project.opengl.Texture;
import project.opengl.VAO;
import project.utils.FileUtils;

public class Scene {
	private List<VAO> objects;
	private Texture texture;
	private Camera camera;
	
	public Scene() {
		this.objects = null;
		this.texture = null;
		this.camera = null;
	}
	
	public void init() {
		this.objects = new ArrayList<>();
		this.texture = new Texture(FileUtils.getResourcePath("creep.png"));
		this.camera = new Camera(new Projection(60.0f, 0.01f, 1000.0f));
		
		VAO vao = new VAO(0, 0, -1);
		this.objects.add(vao);
	}

	public void update() {
		
	}
	
	public List<VAO> getObjects() {
		return this.objects;
	}
	
	public Texture getTexture() {
		return this.texture;
	}
	
	public Camera getCamera() {
		return this.camera;
	}
}

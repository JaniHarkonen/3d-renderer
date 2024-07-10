package project.scene;

import project.asset.Mesh;
import project.opengl.Texture;
import project.utils.FileUtils;

public class Model extends SceneObject {

	private Mesh mesh;
	private Texture texture;
	
	public Model() {
		super();
		this.mesh = new Mesh();
		this.texture = new Texture(FileUtils.getResourcePath("creep.png"));
	}
	
	public Mesh getMesh() {
		return this.mesh;
	}
	
	public Texture getTexture() {
		return this.texture;
	}
}

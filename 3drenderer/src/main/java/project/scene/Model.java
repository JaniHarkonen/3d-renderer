package project.scene;

import project.asset.Mesh;
import project.asset.SceneAssetLoadTask;
import project.opengl.Texture;
import project.utils.FileUtils;

public class Model extends ASceneObject {

	private Mesh mesh;
	private Texture texture;
	
	public Model(Scene scene) {
		super(scene);
		this.mesh = new Mesh();
		this.texture = new Texture(FileUtils.getResourcePath("creep.png"));
		
		SceneAssetLoadTask task = new SceneAssetLoadTask(FileUtils.getResourcePath("box.fbx"));
		task.expectMesh(this.mesh);
		task.load();
		
		this.setPosition((float) Math.random() * 5, (float) Math.random() * 5, (float) Math.random() * 5);
	}
	
	
	@Override
	public void tick(float deltaTime) {
		//this.position.add(0, 0, -1.0f * deltaTime);
		this.updateTransformMatrix();
	}
	
	public Mesh getMesh() {
		return this.mesh;
	}
	
	public Texture getTexture() {
		return this.texture;
	}
}

package project.scene;

import project.asset.Mesh;
import project.asset.SceneAssetLoadTask;
import project.opengl.Texture;
import project.utils.FileUtils;

public class Model extends ASceneObject {

	private Mesh mesh;
	private Texture texture;
	private float DEBUGangle;
	
	public Model(Scene scene) {
		super(scene);
		this.mesh = new Mesh();
		this.texture = new Texture(FileUtils.getResourcePath("textures/creep.png"));
		
		SceneAssetLoadTask task = new SceneAssetLoadTask(FileUtils.getResourcePath("models/Brick.fbx"));
		task.expectMesh(this.mesh);
		task.load();
		
		/*this.setPosition(
			(float) Math.random() * 5, 
			(float) Math.random() * 5, 
			(float) Math.random() * 5
		);*/
		
		this.DEBUGangle = 0.0f;
	}
	
	
	@Override
	public void tick(float deltaTime) {
		//this.position.add(0, 0, -1.0f * deltaTime);
		//this.setRotation(0, 1, 0, this.DEBUGangle);
		//this.DEBUGangle += deltaTime;
		//this.updateTransformMatrix();
	}
	
	public Mesh getMesh() {
		return this.mesh;
	}
	
	public Texture getTexture() {
		return this.texture;
	}
}

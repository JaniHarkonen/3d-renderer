package project.scene;

import project.asset.Mesh;
import project.component.Material;
import project.testing.TestAssets;

public class Model extends ASceneObject {

	private Mesh mesh;
	private Material material;
	private float DEBUGangle;
	
	public Model(Scene scene) {
		super(scene);
		this.mesh = TestAssets.MESH_BRICK;
		this.material = new Material();
		this.material.setTexture(0, TestAssets.TEXTURE_BRICK);
		this.material.setTexture(1, TestAssets.TEXTURE_BRICK_NORMAL);
		
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
	
	public Material getMaterial() {
		return this.material;
	}
}

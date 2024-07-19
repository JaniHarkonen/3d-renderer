package project.scene;

import java.util.ArrayList;
import java.util.List;

import project.asset.Mesh;
import project.component.Material;

public class Model extends ASceneObject {

	private class MeshEntry {
		private Mesh mesh;
		private Material material;
		
		private MeshEntry(Mesh mesh, Material material) {
			this.mesh = mesh;
			this.material = material;
		}
	}
	
	//private Mesh mesh;
	//private Material material;
	private List<MeshEntry> meshMaterialTable;
	private float DEBUGangle;
	
	public Model(Scene scene) {
		super(scene);
		this.meshMaterialTable = new ArrayList<>();
		/*this.mesh = TestAssets.MESH_BRICK;
		this.material = new Material();
		this.material.setTexture(0, TestAssets.TEXTURE_BRICK);
		this.material.setTexture(1, TestAssets.TEXTURE_BRICK_NORMAL);*/
		
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
	
	public void addMesh(Mesh mesh, Material material) {
		this.meshMaterialTable.add(new MeshEntry(mesh, material));
	}
	
	public int getMeshCount() {
		return this.meshMaterialTable.size();
	}
	
	public Mesh getMesh(int meshIndex) {
		return this.meshMaterialTable.get(meshIndex).mesh;
	}
	
	public Material getMaterial(int meshIndex) {
		return this.meshMaterialTable.get(meshIndex).material;
	}
	
	/*public Mesh getMesh() {
		return this.mesh;
	}
	
	public Material getMaterial() {
		return this.material;
	}*/
}

package project.scene;

import java.util.ArrayList;
import java.util.List;

import project.asset.AnimationData;
import project.asset.Mesh;
import project.component.Material;

public class Model {

	private class MeshEntry {
		private Mesh mesh;
		private Material material;
		
		private MeshEntry(Mesh mesh, Material material) {
			this.mesh = mesh;
			this.material = material;
		}
	}
	
	private List<MeshEntry> meshMaterialTable;
	private AnimationData animationData;
	
	public Model() {
		this.meshMaterialTable = new ArrayList<>();
		this.animationData = null;
	}
	
	
	public void addMesh(Mesh mesh, Material material) {
		this.meshMaterialTable.add(new MeshEntry(mesh, material));
	}
	
	public void setAnimationData(AnimationData animationData) {
		this.animationData = animationData;
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
	
	public AnimationData getAnimationData() {
		return this.animationData;
	}
}

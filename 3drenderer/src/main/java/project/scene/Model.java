package project.scene;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import project.asset.AnimationData;
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
	
	private List<MeshEntry> meshMaterialTable;
	private AnimationData animationData;
	
	public Model(Scene scene) {
		super(scene);
		this.meshMaterialTable = new ArrayList<>();
		this.animationData = null;
	}
	
	private Model(Model model) {
		super(null);
		this.position = new Vector3f(model.position);
		this.transformMatrix = new Matrix4f(model.transformMatrix);
		if( model.animationData != null ) {
			this.animationData = new AnimationData(model.animationData);
		} else {
			this.animationData = null;
		}
		this.meshMaterialTable = model.meshMaterialTable; // NOT DEEP COPIED (assumed to stay the same for now)
	}
	
	
	@Override
	protected Model rendererCopy() {
		return new Model(this);
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

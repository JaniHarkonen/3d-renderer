package project.scene;

import java.util.ArrayList;
import java.util.List;

import project.asset.sceneasset.AnimationData;
import project.asset.sceneasset.Mesh;
import project.component.Material;
import project.component.Transform;

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
	
	private Model(Model src) {
		super(null);
		src.transformComponent.updateTransformMatrix();
		this.transformComponent = new Transform(src.transformComponent);
		if( src.animationData != null ) {
			this.animationData = new AnimationData(src.animationData);
		} else {
			this.animationData = null;
		}
		this.meshMaterialTable = src.meshMaterialTable; // NOT DEEP COPIED (assumed to stay the same for now)
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

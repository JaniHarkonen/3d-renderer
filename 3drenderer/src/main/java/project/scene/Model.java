package project.scene;

import java.util.ArrayList;
import java.util.List;

import project.asset.sceneasset.Mesh;
import project.component.Animator;
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
	private Animator animator;
	
	public Model(Scene scene) {
		super(scene);
		this.meshMaterialTable = new ArrayList<>();
		this.animator = new Animator();
	}
	
	private Model(Model src) {
		super(null, src.id);
		this.transform = new Transform(src.transform);
		this.animator = new Animator(src.animator);
		this.meshMaterialTable = src.meshMaterialTable; // NOT DEEP COPIED (assumed to stay the same for now)
	}
	
	
	@Override
	public Model rendererCopy() {
		return new Model(this);
	}
	
	@Override
	public boolean rendererEquals(ASceneObject previous) {
		if( !(previous instanceof Model) ) {
			return false;
		}
		
		Model m = (Model) previous;
		return (
			this.id == m.id && 
			this.transform.equals(m.transform) &&
			this.animator.equals(m.animator) &&
			this.meshMaterialTable == m.meshMaterialTable
		);
	}
	
	public void addMesh(Mesh mesh, Material material) {
		this.meshMaterialTable.add(new MeshEntry(mesh, material));
	}
	
	public Animator swapAnimator(Animator newAnimator) {
		newAnimator
		.setAnimation(this.animator.getAnimation())
		.setSpeed(this.animator.getSpeed())
		.setFrame(this.animator.getCurrentFrameIndex());
		this.animator = newAnimator;
		
		return this.animator;
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
	
	public Animator getAnimator() {
		return this.animator;
	}
}

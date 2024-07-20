package project.asset;

import org.joml.Matrix4f;

public class Bone {
	private int boneID;
	private String boneName;
	private Matrix4f offsetTransform;
	
	public Bone(int boneID, String boneName, Matrix4f offsetTransform) {
		this.boneID = boneID;
		this.boneName = boneName;
		this.offsetTransform = offsetTransform;
	}
	
	
	public int getID() {
		return this.boneID;
	}
	
	public String getName() {
		return this.boneName;
	}
	
	public Matrix4f getOffsetTransform() {
		return this.offsetTransform;
	}
}

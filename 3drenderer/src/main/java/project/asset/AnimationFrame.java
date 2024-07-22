package project.asset;

import org.joml.Matrix4f;

public class AnimationFrame {

	private Matrix4f[] boneTransforms;
	
	public AnimationFrame(Matrix4f[] boneTransforms) {
		this.boneTransforms = boneTransforms;
	}
	
	
	public void setBoneTransform(int boneIndex, Matrix4f boneTransform) {
		this.boneTransforms[boneIndex] = boneTransform;
	}
	
	public Matrix4f getBoneTransform(int boneIndex) {
		return this.boneTransforms[boneIndex];
	}
	
	public Matrix4f[] getBoneTransforms() {
		return this.boneTransforms;
	}
}

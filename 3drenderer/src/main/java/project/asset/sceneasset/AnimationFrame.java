package project.asset.sceneasset;

import java.util.Arrays;

import org.joml.Matrix4f;

public class AnimationFrame {
	
	public static final AnimationFrame DEFAULT = new AnimationFrame(new Matrix4f[SceneAssetLoadTask.MAX_BONE_COUNT]);
	static {
		Matrix4f zeroMatrix = new Matrix4f().zero();
		Arrays.fill(DEFAULT.boneTransforms, zeroMatrix);
	}

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

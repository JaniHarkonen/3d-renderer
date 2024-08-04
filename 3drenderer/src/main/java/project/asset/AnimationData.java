package project.asset;

import java.util.Arrays;

import org.joml.Matrix4f;

public class AnimationData {

	public static final Matrix4f[] DEFAULT_BONE_TRANSFORMS = new Matrix4f[SceneAssetLoadTask.MAX_BONE_COUNT];
	static {
		Matrix4f zeroMatrix = new Matrix4f().zero();
		Arrays.fill(DEFAULT_BONE_TRANSFORMS, zeroMatrix);
	}
	
	private Animation currentAnimation;
	private int currentFrameIndex;
	
	public AnimationData(Animation currentAnimation) {
		this.currentFrameIndex = 0;
		this.currentAnimation = currentAnimation;
	}
	
	public AnimationData(AnimationData src) {
		this.currentAnimation = new Animation(src.currentAnimation);
		this.currentFrameIndex = src.currentFrameIndex;
	}
	
	
	public void nextFrame() {
		int nextFrame = this.currentFrameIndex + 1;
		if( nextFrame > this.currentAnimation.getFrames().size() - 1 ) {
			this.currentFrameIndex = 0;
		} else {
			this.currentFrameIndex = nextFrame;
		}
	}
	
	public void setCurrentAnimation(Animation currentAnimation) {
		this.currentFrameIndex = 0;
		this.currentAnimation = currentAnimation;
	}
	
	public Animation getCurrentAnimation() {
		return this.currentAnimation;
	}
	
	public AnimationFrame getCurrentFrame() {
		return this.currentAnimation.getFrames().get(this.currentFrameIndex);
	}
	
	public int getCurrentFrameIndex() {
		return this.currentFrameIndex;
	}
}

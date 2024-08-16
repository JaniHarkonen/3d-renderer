package project.asset.sceneasset;

import java.util.Arrays;

import org.joml.Matrix4f;

import project.core.asset.IAsset;
import project.core.asset.IAssetData;

public class Animation implements IAsset {
	
	/************************* Frame-class **************************/
	
	public static class Frame {
		
		public static final Frame DEFAULT = new Frame(new Matrix4f[SceneAssetLoadTask.MAX_BONE_COUNT]);
		static {
			Matrix4f zeroMatrix = new Matrix4f().zero();
			Arrays.fill(DEFAULT.boneTransforms, zeroMatrix);
		}

		private Matrix4f[] boneTransforms;
		
		public Frame(Matrix4f[] boneTransforms) {
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
	
	
	/************************* Data-class **************************/
	
	public static class Data implements IAssetData {
		Animation targetAnimation;
		float duration;
		Frame[] frames;

		@Override
		public void assign(long timestamp) {
			this.targetAnimation.populate(this.duration, this.frames);
			this.targetAnimation.update(timestamp);
		}
	}
	
	
	/************************* Animation-class **************************/
	
	public static final Animation DEFAULT = new Animation("anim-default", 1, true);
	static {
		DEFAULT.populate(0, new Frame[] {
			Frame.DEFAULT
		});
	}
	
	private final String name;
	
	private float duration;
	private int expectedFrameCount;
	private Frame[] frames;
	private long lastUpdateTimestamp;
	
	public Animation(String name, int expectedFrameCount) {
		this(name, expectedFrameCount, false);
	}
	
	private Animation(String name, int expectedFrameCount, boolean isDefault) {
		if( isDefault ) {
			this.duration = 0.0f;
			this.frames = null;
		} else {
			this.duration = DEFAULT.duration;
			this.frames = DEFAULT.frames;
		}
		
		this.name = name;
		this.expectedFrameCount = expectedFrameCount;
		this.lastUpdateTimestamp = -1;
	}
	
	public Animation(Animation src) {
		this.name = null;
		this.duration = src.duration;
		this.frames = src.frames; // NO DEEP COPY
	}
	
	
	public void populate(float duration, Frame[] frames) {
		this.duration = duration;
		this.frames = frames;
	}
	
	@Override
	public boolean deload() {
		this.frames = null;
		return true;
	}
	
	@Override
	public void update(long timestamp) {
		this.lastUpdateTimestamp = timestamp;
	}
	
	int getExpectedFrameCount() {
		return this.expectedFrameCount;
	}
	
	public float getDuration() {
		return this.duration;
	}
	
	public Frame[] getFrames() {
		return this.frames;
	}
	
	public Frame getFrame(int frameIndex) {
		return this.frames[frameIndex];
	}
	
	public int getFrameCount() {
		return this.frames.length;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public long getLastUpdateTimestamp() {
		return this.lastUpdateTimestamp;
	}
}

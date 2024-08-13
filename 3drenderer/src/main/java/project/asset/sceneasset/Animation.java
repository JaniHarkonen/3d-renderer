package project.asset.sceneasset;

import project.core.asset.IAsset;
import project.core.asset.IAssetData;

public class Animation implements IAsset {
	
	/************************* Data-class **************************/
	
	public static class Data implements IAssetData {
		Animation targetAnimation;
		double duration;
		AnimationFrame[] frames;

		@Override
		public void assign(long timestamp) {
			this.targetAnimation.populate(this.duration, this.frames);
			this.targetAnimation.update(timestamp);
		}
	}
	
	
	/************************* Animation-class **************************/
	
	public static final Animation DEFAULT = new Animation("anim-default", 1, true);
	static {
		DEFAULT.populate(0, new AnimationFrame[] {
			AnimationFrame.DEFAULT
		});
	}
	
	private final String name;
	
	private double duration;
	private int expectedFrameCount;
	private AnimationFrame[] frames;
	private long lastUpdateTimestamp;
	
	public Animation(String name, int expectedFrameCount) {
		this(name, expectedFrameCount, false);
	}
	
	private Animation(String name, int expectedFrameCount, boolean isDefault) {
		if( isDefault ) {
			this.duration = 0.0d;
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
	
	
	public void populate(double duration, AnimationFrame[] frames) {
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
	
	public AnimationFrame[] getFrames() {
		return this.frames;
	}
	
	public AnimationFrame getFrame(int frameIndex) {
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

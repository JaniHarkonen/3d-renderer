package project.asset.sceneasset;

import java.util.ArrayList;
import java.util.List;

import project.core.asset.IAsset;
import project.core.asset.IAssetData;

public class Animation implements IAsset {
	
	/************************* Data-class **************************/
	
	public static class Data implements IAssetData {
		Animation targetAnimation;
		double duration;
		List<AnimationFrame> frames;

		@Override
		public void assign(long timestamp) {
			this.targetAnimation.populate(this.duration, this.frames);
			this.targetAnimation.update(timestamp);
		}
	}
	
	
	/************************* Animation-class **************************/
	
	public static final Animation DEFAULT = new Animation("anim-default", true);
	static {
		List<AnimationFrame> frames = new ArrayList<>();
		frames.add(AnimationFrame.DEFAULT);
		DEFAULT.populate(0, frames);
	}
	
	private final String name;
	
	private double duration;
	private List<AnimationFrame> frames;
	private long lastUpdateTimestamp;
	
	public Animation(String name) {
		this(name, false);
	}
	
	private Animation(String name, boolean isDefault) {
		if( isDefault ) {
			this.name = name;
			this.duration = 0.0d;
			this.frames = new ArrayList<>();
		} else {
			this.name = DEFAULT.name;
			this.duration = DEFAULT.duration;
			this.frames = DEFAULT.frames;
		}
		
		this.lastUpdateTimestamp = -1;
	}
	
	public Animation(Animation src) {
		this.name = null;
		this.duration = src.duration;
		this.frames = src.frames; // NO DEEP COPY
	}
	
	
	public void populate(double duration, List<AnimationFrame> frames) {
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
	
	public List<AnimationFrame> getFrames() {
		return this.frames;
	}
	
	public AnimationFrame getFrame(int frameIndex) {
		return this.frames.get(frameIndex);
	}
	
	public int getFrameCount() {
		return this.frames.size();
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

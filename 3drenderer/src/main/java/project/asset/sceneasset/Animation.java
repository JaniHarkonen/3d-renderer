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
	
	private final String name;
	
	private double duration;
	private List<AnimationFrame> frames;
	private long lastUpdateTimestamp;
	
	public Animation(String name) {
		this.name = name;
		this.duration = 0.0d;
		this.frames = new ArrayList<>();
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

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public long getLastUpdateTimestamp() {
		return this.lastUpdateTimestamp;
	}
}

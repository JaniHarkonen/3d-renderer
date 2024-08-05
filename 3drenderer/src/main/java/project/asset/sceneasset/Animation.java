package project.asset.sceneasset;

import java.util.ArrayList;
import java.util.List;

public class Animation {

	private String animationName;
	private double duration;
	private List<AnimationFrame> frames;
	
	public Animation() {
		this.animationName = null;
		this.duration = 0.0d;
		this.frames = new ArrayList<>();
	}
	
	public Animation(Animation src) {
		this.animationName = new String(src.animationName);
		this.duration = src.duration;
		this.frames = src.frames; // NO DEEP COPY
	}
	
	
	public void setName(String name) {
		this.animationName = name;
	}
	
	public void setDuration(double duration) {
		this.duration = duration;
	}
	
	public void setFrames(List<AnimationFrame> frames) {
		this.frames = frames;
	}
	
	public List<AnimationFrame> getFrames() {
		return this.frames;
	}
}

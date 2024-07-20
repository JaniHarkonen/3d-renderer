package project.asset;

import java.util.List;

public class Animation {

	private String animationName;
	private double duration;
	private List<AnimationFrame> frames;
	
	public Animation(String animationName, double duration, List<AnimationFrame> frames) {
		this.animationName = animationName;
		this.duration = duration;
		this.frames = frames;
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
}

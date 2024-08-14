package project.component;

import project.asset.sceneasset.Animation;

public class Animator {

	private Animation animation;
	private int currentFrameIndex;
	private Animation.Frame currentFrame;
	private float frameTimer;
	private float frameSpeed;
	private int direction;	// Must be 1 or -1
	private boolean isPaused;
	
	public Animator() {
		this.animation = Animation.DEFAULT;
		this.setFrame(0);
		this.frameTimer = 0.0f;
		this.frameSpeed = 1.0f;
		this.direction = 1;
		this.isPaused = false;
	}
	
	public Animator(Animator src) {
		this.animation = src.animation;
		this.currentFrameIndex = src.currentFrameIndex;
		this.currentFrame = src.currentFrame;
	}
	
	
	public void update(float deltaTime) {
		if( this.isPaused ) {
			return;
		}
		
		this.frameTimer += deltaTime;
		
		if( this.frameTimer >= this.frameSpeed ) {
			this.currentFrameIndex += this.direction;
			
			if( this.currentFrameIndex >= 0 && this.currentFrameIndex < this.animation.getFrameCount() ) {
				this.setFrame(this.currentFrameIndex);
			} else {
				this.currentFrameIndex = Math.max(
					0, Math.min(this.animation.getFrameCount(), this.currentFrameIndex)
				);
				this.onFinish();
			}
			this.frameTimer = 0.0f;
		}
	}
	
	public void onFinish() {
		this.pause();
	}
	
	public Animator restart() {
		this.setFrame(0);
		return this;
	}
	
	public Animator play() {
		this.isPaused = false;
		return this;
	}
	
	public Animator pause() {
		this.isPaused = true;
		return this;
	}
	
	public Animator stop() {
		this.pause();
		this.restart();
		return this;
	}
	
	public Animator reverse() {
		this.direction *= -1;
		return this;
	}
	
	public Animator setFrame(int frameIndex) {
		this.currentFrameIndex = frameIndex;
		this.currentFrame = this.animation.getFrame(this.currentFrameIndex);
		return this;
	}
	
	public Animator setAnimation(Animation animation) {
		this.animation = animation;
		this.setFrame(0);
		return this;
	}
	
	public Animator setSpeed(float frameDelta) {
		this.frameSpeed = frameDelta;
		return this;
	}
	
	public float getSpeed() {
		return this.frameSpeed;
	}
	
	public Animation.Frame getCurrentFrame() {
		return this.currentFrame;
	}
	
	public int getCurrentFrameIndex() {
		return this.currentFrameIndex;
	}
	
	public Animation getAnimation() {
		return this.animation;
	}
}

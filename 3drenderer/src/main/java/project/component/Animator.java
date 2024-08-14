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
	
	public void restart() {
		this.setFrame(0);
	}
	
	public void play() {
		this.isPaused = false;
	}
	
	public void pause() {
		this.isPaused = true;
	}
	
	public void stop() {
		this.pause();
		this.restart();
	}
	
	public void reverse() {
		this.direction *= -1;
	}
	
	private void setFrame(int frameIndex) {
		this.currentFrameIndex = frameIndex;
		this.currentFrame = this.animation.getFrame(this.currentFrameIndex);
	}
	
	public void setAnimation(Animation animation) {
		this.animation = animation;
		this.setFrame(0);
	}
	
	public void setSpeed(float frameDelta) {
		this.frameSpeed = frameDelta;
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

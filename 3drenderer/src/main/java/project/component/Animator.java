package project.component;

import project.asset.sceneasset.Animation;

public class Animator {

	private Animation animation;
	private int currentFrameIndex;
	private Animation.Frame currentFrame;
	private float frameTimer;
	private float frameSpeed;
	private boolean isPaused;
	
	public Animator() {
		this.animation = Animation.DEFAULT;
		this.setFrame(0);
		this.frameTimer = 0.0f;
		this.frameSpeed = 1.0f;
		this.isPaused = false;
	}
	
	
	public void update(float deltaTime) {
		if( this.isPaused ) {
			return;
		}
		
		this.frameTimer += deltaTime;
		
		if( this.frameTimer >= this.frameSpeed ) {
			if( this.currentFrameIndex + 1 < this.animation.getFrameCount() ) {
				this.setFrame(++this.currentFrameIndex);
			} else {
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
	
	public Animation getAnimation() {
		return this.animation;
	}
}

package project.input;

public abstract class AInputEvent {
	
	protected final int inputKey;
	protected final float[] thresholds;
	protected final float[] axes;
	
	protected AInputEvent(int inputKey, float... thresholds) {
		this.inputKey = inputKey;
		this.thresholds = thresholds;
		this.axes = new float[thresholds.length];
	}
	
	
	protected void setAxis(int axis, float intensity) {
		this.axes[axis] = intensity;
	}
	
	protected float getAxis(int axis) {
		return this.axes[axis];
	}
	
	protected float getThreshold(int axis) {
		return this.thresholds[axis];
	}
	
	public abstract void poll();
	
	public boolean checkThreshold(int axis) {
		return (this.axes[axis] >= this.thresholds[axis]);
	}
	
	public float getIntensity(int axis) {
		return this.axes[axis];
	}
	
	public float getIntensityBeyondThreshold(int axis) {
		return this.axes[axis] >= this.thresholds[axis] ? this.axes[axis] : 0.0f;
	}
	
	public int getAxisCount() {
		return this.axes.length;
	}
}

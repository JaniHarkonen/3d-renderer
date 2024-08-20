package project.input;

public class MouseMove extends AInputEvent {
	public MouseMove(Input hostInput) {
		super(hostInput, -1, 0.0f, 0.0f);
	}
	
	
	@Override
	public void poll() {
		InputSnapshot front = this.hostInput.getLatestInput();
		this.setAxis(0, (float) front.getMouseDeltaX());
		this.setAxis(1, (float) front.getMouseDeltaY());
	}
	
	@Override
	public boolean checkThreshold(int axis) {
		return this.getAxis(axis) != this.getThreshold(axis);
	}
	
	@Override
	public float getIntensityBeyondThreshold(int axis) {
		return (
			this.getAxis(axis) != this.getThreshold(axis) ? 
			this.getAxis(axis) : 0.0f
		);
	}
}

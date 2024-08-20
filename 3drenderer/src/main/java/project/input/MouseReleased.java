package project.input;

public class MouseReleased extends AInputEvent {
	public MouseReleased(Input hostInput, int key) {
		super(hostInput, key, 0.0f);
	}
	
	
	@Override
	public void poll() {
		this.setAxis(0, this.hostInput.getLatestInput().isMouseButtonReleased(this.inputKey) ? 1.0f : 0.0f);
	}
}

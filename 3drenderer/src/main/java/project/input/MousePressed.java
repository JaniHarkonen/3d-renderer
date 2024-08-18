package project.input;

public class MousePressed extends AInputEvent {
	public MousePressed(Input hostInput, int key) {
		super(hostInput, key, 0.0f);
	}
	
	
	@Override
	public void poll() {
		this.setAxis(0, this.hostInput.getLatestInput().isMouseButtonPressed(this.inputKey) ? 1.0f : 0.0f);
	}
}

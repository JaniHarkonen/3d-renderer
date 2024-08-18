package project.input;

public class MouseHeld extends AInputEvent {
	public MouseHeld(Input hostInput, int key) {
		super(hostInput, key, 0.0f);
	}
	
	
	@Override
	public void poll() {
		this.setAxis(0, this.hostInput.getLatestInput().isMouseButtonHeld(this.inputKey) ? 1.0f : 0.0f);
	}
}	

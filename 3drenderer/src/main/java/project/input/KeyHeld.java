package project.input;

public class KeyHeld extends AInputEvent {
	public KeyHeld(Input hostInput, int key) {
		super(hostInput, key, 0.0f);
	}
	
	
	@Override
	public void poll() {
		this.setAxis(0, this.hostInput.getLatestInput().isKeyHeld(this.inputKey) ? 1.0f : 0.0f);
	}
}

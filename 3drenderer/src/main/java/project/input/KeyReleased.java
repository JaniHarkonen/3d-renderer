package project.input;

public class KeyReleased extends AInputEvent {
	public KeyReleased(Input hostInput, int key) {
		super(hostInput, key, 0.0f);
	}
	
	
	@Override
	public void poll() {
		this.setAxis(0, this.hostInput.getLatestInput().isKeyReleased(this.inputKey) ? 1.0f : 0.0f);
	}
}

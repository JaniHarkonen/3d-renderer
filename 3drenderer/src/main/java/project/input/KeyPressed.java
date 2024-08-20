package project.input;

public class KeyPressed extends AInputEvent {
	public KeyPressed(Input hostInput, int key) {
		super(hostInput, key, 0.0f);
	}
	
	
	@Override
	public void poll() {
		this.setAxis(0, this.hostInput.getLatestInput().isKeyPressed(this.inputKey) ? 1.0f : 0.0f);
	}
}

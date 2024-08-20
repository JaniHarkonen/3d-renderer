package project.input;

public class NoKey extends AInputEvent {
	public NoKey(Input hostInput) {
		super(hostInput, -1, 0.0f);
	}
	
	
	@Override
	public void poll() {
		this.setAxis(0, this.hostInput.getLatestInput().isNoKeyHeld() ? 1.0f : 0.0f);
	}
}
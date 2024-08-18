package project.input;

public class AnyKeyHeld extends AInputEvent {
	public AnyKeyHeld(Input hostInput) {
		super(hostInput, -1, 0.0f);
	}
	
	
	@Override
	public void poll() {
		this.setAxis(0, this.hostInput.getLatestInput().isAnyKeyHeld() ? 1.0f : 0.0f);
	}
}

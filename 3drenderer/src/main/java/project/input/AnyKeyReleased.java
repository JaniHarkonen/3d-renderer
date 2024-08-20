package project.input;

public class AnyKeyReleased extends AInputEvent {
	public AnyKeyReleased(Input hostInput) {
		super(hostInput, -1, 0.0f);
	}
	
	
	@Override
	public void poll() {
		this.setAxis(0, this.hostInput.getLatestInput().isAnyKeyReleased() ? 1.0f : 0.0f);
	}
}

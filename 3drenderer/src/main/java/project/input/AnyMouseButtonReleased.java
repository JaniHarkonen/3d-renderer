package project.input;

public class AnyMouseButtonReleased extends AInputEvent {
	public AnyMouseButtonReleased(Input hostInput) {
		super(hostInput,-1, 0.0f);
	}
	
	
	@Override
	public void poll() {
		this.setAxis(0, this.hostInput.getLatestInput().isAnyMouseButtonReleased() ? 1.0f : 0.0f);
	}
}

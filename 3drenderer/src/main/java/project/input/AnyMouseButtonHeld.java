package project.input;

public class AnyMouseButtonHeld extends AInputEvent {
	public AnyMouseButtonHeld(Input hostInput) {
		super(hostInput, -1, 0.0f);
	}
	
	
	@Override
	public void poll() {
		this.setAxis(0, this.hostInput.getLatestInput().isAnyMouseButtonHeld() ? 1.0f : 0.0f);
	}
}

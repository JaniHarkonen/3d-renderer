package project.input;

public class AnyMouseButtonPressed extends AInputEvent {
	public AnyMouseButtonPressed(Input hostInput) {
		super(hostInput, -1, 0.0f);
	}
	
	
	@Override
	public void poll() {
		this.setAxis(0, this.hostInput.getLatestInput().isAnyMouseButtonPressed() ? 1.0f : 0.0f);
	}
}

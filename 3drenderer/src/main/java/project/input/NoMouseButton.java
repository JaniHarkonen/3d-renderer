package project.input;

public class NoMouseButton extends AInputEvent {
	public NoMouseButton(Input hostInput) {
		super(hostInput, -1, 0.0f);
	}
	
	
	@Override
	public void poll() {
		this.setAxis(0, this.hostInput.getLatestInput().isNoMouseButtonHeld() ? 1.0f : 0.0f);
	}
}

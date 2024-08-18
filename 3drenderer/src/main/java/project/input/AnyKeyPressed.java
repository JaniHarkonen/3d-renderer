package project.input;

public class AnyKeyPressed extends AInputEvent {
	public AnyKeyPressed(Input hostInput) {
		super(hostInput, -1, 0.0f);
	}
	
	
	@Override
	public void poll() {
		this.setAxis(0, this.hostInput.getLatestInput().isAnyKeyPressed() ? 1.0f : 0.0f);
	}
}

package project.controls;

public interface IControllable {

	public void control(Action action, float deltaTime);
	
	public void setController(Controller controller);
}

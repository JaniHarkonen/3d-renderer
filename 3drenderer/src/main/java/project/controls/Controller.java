package project.controls;

import java.util.ArrayList;
import java.util.List;

import project.input.AInputEvent;
import project.input.Input;

public class Controller {
	private Input input;
	private List<Action> actionList;
	private IControllable target;
	
	public Controller(Input input, IControllable target) {
		this.input = input;
		this.actionList = new ArrayList<>();
		this.target = target;
	}
	
	
	public void tick(float deltaTime) {
		for( Action action : this.actionList ) {
			if( action.pollInput() ) {
				this.target.control(action, deltaTime);
			}
		}
	}
	
	public Controller addBinding(int actionID, AInputEvent inputEvent) {
		this.actionList.add(new Action(actionID, inputEvent));
		return this;
	}
	
	public Input getInput() {
		return this.input;
	}
}

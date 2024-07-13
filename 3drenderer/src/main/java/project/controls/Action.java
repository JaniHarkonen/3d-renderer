package project.controls;

import project.input.AInputEvent;

public class Action {

	private final int actionID;
	private final AInputEvent inputEvent;
	
	public Action(int actionID, AInputEvent inputEvent) {
		this.actionID = actionID;
		this.inputEvent = inputEvent;
	}
	
	
	public boolean pollInput() {
		this.inputEvent.poll();
		
		for( int i = 0; i < this.inputEvent.getAxisCount(); i++ ) {
			if( this.inputEvent.checkThreshold(i) ) {
				return true;
			}
		}
		
		return false;
	}
	
	public float getIntensity() {
		return this.getAxisIntensity(0);
	}
	
	public float getAxisIntensity(int axis) {
		return this.inputEvent.getIntensityBeyondThreshold(axis);
	}
	
	public AInputEvent getInputEvent() {
		return this.inputEvent;
	}
	
	public int getActionID() {
		return this.actionID;
	}
}

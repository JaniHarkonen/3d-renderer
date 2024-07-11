package project.input;

import org.lwjgl.glfw.GLFW;

public class InputSnapshot {

	private static final int KEY_MAP_SIZE = GLFW.GLFW_KEY_LAST + 1;
	private static final int MOUSE_BUTTON_MAP_SIZE = GLFW.GLFW_MOUSE_BUTTON_LAST + 1;
	
	public static final int ACTION_NONE = 0;
	public static final int ACTION_PRESSED = 1;
	public static final int ACTION_RELEASED = 2;
	public static final int ACTION_HELD = 3;
	
	private int[] keyMap;
	private int[] mouseButtonMap;
	
	private double mouseX;
	private double mouseY;
	private double mouseDeltaX;
	private double mouseDeltaY;
	
		// Add keyboard string
	
	public InputSnapshot() {
		this.keyMap = new int[InputSnapshot.KEY_MAP_SIZE];
		this.mouseButtonMap = new int[InputSnapshot.MOUSE_BUTTON_MAP_SIZE];
	}
	
	public void snapshot(InputSnapshot target) {
		for( int i = 0; i < this.keyMap.length; i++ ) {
			target.keyMap[i] = this.keyMap[i];
			
			if( this.keyMap[i] == InputSnapshot.ACTION_PRESSED ) {
				this.keyMap[i] = InputSnapshot.ACTION_HELD;
			} else if( this.keyMap[i] == InputSnapshot.ACTION_RELEASED ) {
				this.keyMap[i] = InputSnapshot.ACTION_NONE;
			}
		}
		
		for( int i = 0; i < this.mouseButtonMap.length; i++ ) {
			target.mouseButtonMap[i] = this.mouseButtonMap[i];
			
			if( this.mouseButtonMap[i] == InputSnapshot.ACTION_PRESSED ) {
				this.mouseButtonMap[i] = InputSnapshot.ACTION_HELD;
			} else if( this.mouseButtonMap[i] == InputSnapshot.ACTION_RELEASED ) {
				this.mouseButtonMap[i] = InputSnapshot.ACTION_NONE;
			}
		}
		
		target.mouseX = this.mouseX;
		target.mouseY = this.mouseY;
		target.mouseDeltaX = this.mouseDeltaX;
		target.mouseDeltaY = this.mouseDeltaY;
		
			// Reset accumulated mouse delta
		this.mouseDeltaX = 0.0d;
		this.mouseDeltaY = 0.0d;
	}
	
	void setKey(int key, int action) {
		this.keyMap[key] = action;
	}
	
	void attemptToPressKey(int key) {
		if(
			this.keyMap[key] != InputSnapshot.ACTION_HELD && 
			this.keyMap[key] != InputSnapshot.ACTION_RELEASED 
		) {
			this.keyMap[key] = InputSnapshot.ACTION_PRESSED;
		}
	}
	
	void attemptToReleaseKey(int key) {
		if( this.keyMap[key] != InputSnapshot.ACTION_NONE ) {
			this.keyMap[key] = InputSnapshot.ACTION_RELEASED;
		}
	}
	
	void setMouseButton(int mouseButton, int action) {
		this.mouseButtonMap[mouseButton] = action;
	}
	
	void setMouseMetrics(double mouseX, double mouseY, double mouseDeltaX, double mouseDeltaY) {
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		this.mouseDeltaX += mouseDeltaX;
		this.mouseDeltaY += mouseDeltaY;
	}
	
	public boolean isKeyHeld(int key) {
		return this.keyMap[key] == InputSnapshot.ACTION_HELD;
	}
	
	public boolean isKeyReleased(int key) {
		return this.keyMap[key] == InputSnapshot.ACTION_RELEASED;
	}
	
	public boolean isKeyPressed(int key) {
		return this.keyMap[key] == InputSnapshot.ACTION_PRESSED;
	}
	
	public float getMouseX() {
		return (float) this.mouseX;
	}
	
	public float getMouseY() {
		return (float) this.mouseY;
	}
	
	public float getMouseDeltaX() {
		return (float) this.mouseDeltaX;
	}
	
	public float getMouseDeltaY() {
		return (float) this.mouseDeltaY;
	}
}

package project.input;

import org.lwjgl.glfw.GLFW;

public class InputSnapshot {

	public static final int DEFAULT_MAX_KEYBOARD_STRING_LENTH = 50;
	
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
	private double previousMouseX;
	private double previousMouseY;
	private double mouseDeltaX;
	private double mouseDeltaY;
	
	private String keyboardString;
	private int maxKeyboardStringLength;
	
	public InputSnapshot() {
		this.keyMap = new int[InputSnapshot.KEY_MAP_SIZE];
		this.mouseButtonMap = new int[InputSnapshot.MOUSE_BUTTON_MAP_SIZE];
		
		this.mouseX = 0.0d;
		this.mouseY = 0.0d;
		this.previousMouseX = 0.0d;
		this.previousMouseY = 0.0d;
		this.mouseDeltaX = 0.0d;
		this.mouseDeltaY = 0.0d;
		
		this.keyboardString = "";
		this.maxKeyboardStringLength = InputSnapshot.DEFAULT_MAX_KEYBOARD_STRING_LENTH;
	}
	
	public void snapshot(InputSnapshot target) {
		
			// Update the key presses of this snapshot and the target snapshot
		for( int i = 0; i < this.keyMap.length; i++ ) {
			target.keyMap[i] = this.keyMap[i];
			
			if( this.keyMap[i] == InputSnapshot.ACTION_PRESSED ) {
				this.keyMap[i] = InputSnapshot.ACTION_HELD;
			} else if( this.keyMap[i] == InputSnapshot.ACTION_RELEASED ) {
				this.keyMap[i] = InputSnapshot.ACTION_NONE;
			}
		}

			// Update the mouse presses of this snapshot and the target snapshot
		for( int i = 0; i < this.mouseButtonMap.length; i++ ) {
			target.mouseButtonMap[i] = this.mouseButtonMap[i];
			
			if( this.mouseButtonMap[i] == InputSnapshot.ACTION_PRESSED ) {
				this.mouseButtonMap[i] = InputSnapshot.ACTION_HELD;
			} else if( this.mouseButtonMap[i] == InputSnapshot.ACTION_RELEASED ) {
				this.mouseButtonMap[i] = InputSnapshot.ACTION_NONE;
			}
		}
		
			// Update the mouse metrics of the target snapshot
		target.previousMouseX = target.mouseX;
		target.previousMouseY = target.mouseY;
		target.mouseX = this.mouseX;
		target.mouseY = this.mouseY;
		target.mouseDeltaX = target.mouseX - target.previousMouseX;
		target.mouseDeltaY = target.mouseY - target.previousMouseY;
		
			// Update the keyboard string of this snapshot and the target snapshot
			// Also only keep track of the last 'maxKeyboardStringLength' characters
		int concatenationLength = target.keyboardString.length() + this.keyboardString.length();
		if( concatenationLength > this.maxKeyboardStringLength ) {
			int cutoff = Math.min(
				this.maxKeyboardStringLength, concatenationLength - this.maxKeyboardStringLength
			);
			
			target.keyboardString = target.keyboardString.substring(cutoff) + this.keyboardString;
		} else {
			target.keyboardString += this.keyboardString;
		}
		
		this.keyboardString = "";
	}
	
	private void attemptToPress(int[] map, int input) {
		if(
			map[input] != InputSnapshot.ACTION_HELD && 
			map[input] != InputSnapshot.ACTION_RELEASED 
		) {
			map[input] = InputSnapshot.ACTION_PRESSED;
		}
	}
	
	private void attemptToRelease(int[] map, int input) {
		if( map[input] != InputSnapshot.ACTION_NONE ) {
			map[input] = InputSnapshot.ACTION_RELEASED;
		}
	}
	
	void attemptToPressKey(int key) {
		this.attemptToPress(this.keyMap, key);
	}
	
	void attemptToReleaseKey(int key) {
		this.attemptToRelease(this.keyMap, key);
	}
	
	void attemptToPressMouseButton(int button) {
		this.attemptToPress(this.mouseButtonMap, button);
	}
	
	void attemptToReleaseMouseButton(int button) {
		this.attemptToRelease(this.mouseButtonMap, button);
	}
	
	void updateKeyboardString(char keyboardCharacter) {
		this.keyboardString += keyboardCharacter;
	}
	
	void setMouseButton(int mouseButton, int action) {
		this.mouseButtonMap[mouseButton] = action;
	}
	
	void setMousePosition(double mouseX, double mouseY) {
		this.mouseX = mouseX;
		this.mouseY = mouseY;
	}
	
	public void resetKeyboardString() {
		this.keyboardString = "";
	}
	
	public boolean isKeyHeld(int key) {
		return (this.keyMap[key] == InputSnapshot.ACTION_HELD);
	}
	
	public boolean isKeyReleased(int key) {
		return (this.keyMap[key] == InputSnapshot.ACTION_RELEASED);
	}
	
	public boolean isKeyPressed(int key) {
		return (this.keyMap[key] == InputSnapshot.ACTION_PRESSED);
	}
	
	public boolean isMouseButtonPressed(int button) {
		return (this.mouseButtonMap[button] == InputSnapshot.ACTION_PRESSED);
	}
	
	public boolean isMouseButtonHeld(int button) {
		return (this.mouseButtonMap[button] == InputSnapshot.ACTION_HELD);
	}
	
	public boolean isMouseButtonReleased(int button) {
		return (this.mouseButtonMap[button] == InputSnapshot.ACTION_RELEASED);
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
	
	public String getKeyboardString() {
		return this.keyboardString;
	}
}

package project.input;

import org.lwjgl.glfw.GLFW;

public class InputSnapshot {

	public static final int DEFAULT_MAX_KEYBOARD_STRING_LENGTH = 50;
	
	private static final int KEY_MAP_SIZE = GLFW.GLFW_KEY_LAST + 1;
	private static final int MOUSE_BUTTON_MAP_SIZE = GLFW.GLFW_MOUSE_BUTTON_LAST + 1;
	
	public static final int ACTION_NONE = 0;
	public static final int ACTION_PRESSED = 1;
	public static final int ACTION_RELEASED = 2;
	public static final int ACTION_HELD = 3;
	
	private int[] keyMap;
	private int[] mouseButtonMap;
	
	private boolean[] anyKey;
	private boolean[] anyMouseButton;
	
	private double mouseX;
	private double mouseY;
	private double previousMouseX;
	private double previousMouseY;
	private double mouseDeltaX;
	private double mouseDeltaY;
	
	private String keyboardString;
	private int maxKeyboardStringLength;
	
	public InputSnapshot() {
		this.keyMap = new int[KEY_MAP_SIZE];
		this.mouseButtonMap = new int[MOUSE_BUTTON_MAP_SIZE];
		
		this.anyKey = new boolean[ACTION_HELD + 1];
		this.anyKey[ACTION_NONE] = true;
		this.anyKey[ACTION_PRESSED] = false;
		this.anyKey[ACTION_RELEASED] = false;
		this.anyKey[ACTION_HELD] = false;
		
		this.anyMouseButton = new boolean[ACTION_HELD + 1];
		this.anyMouseButton[ACTION_NONE] = true;
		this.anyMouseButton[ACTION_PRESSED] = false;
		this.anyMouseButton[ACTION_RELEASED] = false;
		this.anyMouseButton[ACTION_HELD] = false;
		
		this.mouseX = 0.0d;
		this.mouseY = 0.0d;
		this.previousMouseX = 0.0d;
		this.previousMouseY = 0.0d;
		this.mouseDeltaX = 0.0d;
		this.mouseDeltaY = 0.0d;
		
		this.keyboardString = "";
		this.maxKeyboardStringLength = DEFAULT_MAX_KEYBOARD_STRING_LENGTH;
	}
	
	public void snapshot(InputSnapshot target) {
		this.updateSnapshotMap(target.keyMap, this.keyMap, target.anyKey, this.anyKey);
		this.updateSnapshotMap(target.mouseButtonMap, this.mouseButtonMap, target.anyMouseButton, this.anyMouseButton);
		
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
	
	private void updateSnapshotMap(
		int[] targetMap, int[] sourceMap, boolean[] targetAny, boolean[] sourceAny
	) {
		for( int i = 0; i < targetAny.length; i++ ) {
			targetAny[i] = false;
		}
		
		int noActionCount = 0;
		for( int i = 0; i < sourceMap.length; i++ ) {
			targetMap[i] = sourceMap[i];
			
			if( sourceMap[i] == ACTION_HELD ) {
				targetAny[ACTION_HELD] = true;
			}
			
			if( sourceMap[i] == ACTION_PRESSED ) {
				sourceMap[i] = ACTION_HELD;
				targetAny[ACTION_PRESSED] = true;
			} else if( sourceMap[i] == ACTION_RELEASED ) {
				sourceMap[i] = ACTION_NONE;
				targetAny[ACTION_RELEASED] = true;
			}
			
			if( sourceMap[i] == ACTION_NONE ) {
				noActionCount++;
			}
		}
		
		if( noActionCount == sourceMap.length ) {
			targetAny[ACTION_NONE] = true;
		}
	}
	
	private void attemptToPress(int[] map, int input) {
		if(
			map[input] != ACTION_HELD && 
			map[input] != ACTION_RELEASED 
		) {
			map[input] = ACTION_PRESSED;
		}
	}
	
	private void attemptToRelease(int[] map, int input) {
		if( map[input] != ACTION_NONE ) {
			map[input] = ACTION_RELEASED;
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
	
	public boolean isNoKeyHeld() {
		return this.anyKey[ACTION_NONE];
	}
	
	public boolean isAnyKeyPressed() {
		return this.anyKey[ACTION_PRESSED];
	}
	
	public boolean isAnyKeyReleased() {
		return this.anyKey[ACTION_RELEASED];
	}
	
	public boolean isAnyKeyHeld() {
		return this.anyKey[ACTION_HELD];
	}
	
	public boolean isKeyHeld(int key) {
		return (this.keyMap[key] == ACTION_HELD);
	}
	
	public boolean isKeyReleased(int key) {
		return (this.keyMap[key] == ACTION_RELEASED);
	}
	
	public boolean isKeyPressed(int key) {
		return (this.keyMap[key] == ACTION_PRESSED);
	}
	
	public boolean isMouseButtonPressed(int button) {
		return (this.mouseButtonMap[button] == ACTION_PRESSED);
	}
	
	public boolean isMouseButtonHeld(int button) {
		return (this.mouseButtonMap[button] == ACTION_HELD);
	}
	
	public boolean isMouseButtonReleased(int button) {
		return (this.mouseButtonMap[button] == ACTION_RELEASED);
	}
	
	public boolean isNoMouseButtonHeld() {
		return this.anyMouseButton[ACTION_NONE];
	}
	
	public boolean isAnyMouseButtonPressed() {
		return this.anyMouseButton[ACTION_PRESSED];
	}
	
	public boolean isAnyMouseButtonReleased() {
		return this.anyMouseButton[ACTION_RELEASED];
	}
	
	public boolean isAnyMouseButtonHeld() {
		return this.anyMouseButton[ACTION_HELD];
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

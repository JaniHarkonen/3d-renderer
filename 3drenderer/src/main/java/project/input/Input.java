package project.input;

import org.lwjgl.glfw.GLFW;

import project.Window;
import project.utils.DebugUtils;

public class Input {
	private Window clientWindow;
	private InputSnapshot front;
	private InputSnapshot back;
	
	public Input() {
		this.clientWindow = null;
		this.front = null;
		this.back = null;
	}

	
	public void bind(Window clientWindow) {
		this.clientWindow = clientWindow;
		this.front = new InputSnapshot();
		this.back = new InputSnapshot();
		
			// Bind key listener
		long windowHandle = this.clientWindow.getHandle();
		GLFW.glfwSetKeyCallback(
			windowHandle, 
			(window, key, scanCode, action, mods) -> {
				this.keyListener(window, key, scanCode, action, mods);
			}
		);
		
			// Bind mouse button listener
		GLFW.glfwSetMouseButtonCallback(
			windowHandle, 
			(window, button, action, mods) -> {
				this.mouseButtonListener(window, button, action, mods);
			}
		);
		
			// Bind mouse position listener
		GLFW.glfwSetCursorPosCallback(windowHandle, (window, mouseX, mouseY) -> {
			this.mousePositionListener(window, mouseX, mouseY);
		});
		
			// Bind keyboard character input listener (keyboard string)
		GLFW.glfwSetCharCallback(windowHandle, (window, codePoint) -> {
			this.keyboardCharacterListener(window, codePoint);
		});
	}
	
	public void updateBackSnapshot() {
		GLFW.glfwPollEvents();
	}
	
	public void updateFrontSnapshot() {
		this.back.snapshot(this.front);
	}
	
	private void keyListener(
		long window, int key, int scanCode, int action, int mods
	) {
		switch( action ) {
			case GLFW.GLFW_REPEAT:
			case GLFW.GLFW_PRESS: this.back.attemptToPressKey(key); break;
			
			case GLFW.GLFW_RELEASE: this.back.attemptToReleaseKey(key); break;
			
			default: {
				DebugUtils.log(
					this,
					"WARNING: Unknown action code '" + action + "' on keyboard key '" + 
					key + "'!"
				);
			} break;
		}
	}
	
	private void mousePositionListener(long window, double xpos, double ypos) {
		this.back.setMousePosition(xpos, ypos);
	}
	
	private void mouseButtonListener(long window, int button, int action, int mods) {
		switch( action ) {
			case GLFW.GLFW_REPEAT:
			case GLFW.GLFW_PRESS: {
				this.back.attemptToPressMouseButton(button);
			} break;
			
			case GLFW.GLFW_RELEASE: this.back.attemptToReleaseMouseButton(button); break;
			
			default: {
				DebugUtils.log(
					this,
					"WARNING: Unknown action code '" + action + "' on mouse button '" + 
					button + "'!"
				);
			} break;
		}
	}
	
	private void keyboardCharacterListener(long window, int codePoint) {
		this.back.updateKeyboardString((char) codePoint);
	}
	
	public InputSnapshot getLatestInput() {
		return this.front;
	}
}

package project.input;

import org.lwjgl.glfw.GLFW;

import project.Window;
import project.utils.DebugUtils;

public class Input {
	
	public class KeyPressed extends AInputEvent {
		public KeyPressed(int key) {
			super(key, 0.0f);
		}
		
		
		@Override
		public void poll() {
			this.setAxis(0, Input.this.front.isKeyPressed(this.inputKey) ? 1.0f : 0.0f);
		}
	}
	
	public class KeyHeld extends AInputEvent {
		public KeyHeld(int key) {
			super(key, 0.0f);
		}
		
		
		@Override
		public void poll() {
			this.setAxis(0, Input.this.front.isKeyHeld(this.inputKey) ? 1.0f : 0.0f);
		}
	}
	
	public class KeyReleased extends AInputEvent {
		public KeyReleased(int key) {
			super(key, 0.0f);
		}
		
		
		@Override
		public void poll() {
			this.setAxis(0, Input.this.front.isKeyReleased(this.inputKey) ? 1.0f : 0.0f);
		}
	}
	
	public class MousePressed extends AInputEvent {
		public MousePressed(int key) {
			super(key, 0.0f);
		}
		
		
		@Override
		public void poll() {
			this.setAxis(0, Input.this.front.isMouseButtonPressed(this.inputKey) ? 1.0f : 0.0f);
		}
	}
	
	public class MouseHeld extends AInputEvent {
		public MouseHeld(int key) {
			super(key, 0.0f);
		}
		
		
		@Override
		public void poll() {
			this.setAxis(0, Input.this.front.isMouseButtonHeld(this.inputKey) ? 1.0f : 0.0f);
		}
	}
	
	public class MouseReleased extends AInputEvent {
		public MouseReleased(int key) {
			super(key, 0.0f);
		}
		
		
		@Override
		public void poll() {
			this.setAxis(0, Input.this.front.isMouseButtonReleased(this.inputKey) ? 1.0f : 0.0f);
		}
	}
	
	public class MouseMove extends AInputEvent {
		public MouseMove() {
			super(-1, 0.0f, 0.0f);
		}
		
		
		@Override
		public void poll() {
			this.setAxis(0, (float) Input.this.front.getMouseDeltaX());
			this.setAxis(1, (float) Input.this.front.getMouseDeltaY());
		}
		
		@Override
		public boolean checkThreshold(int axis) {
			return this.getAxis(axis) != this.getThreshold(axis);
		}
		
		@Override
		public float getIntensityBeyondThreshold(int axis) {
			return (
				this.getAxis(axis) != this.getThreshold(axis) ? 
				this.getAxis(axis) : 0.0f
			);
		}
	}
	
	
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
		GLFW.glfwSetCursorPosCallback(
			windowHandle, 
			(window, mouseX, mouseY) -> {
				this.mousePositionListener(window, mouseX, mouseY);
			}
		);
		
			// Bind keyboard character input listener (keyboard string)
		GLFW.glfwSetCharCallback(
			windowHandle,
			(window, codePoint) -> {
				this.keyboardCharacterListener(window, codePoint);
			}
		);
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

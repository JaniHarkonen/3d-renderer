package project.input;

import org.lwjgl.glfw.GLFW;

import project.Window;
import project.utils.DebugUtils;

public class Input {
	private Window clientWindow;
	private InputSnapshot frontSnapshot;
	private InputSnapshot backSnapshot;
	
	public Input() {
		this.clientWindow = null;
		this.frontSnapshot = null;
		this.backSnapshot = null;
	}

	
	public void bind(Window clientWindow) {
		this.clientWindow = clientWindow;
		this.frontSnapshot = new InputSnapshot();
		this.backSnapshot = new InputSnapshot();
		
		/*GLFW.glfwSetKeyCallback(this.windowHandle, (window, key, scancode, action, mods) -> {
			if( key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_PRESS ) {
				GLFW.glfwSetWindowShouldClose(this.windowHandle, true);
			}
		});*/
		
			// Bind key listener
		long windowHandle = this.clientWindow.getHandle();
		GLFW.glfwSetKeyCallback(
			windowHandle, 
			(window, key, scanCode, action, mods) -> {
				this.keyListener(window, key, scanCode, action, mods);
			}
		);
		
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
		GLFW.glfwSetCursorPos(
			this.clientWindow.getHandle(), 
			this.clientWindow.getWidth() / 2, 
			this.clientWindow.getHeight() / 2
		);
	}
	
	public void updateFrontSnapshot() {
		this.backSnapshot.snapshot(this.frontSnapshot);
	}
	
	private void keyListener(
		long window, int key, int scanCode, int action, int mods
	) {
		switch( action ) {
			case GLFW.GLFW_REPEAT:
			case GLFW.GLFW_PRESS: {
				this.backSnapshot.attemptToPressKey(key);
			} break;
			
			case GLFW.GLFW_RELEASE: this.backSnapshot.attemptToReleaseKey(key); break;
			
			default: DebugUtils.log(this, "WARNING: Unknown action code '" + action + "'!"); return;
		}
	}
	
	private void mousePositionListener(long window, double xpos, double ypos) {
		this.backSnapshot.setMouseMetrics(
			xpos, 
			ypos, 
			xpos - (this.clientWindow.getWidth() / 2), 
			ypos - (this.clientWindow.getHeight() / 2)
		);
	}
	
	private void mouseButtonListener(long window, int button, int action, int mods) {
		
	}
	
	private void keyboardCharacterListener(long window, int codePoint) {
		
	}
	
	public InputSnapshot getSnapshot() {
		return this.frontSnapshot;
	}
}

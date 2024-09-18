package project;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryUtil;

import project.core.renderer.IRenderer;
import project.input.Input;
import project.input.InputSnapshot;
import project.shared.logger.Logger;

public class Window {
	private long windowHandle;
	private boolean isDestroyed;
	
	private String title;
	private int width;
	private int height;
	private int fpsMax;
	private int vsync;
	
	private int fps;
	private int fpsCounter;
	private long fpsTimer;
	
	private long frameDelta;
	private long frameTimer;
	
	private IRenderer renderer;
	private Input input;
	private InputSnapshot latestInputSnapshot;
	
	public Window(String title, int width, int height, int fpsMax, int vsync) {
		this.windowHandle = MemoryUtil.NULL;
		this.isDestroyed = true;
		this.title = title;
		this.width = width;
		this.height = height;
		this.fpsMax = fpsMax;
		this.vsync = vsync;
		
		this.fps = 0;
		this.fpsTimer = 0;
		this.fpsCounter = 0;
		
		this.frameDelta = 1000000000 / this.fpsMax;
		this.frameTimer = 0;
		
		this.renderer = null;
		this.input = null;
		this.latestInputSnapshot = null;
	}
	
	public void initialize() {
		GLFW.glfwInit();
		
		GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, 4);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 6);
		
			// Create the window
		this.windowHandle = GLFW.glfwCreateWindow(
			this.width, this.height, this.title, MemoryUtil.NULL, MemoryUtil.NULL
		);
		
		if( this.windowHandle == MemoryUtil.NULL ) {
			Logger.get().error(this, "Unable to create window!");
			return;
		}
		
			// Center the window
		GLFWVidMode videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		GLFW.glfwSetWindowPos(
			this.windowHandle, 
			videoMode.width() / 2 - this.width / 2, 
			videoMode.height() / 2 - this.height / 2
		);
		
			// Disable the cursor to allow free range of motion
			// (enable the cursor, once interacting with menus or other window bound UI elements)
		GLFW.glfwSetInputMode(
            this.windowHandle, 
            GLFW.GLFW_CURSOR, 
            GLFW.GLFW_CURSOR_DISABLED
        );
		
			// Bind input listener
		this.input = new Input();
		this.input.bind(this);
		
		GLFW.glfwSetWindowSizeCallback(
			this.windowHandle, (window, width, height) -> this.windowResizeListener(width, height)
		);
		
		GLFW.glfwMakeContextCurrent(this.windowHandle);
		GLFW.glfwSwapInterval(this.vsync); // v-sync
		this.isDestroyed = false;
		
		this.renderer.initialize();
	}
	
	public void refresh() {
		this.input.updateBackSnapshot();
		
			// FPS-counter
		if( System.nanoTime() - this.fpsTimer >= 1000000000 ) {
			this.fpsTimer = System.nanoTime();
			this.fps = this.fpsCounter;
			this.fpsCounter = 0;
		}
		
			// Only refresh if the frame timer allows it
			// This will cap the FPS to the given FPS max
		if( System.nanoTime() - this.frameTimer < this.frameDelta ) {
			return;
		}
		
		this.frameTimer = System.nanoTime();
		
			// Toggle cursor visibility upon pressing ESC
		if( this.input.getLatestInput().isKeyPressed(GLFW.GLFW_KEY_ESCAPE) ) {
			boolean isCursorDisabled = (
				GLFW.glfwGetInputMode(this.windowHandle, GLFW.GLFW_CURSOR) == 
				GLFW.GLFW_CURSOR_DISABLED
			);
			
			int inputMode = (
				isCursorDisabled ? GLFW.GLFW_CURSOR_NORMAL : GLFW.GLFW_CURSOR_DISABLED
			);
			
			GLFW.glfwSetInputMode(
	            this.windowHandle, 
	            GLFW.GLFW_CURSOR, 
	            inputMode
	        );
		}
		
			// Polling events may cause the window to be marked as "closing"
			// Buffers of destroyed windows don't need to be swapped
		if( GLFW.glfwWindowShouldClose(this.windowHandle) ) {
			this.destroy();
			return;
		}
		
		GLFW.glfwSwapBuffers(this.windowHandle);
		this.renderer.render();
		this.fpsCounter++;
	}
	
	public void pollInput() {
		this.input.updateFrontSnapshot();
		this.latestInputSnapshot = this.input.getLatestInput();
	}
	
	public void destroy() {
		this.isDestroyed = true;
		GLFW.glfwDestroyWindow(this.windowHandle);
		GLFW.glfwTerminate();
	}
	
	private void windowResizeListener(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public void setRenderer(IRenderer renderer) {
		this.renderer = renderer;
	}
	
	public void setInput(Input input) {
		this.input = input;
	}
	
	public boolean isDestroyed() {
		return this.isDestroyed;
	}
	
	public long getHandle() {
		return this.windowHandle;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public InputSnapshot getInputSnapshot() {
		return this.latestInputSnapshot;
	}
	
	public Input getInput() {
		return this.input;
	}
	
	public int getFPS() {
		return this.fps;
	}
	
	public int getMaxFPS() {
		return this.fpsMax;
	}
}

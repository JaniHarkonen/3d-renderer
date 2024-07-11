package project;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryUtil;

import project.input.Input;
import project.input.InputSnapshot;
import project.opengl.Renderer;
import project.utils.DebugUtils;

public class Window {
	private long windowHandle;
	private boolean isDestroyed;
	
	private String title;
	private int width;
	private int height;
	private int fpsMax;
	private int vsync;
	
	private int fpsCounter;
	private long fpsTimer;
	
	private long frameDelta;
	private long frameTimer;
	
	private Renderer renderer;
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
		
		this.fpsTimer = 0;
		this.fpsCounter = 0;
		
		this.frameDelta = 1000000000 / this.fpsMax;
		this.frameTimer = 0;
		
		this.renderer = null;
		this.input = null;
		this.latestInputSnapshot = null;
	}
	
	public void init() {
		GLFW.glfwInit();
		
			// Create the window
		this.windowHandle = GLFW.glfwCreateWindow(
			this.width, this.height, this.title, MemoryUtil.NULL, MemoryUtil.NULL
		);
		
		if( this.windowHandle == MemoryUtil.NULL ) {
			DebugUtils.log(this, "Unable to create window!");
			return;
		}
		
			// Center the window
		GLFWVidMode videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		GLFW.glfwSetWindowPos(
			this.windowHandle, 
			videoMode.width() / 2 - this.width / 2, 
			videoMode.height() / 2 - this.height / 2
		);
		
			// Bind input listener
		this.input = new Input();
		this.input.bind(this);
		
		GLFW.glfwMakeContextCurrent(this.windowHandle);
		GLFW.glfwSwapInterval(this.vsync); // v-sync
		this.isDestroyed = false;
		
		this.renderer.init();
	}
	
	public void refresh() {
		this.input.updateBackSnapshot();
		
			// FPS-counter
		if( System.nanoTime() - this.fpsTimer >= 1000000000 ) {
			GLFW.glfwSetWindowTitle(
				this.windowHandle, this.title + " | FPS: " + this.fpsCounter + " / " + this.fpsMax
			);
			
			this.fpsTimer = System.nanoTime();
			this.fpsCounter = 0;
		}
		
			// Only refresh if the frame timer allows it
			// This will cap the FPS to the given FPS max
		if( System.nanoTime() - this.frameTimer < this.frameDelta ) {
			return;
		}
		
		this.frameTimer = System.nanoTime();
		
			// Polling events may cause the window to be marked as "closing"
			// Buffers don't need to be swapped on destroyed windows
		if( this.input.getSnapshot().isKeyHeld(GLFW.GLFW_KEY_ESCAPE) ) {
			this.destroy();
			return;
		}
		
		GLFW.glfwSwapBuffers(this.windowHandle);
		this.renderer.render();
		this.fpsCounter++;
	}
	
	public void pollInput() {
		this.input.updateFrontSnapshot();
		this.latestInputSnapshot = this.input.getSnapshot();
	}
	
	public void destroy() {
		this.isDestroyed = true;
		GLFW.glfwDestroyWindow(this.windowHandle);
		GLFW.glfwTerminate();
	}
	
	public void setRenderer(Renderer renderer) {
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
}

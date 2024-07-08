package project;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryUtil;

public class Window {
	private int width;
	private int height;
	private long windowID;
	private Renderer renderer;
	
	public Window(int width, int height) {
		this.windowID = MemoryUtil.NULL;
		this.width = width;
		this.height = height;
		this.renderer = null;
	}
	
	public void init() {
		GLFW.glfwInit();
		
		this.windowID = GLFW.glfwCreateWindow(this.width, this.height, "3D renderer", MemoryUtil.NULL, MemoryUtil.NULL);
		
		if( this.windowID == MemoryUtil.NULL ) {
			System.out.println("Unable to create window!");
			return;
		}
		
		GLFWVidMode videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		GLFW.glfwSetWindowPos(this.windowID, videoMode.width() / 2 - this.width / 2, videoMode.height() / 2 - this.height / 2);
		
		GLFW.glfwSetKeyCallback(this.windowID, (window, key, scancode, action, mods) -> {
			if( key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_PRESS ) {
				GLFW.glfwSetWindowShouldClose(this.windowID, true);
			}
		});
		
		GLFW.glfwMakeContextCurrent(this.windowID);
		GLFW.glfwSwapInterval(1); // v-sync
		this.renderer.init();
	}
	
	public void pollInputEvents() {
		GLFW.glfwPollEvents();
	}
	
	public void swapBuffers() {
		GLFW.glfwSwapBuffers(this.windowID);
		this.renderer.render();
	}
	
	public boolean closeIfNeeded() {
		if( GLFW.glfwWindowShouldClose(this.windowID) ) {
			GLFW.glfwDestroyWindow(this.windowID);
			GLFW.glfwTerminate();
			return true;
		}
		
		return false;
	}
	
	public void setRenderer(Renderer renderer) {
		this.renderer = renderer;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
}

package project;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryUtil;

public class Window {
	private long windowHandle;
	private String title;
	private int width;
	private int height;
	private Renderer renderer;
	
	public Window(String title, int width, int height) {
		this.windowHandle = MemoryUtil.NULL;
		this.title = title;
		this.width = width;
		this.height = height;
		this.renderer = null;
	}
	
	public void init() {
		GLFW.glfwInit();
		
		this.windowHandle = GLFW.glfwCreateWindow(this.width, this.height, this.title, MemoryUtil.NULL, MemoryUtil.NULL);
		
		if( this.windowHandle == MemoryUtil.NULL ) {
			System.out.println("Unable to create window!");
			return;
		}
		
		GLFWVidMode videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		GLFW.glfwSetWindowPos(this.windowHandle, videoMode.width() / 2 - this.width / 2, videoMode.height() / 2 - this.height / 2);
		
		GLFW.glfwSetKeyCallback(this.windowHandle, (window, key, scancode, action, mods) -> {
			if( key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_PRESS ) {
				GLFW.glfwSetWindowShouldClose(this.windowHandle, true);
			}
		});
		
		GLFW.glfwMakeContextCurrent(this.windowHandle);
		GLFW.glfwSwapInterval(1); // v-sync
		this.renderer.init();
	}
	
	public void pollInputEvents() {
		GLFW.glfwPollEvents();
	}
	
	public void swapBuffers() {
		GLFW.glfwSwapBuffers(this.windowHandle);
		this.renderer.render();
	}
	
	public boolean closeIfNeeded() {
		if( GLFW.glfwWindowShouldClose(this.windowHandle) ) {
			GLFW.glfwDestroyWindow(this.windowHandle);
			GLFW.glfwTerminate();
			return true;
		}
		
		return false;
	}
	
	public void setRenderer(Renderer renderer) {
		this.renderer = renderer;
	}
	
	public void setTitle(String title) {
		GLFW.glfwSetWindowTitle(this.windowHandle, title);
		this.title = title;
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
}

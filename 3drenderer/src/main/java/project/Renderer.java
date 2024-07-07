package project;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL46;

public class Renderer {

	public void init() {
		GL.createCapabilities();
		GL46.glClearColor(0.85f, 0.85f, 0.85f, 0.0f);
	}
	
	public void render() {
		GL46.glClear(GL46.GL_COLOR_BUFFER_BIT | GL46.GL_DEPTH_BUFFER_BIT);
	}
}

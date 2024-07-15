package project.opengl;

import project.shader.ShaderProgram;

public class SceneRenderPass {
	
	private ShaderProgram shaderProgram;
	private VAOCache vaoCache;
	private TextureCache textureCache;
	
	public SceneRenderPass() {
		this.shaderProgram = null;
		this.vaoCache = null;
		this.textureCache = null;
	}
	
	
	public void init() {
		this.shaderProgram = new ShaderProgram();
		this.vaoCache = new VAOCache();
		this.textureCache = new TextureCache();
	}

	public void render(Renderer renderer) {
		
	}
}

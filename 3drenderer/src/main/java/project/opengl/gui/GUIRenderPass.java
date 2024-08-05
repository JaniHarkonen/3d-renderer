package project.opengl.gui;


import org.lwjgl.opengl.GL46;

import project.asset.AssetUtils;
import project.asset.sceneasset.Mesh;
import project.core.renderer.IRenderPass;
import project.core.renderer.IRenderer;
import project.core.renderer.NullRenderStrategy;
import project.gui.AGUIElement;
import project.gui.Image;
import project.gui.Text;
import project.opengl.RenderStrategyManager;
import project.opengl.Renderer;
import project.opengl.shader.Shader;
import project.opengl.shader.ShaderProgram;
import project.scene.ASceneObject;
import project.scene.Scene;

public class GUIRenderPass implements IRenderPass {
	static final String U_PROJECTION = "uProjection";
	static final String U_DIFFUSE_SAMPLER= "uDiffuseSampler";
	static final String U_OBJECT_TRANSFORM = "uObjectTransform";
	static final String U_TEXT_COLOR = "uTextColor";

	Mesh imagePlane;
	ShaderProgram shaderProgram;
	float lineHeight;
	float baseLine;
	
	private RenderStrategyManager<GUIRenderPass> renderStrategyManager;
	
	public GUIRenderPass() {
		this.imagePlane = null;
		this.shaderProgram = new ShaderProgram();
		this.lineHeight = 22.0f;
		this.baseLine = 16.0f;
		
		this.renderStrategyManager = new RenderStrategyManager<>(new NullRenderStrategy<GUIRenderPass>())
		.addStrategy(Text.class, new RenderText())
		.addStrategy(Image.class, new RenderImage());
	}
	
	@Override
	public boolean init() {
		this.imagePlane = AssetUtils.createPlaneMesh("mesh-default-gui-plane", 0, 0, 16, 16, 0, 0, 1, 1);
		this.shaderProgram = new ShaderProgram();
		this.shaderProgram.declareUniform(U_PROJECTION);
		this.shaderProgram.declareUniform(U_DIFFUSE_SAMPLER);
		this.shaderProgram.declareUniform(U_OBJECT_TRANSFORM);
		this.shaderProgram.declareUniform(U_TEXT_COLOR);
		this.shaderProgram.addShader(
			new Shader("shaders/gui/gui.vert", GL46.GL_VERTEX_SHADER)
		);
		this.shaderProgram.addShader(
			new Shader("shaders/gui/gui.frag", GL46.GL_FRAGMENT_SHADER)
		);
		this.shaderProgram.init();
		
		return true;
	}

	@Override
	public void render(IRenderer renderer) {
		Scene scene = ((Renderer) renderer).getActiveScene();
		ShaderProgram activeShaderProgram = this.shaderProgram;
		
        activeShaderProgram.bind();
        activeShaderProgram.setInteger1Uniform(U_DIFFUSE_SAMPLER, 0);
        activeShaderProgram.setMatrix4fUniform(
			U_PROJECTION, 
			scene.getGUI().calculateAndGetProjection()
		);
		
		for( AGUIElement element : scene.getGUI().getElements() ) {
			this.recursiveRender(renderer, element);
		}
		
		activeShaderProgram.unbind();
	}
	
	private void recursiveRender(IRenderer renderer, ASceneObject object) {
		for( ASceneObject child : object.getChildren() ) {
			this.recursiveRender(renderer, child);
		}
		
		this.renderStrategyManager.getStrategy(object.getClass()).execute(renderer, this, object);
	}
}

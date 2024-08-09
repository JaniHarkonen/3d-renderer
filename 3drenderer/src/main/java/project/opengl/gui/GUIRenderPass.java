package project.opengl.gui;


import org.lwjgl.opengl.GL46;

import project.asset.AssetUtils;
import project.asset.sceneasset.Mesh;
import project.core.GameState;
import project.core.renderer.IRenderPass;
import project.core.renderer.IRenderer;
import project.core.renderer.NullRenderStrategy;
import project.core.renderer.RenderStrategyManager;
import project.gui.AGUIElement;
import project.gui.Image;
import project.gui.Text;
import project.opengl.shader.Shader;
import project.opengl.shader.ShaderProgram;
import project.opengl.shader.uniform.UAMatrix4f;
import project.opengl.shader.uniform.UInteger1;
import project.opengl.shader.uniform.UVector4f;
import project.scene.ASceneObject;
import project.scene.Scene;

public class GUIRenderPass implements IRenderPass {
	Mesh imagePlane;
	ShaderProgram shaderProgram;
	float lineHeight;
	float baseLine;
	
	private GameState gameState;
	private UAMatrix4f uProjection;
	private UInteger1 uDiffuseSampler;
	private RenderStrategyManager<GUIRenderPass> renderStrategyManager;
	
	public GUIRenderPass() {
		this.gameState = null;
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
		this.uProjection = new UAMatrix4f("uProjection");
		this.uDiffuseSampler = new UInteger1("uDiffuseSampler");
		
		this.shaderProgram
		.declareUniform(this.uProjection)
		.declareUniform(this.uDiffuseSampler)
		.declareUniform(new UAMatrix4f("uObjectTransform"))
		.declareUniform(new UVector4f("uTextColor"));
		
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
	public void render(IRenderer renderer, GameState gameState) {
		this.gameState = gameState;
		Scene scene = this.gameState.DEBUGgetActiveScene();
		ShaderProgram activeShaderProgram = this.shaderProgram;
		
        activeShaderProgram.bind();
        this.uDiffuseSampler.update(0);
        this.uProjection.update(scene.getGUI().calculateAndGetProjection());
		
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
	
	@Override
	public GameState getGameState() {
		return this.gameState;
	}
}

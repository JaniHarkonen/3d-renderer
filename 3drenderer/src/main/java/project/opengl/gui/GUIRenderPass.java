package project.opengl.gui;


import org.joml.Matrix4f;
import org.lwjgl.opengl.GL46;

import project.Window;
import project.asset.AssetUtils;
import project.asset.sceneasset.Mesh;
import project.core.GameState;
import project.core.renderer.IRenderPass;
import project.core.renderer.IRenderer;
import project.core.renderer.NullRenderStrategy;
import project.core.renderer.RenderStrategyManager;
import project.opengl.shader.Shader;
import project.opengl.shader.ShaderProgram;
import project.opengl.shader.uniform.UAMatrix4f;
import project.opengl.shader.uniform.UInteger1;
import project.opengl.shader.uniform.UVector4f;
import project.ui.AUIElement;
import project.ui.Div;
import project.ui.Image;
import project.ui.Text;

public class GUIRenderPass implements IRenderPass {
		// Shared context
	Mesh imagePlane;
	ShaderProgram shaderProgram;
	
		// Uniforms
	UVector4f uPrimaryColor;
	UAMatrix4f uObjectTransform;
	private UAMatrix4f uProjection;
	private UInteger1 uDiffuseSampler;
	UInteger1 uHasTexture;
	
	private final Matrix4f projectionMatrix;
	private GameState gameState;
	private RenderStrategyManager<GUIRenderPass, AUIElement> renderStrategyManager;
	
	public GUIRenderPass() {
		this.projectionMatrix = new Matrix4f();
		this.gameState = null;
		this.imagePlane = null;
		this.shaderProgram = new ShaderProgram();
		
		this.renderStrategyManager = 
			new RenderStrategyManager<>(new NullRenderStrategy<GUIRenderPass, AUIElement>());
		this.renderStrategyManager
		.addStrategy(Div.class, new RenderDiv())
		.addStrategy(Text.class, new RenderText())
		.addStrategy(Image.class, new RenderImage());
	}
	
	
	@Override
	public boolean initialize() {
		this.imagePlane = 
			AssetUtils.createPlaneMesh("mesh-default-gui-plane", 0, 0, 1, 1, 0, 0, 1, 1);
		this.shaderProgram = new ShaderProgram();
		this.uProjection = new UAMatrix4f(Uniforms.PROJECTION);
		this.uDiffuseSampler = new UInteger1(Uniforms.DIFFUSE_SAMPLER);
		this.uPrimaryColor = new UVector4f(Uniforms.PRIMARY_COLOR);
		this.uObjectTransform = new UAMatrix4f(Uniforms.OBJECT_TRANSFORM);
		this.uHasTexture = new UInteger1(Uniforms.HAS_TEXTURE);
		
		this.shaderProgram
		.declareUniform(this.uProjection)
		.declareUniform(this.uDiffuseSampler)
		.declareUniform(this.uObjectTransform)
		.declareUniform(this.uPrimaryColor)
		.declareUniform(this.uHasTexture);
		
		this.shaderProgram.addShader(
			new Shader("shaders/gui/gui.vert", GL46.GL_VERTEX_SHADER)
		);
		this.shaderProgram.addShader(
			new Shader("shaders/gui/gui.frag", GL46.GL_FRAGMENT_SHADER)
		);
		this.shaderProgram.initialize();
		
		return true;
	}

	@Override
	public void render(IRenderer renderer, GameState gameState) {
		this.gameState = gameState;
		ShaderProgram activeShaderProgram = this.shaderProgram;
		
        activeShaderProgram.bind();
        this.uDiffuseSampler.update(0);
        
        	// Calculate and update projection
        Window window = renderer.getClientWindow();
		this.projectionMatrix.identity().setOrtho2D(0, window.getWidth(), window.getHeight(), 0);
        this.uProjection.update(this.projectionMatrix);
        
        this.recursivelyRender(renderer, gameState.getActiveGUIRoot());
		activeShaderProgram.unbind();
	}
	
	private void recursivelyRender(IRenderer renderer, AUIElement element) {
		this.renderStrategyManager.getStrategy(element.getClass()).execute(renderer, this, element);
		
		if( element.hasText() ) {
			Text text = element.getText();
			this.renderStrategyManager.getStrategy(text.getClass()).execute(renderer, this, text);
		}
		
		for( AUIElement child : element.getChildren() ) {
			this.recursivelyRender(renderer, child);
		}
	}
	
	@Override
	public GameState getGameState() {
		return this.gameState;
	}
}

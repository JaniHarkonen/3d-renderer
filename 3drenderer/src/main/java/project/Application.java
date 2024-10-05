package project;

import project.core.Networker;
import project.core.asset.AssetManager;
import project.core.renderer.IRenderer;
import project.gui.GUI;
import project.gui.jeemu.DocumentParser;
import project.gui.jeemu.Token;
import project.gui.jeemu.Tokenizer;
import project.opengl.RendererGL;
import project.scene.Scene;
import project.shared.ConsoleRecorder;
import project.shared.logger.Logger;
import project.testing.TestAssets;
import project.utils.DebugUtils;

public class Application {
	
	private static Application APPLICATION;
	
	public static void main(String[] args) {
		if( APPLICATION != null )
		return;
		
		APPLICATION = new Application();
		APPLICATION.execute();
	}
	
	
	private AssetManager assetManager;
	private Networker networker;
	private Window window;
	private IRenderer renderer;
	private Scene scene;
	
	public Application() {
		this.assetManager = null;
		this.networker = null;
		this.window = null;
		this.renderer = null;
		this.scene = null;
		Logger.configure(
			//Logger.LOG_TIMESTAMP | 
			//Logger.LOG_SYSTEM | 
			Logger.LOG_CALLER | 
			Logger.LOG_SEVERITY, 
			Logger.INFO
		);
		ConsoleRecorder consoleRecoder = new ConsoleRecorder();
		Logger.get().registerTarget(consoleRecoder);
		Logger.get().info(this, "Logger has been configured!");
	}
	
	
	public void execute() {
		final String TITLE = "3D Renderer - JOHNEngine";
		final int FPS_MAX = 60;
		final int TICK_RATE = 60;
		
		Tokenizer tokenizer = new Tokenizer();
		/*Tokenizer.Result result = tokenizer.tokenize("      // Declares a custom div called 'custom'\r\n" + 
				"    custom collection as div {\r\n" + 
				"      width: 100px;\r\n" + 
				"      height: 100px;\r\n" + 
				"      backgroundColor: #FFFFFF;\r\n" + 
				"    }\r\n" + 
				"\r\n" + 
				"    body {\r\n" + 
				"      div {\r\n" + 
				"        width: 50%;\r\n" + 
				"        height: 50%;\r\n" + 
				"\r\n" + 
				"          // Use the 'custom' collection\r\n" + 
				"        custom {\r\n" + 
				"          width: 200px; // Sets the width overriding the pre-defined value\r\n" + 
				"          primaryColor: #000000;\r\n" + 
				"\r\n" + 
				"            // Adds a text child into the div\r\n" + 
				"          text {\r\n" + 
				"            Hello world!\r\n" + 
				"          }\r\n" + 
				"        }\r\n" + 
				"      }\r\n" + 
				"    }");*/
		Tokenizer.Result result = tokenizer.tokenize(
			"casual theme {someProp:'this is a testprop';}\n"+
			"box collection as image {\n"
			+ "width:56px;}\n"+
			 "body{\n"
			+ "div {\n"
			+ "ID: 'testing';\n"
			+ "width: 50%;\n"
			+ "height:100px;\n"
			+ "div{ID:'another-test'; minWidth: 6r;}\n"
			+ "} box {ID:'yet-another-div';}\n"
			+ "}"
		);
		DebugUtils.log(this, result.errorMessage);
		String tokenString = "";
		for( Token token : result.tokens ) {
			tokenString += token.type + "\n";
		}
		DebugUtils.log(this, tokenString);
		DocumentParser parser = new DocumentParser();
		GUI test = new GUI();
		DocumentParser.Result parse = parser.parse(test, result.tokens);
		DebugUtils.log(this, parse.errorMessage);
		DebugUtils.log(this, test.getElementByID("yet-another-div"));
		//DebugUtils.log(this, test.getElementByID("another-test").getProperties().getProperty(Properties.MIN_WIDTH).getValue());
		//DebugUtils.log(this, test.getElementByID("yet-another-test"));
		
		//DebugUtils.log(this, test.getTheme("casual").getPropertyBuilder("someProp"));
		DebugUtils.log(this, test.getTheme("casual").getPropertyBuilder("someProp").value);
		
			// Asset manager
		this.assetManager = new AssetManager();
		
			// Networker
		//NetworkStandard networkStandard = new NetworkStandard();
		//networkStandard.declare();
		//this.networker = new Networker(networkStandard);
		//this.networker.launchSession("localhost", 12345);
		
			// Window and renderer
		Window window = new Window(TITLE, 800, 600, FPS_MAX, 0);
		this.window = window;
			this.renderer = new RendererGL(window);
			window.setRenderer(this.renderer);
		window.initialize();
		
			// Scene and assets
		TestAssets.initialize();
		this.scene = new Scene(this, TICK_RATE);
		this.scene.initialize();
		
			// Game loop
		while( !window.isDestroyed() ) {
			window.refresh();
			//this.networker.handleInboundMessages();
			this.scene.update();
			//this.networker.handleOutboundMessages();
		}
		
		//this.networker.abortSession();
		//DebugUtils.log(this, "main loop terminated!");
		Logger.get().info(this, "Main loop terminated!");
	}
	
	public static Application getApp() {
		return APPLICATION;
	}
	
	public AssetManager getAssetManager() {
		return this.assetManager;
	}
	
	public Window getWindow() {
		return this.window;
	}
	
	public IRenderer getRenderer() {
		return this.renderer;
	}
	
	public Networker getNetworker() {
		return this.networker;
	}
	
	public Scene getScene() {
		return this.scene;
	}
}

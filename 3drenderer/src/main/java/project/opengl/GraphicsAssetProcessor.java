package project.opengl;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import project.asset.IGraphicsAsset;
import project.asset.Mesh;

public class GraphicsAssetProcessor {

	/*private Renderer renderer;
	private Queue<IGraphicsAsset> generationQueue;
	private Queue<IGraphicsGL> disposalQueue;
	
	public GraphicsAssetProcessor(Renderer renderer) {
		this.renderer = renderer;
		this.generationQueue = new ConcurrentLinkedQueue<>();
		this.disposalQueue = new ConcurrentLinkedQueue<>();
	}
	
	
	public void processRequests() {
		IGraphicsAsset graphicsAsset;
		
		while( (graphicsAsset = this.generationQueue.poll()) != null ) {
			if( graphicsAsset instanceof Mesh ) {
				Mesh mesh = (Mesh) graphicsAsset;
				VAO vao = new VAO(mesh, this.renderer);
				vao.generate();
				mesh.setGraphics(vao);
			} else if( graphicsAsset instanceof Texture ) {
				Texture texture = (Texture) graphicsAsset;
				TextureGL gl = new TextureGL(texture, this.renderer);
				gl.generate();
				texture.setGraphics(gl);
			}
		}
		
		IGraphicsGL graphicsGL;
		while( (graphicsGL = this.disposalQueue.poll()) != null ) {
			graphicsGL.disposeGL();
		}
	}
	
	public void requestGeneration(IGraphicsAsset graphicsAsset) {
		this.generationQueue.add(graphicsAsset);
	}
	
	public void requestDisposal(IGraphicsGL graphicsGL) {
		this.disposalQueue.add(graphicsGL);
	}*/
}

package project.ui;

import project.asset.texture.Texture;

public class Image extends AUIElement {
	private Texture imageTexture;
	
	public Image(UI ui, String id, Texture imageTexture) {
		super(ui, id);
		this.imageTexture = imageTexture;
	}
	
	private Image(Image src) {
		super(src);
		
		this.imageTexture = src.imageTexture;
	}
	
	
	@Override
	public Image rendererCopy() {
		return new Image(this);
	}
	
	@Override
	public boolean rendererEquals(AUIElement previous) {
		if( !(previous instanceof Image) ) {
			return false;
		}
		
		Image img = (Image) previous;
		return (
			this.id.equals(img.id) && 
			this.imageTexture == img.imageTexture && 
			this.statistics.equals(img.getStatistics())
		);
	}
	
	@Override
	public Image createInstance(UI ui, String id) {
		return new Image(ui, id, null);
	}
	
	public Texture getTexture() {
		return this.imageTexture;
	}
}

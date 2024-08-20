package project.gui;

import org.joml.Vector2f;
import org.joml.Vector4f;

import project.asset.texture.Texture;
import project.scene.ASceneObject;

public class Image extends AGUIElement {
	private Texture imageTexture;
	private Vector2f anchor;
	
	public Image(GUI gui, Texture imageTexture) {
		super(gui);
		this.imageTexture = imageTexture;
		this.primaryColor = new Vector4f(1.0f, 1.0f, 1.0f, 0.85f);
		this.anchor = new Vector2f(0.0f, 0.0f);
	}
	
	private Image(Image src) {
		super(src);
		
		this.imageTexture = src.imageTexture;
		this.anchor = new Vector2f(src.anchor);
	}
	
	
	@Override
	public Image rendererCopy() {
		return new Image(this);
	}
	
	@Override
	public boolean rendererEquals(ASceneObject previous) {
		if( !(previous instanceof Image) ) {
			return false;
		}
		
		Image img = (Image) previous;
		return(
			this.id == img.id && 
			this.transform.equals(img.transform) && 
			this.primaryColor.equals(img.primaryColor) && 
			this.secondaryColor.equals(img.secondaryColor) && 
			this.imageTexture == img.imageTexture && 
			this.anchor.equals(img.anchor)
		);
	}
	
	public void setAnchor(float anchorX, float anchorY) {
		this.anchor.set(anchorX, anchorY);
	}
	
	public Texture getTexture() {
		return this.imageTexture;
	}
	
	public Vector2f getAnchor() {
		return this.anchor;
	}
}

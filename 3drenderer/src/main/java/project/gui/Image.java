package project.gui;

import org.joml.Vector2f;
import org.joml.Vector4f;

import project.asset.Texture;

public class Image extends AGUIElement {
	private Texture imageTexture;
	private Vector4f primaryColor;
	private Vector2f anchor;
	
	public Image(GUI gui, Texture imageTexture) {
		super(gui);
		this.imageTexture = imageTexture;
		this.primaryColor = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
		this.anchor = new Vector2f(0.0f, 0.0f);
	}
	
	private Image(Image src) {
		super(null);
		this.imageTexture = src.imageTexture;
		this.primaryColor = new Vector4f(src.primaryColor);
		this.anchor = new Vector2f(src.anchor);
	}
	
	
	@Override
	protected Image rendererCopy() {
		return new Image(this);
	}
	
	public void setAnchor(float anchorX, float anchorY) {
		this.anchor.set(anchorX, anchorY);
	}
	
	public Vector4f getPrimaryColor() {
		return this.primaryColor;
	}
	
	public Texture getTexture() {
		return this.imageTexture;
	}
	
	public Vector2f getAnchor() {
		return this.anchor;
	}
}

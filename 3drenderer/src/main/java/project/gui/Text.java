package project.gui;

import org.joml.Vector4f;

import project.asset.Font;
import project.testing.TestAssets;

public class Text extends AGUIElement {
	private Font font;
	private String content;
	private Vector4f textColor;
	
	public Text(GUI gui, String content) {
		super(gui);
		this.font = TestAssets.FONT_ARIAL_20;
		this.content = content;
		this.textColor = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
	}
	
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getContent() {
		return this.content;
	}
	
	public Font getFont() {
		return this.font;
	}
	
	public Vector4f getTextColor() {
		return this.textColor;
	}
}

package project.gui;

import org.joml.Vector4f;

import project.asset.font.Font;
import project.testing.TestAssets;

public class Text extends AGUIElement {
	private Font font;
	private String content;
	
	public Text(GUI gui, String content) {
		super(gui);
		this.font = TestAssets.FONT_ARIAL_16;
		this.content = content;
		this.primaryColor = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
	}
	
	private Text(Text src) {
		super(null);
		this.font = src.font;
		this.content = new String(src.content);
	}
	
	
	@Override
	protected Text rendererCopy() {
		return new Text(this);
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
}

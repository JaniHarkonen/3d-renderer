package project.gui;

import project.asset.font.Font;
import project.testing.TestAssets;

public class Text extends AGUIElement {
	private Font font;
	private String content;
	
	public Text(GUI gui, String id, String content) {
		super(gui, id);
		this.font = TestAssets.FONT_ARIAL_16;
		this.content = content;
		//this.primaryColor = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
	}
	
	private Text(Text src) {
		super(src);
		
		this.font = src.font;
		this.content = new String(src.content);
	}
	
	
	@Override
	public Text rendererCopy() {
		return new Text(this);
	}
	
	@Override
	public boolean rendererEquals(AGUIElement previous) {
		if( !(previous instanceof Text) ) {
			return false;
		}
		
		Text t = (Text) previous;
		return(
			this.id.equals(t.id) && 
			//this.transform.equals(t.transform) && 
			//this.primaryColor.equals(t.primaryColor) && 
			//this.secondaryColor.equals(t.secondaryColor) && 
			this.font == t.font &&
			this.properties.equals(t.getProperties()) && 
			this.content.equals(t.content)
		);
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

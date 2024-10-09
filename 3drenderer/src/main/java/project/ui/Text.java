package project.ui;

import project.asset.font.Font;
import project.gui.props.Properties;
import project.gui.props.Property;
import project.testing.TestAssets;

public class Text extends AUIElement {
	private Font font;
	private String content;
	
	public Text(GUI gui, String content) {
		super(gui, null);
		this.font = TestAssets.FONT_ARIAL_16;
		this.content = content;
		this.properties.setProperty(
			Properties.WIDTH, new Property(Properties.WIDTH, 1, Property.WPERCENT)
		);
		this.properties.setProperty(
			Properties.HEIGHT, new Property(Properties.HEIGHT, 1, Property.HPERCENT)
		);
	}
	
	public Text(Text src) {
		super(src);
		
		this.font = src.font;
		this.content = new String(src.content);
	}
	
	
	@Override
	public Text rendererCopy() {
		return new Text(this);
	}
	
	@Override
	public boolean rendererEquals(AUIElement previous) {
		if( !(previous instanceof Text) ) {
			return false;
		}
		
		Text t = (Text) previous;
		return(
			this.id.equals(t.id) && 
			this.font == t.font &&
			this.statistics.equals(t.getStatistics()) && 
			this.content.equals(t.content)
		);
	}
	
	@Override
	public Text createInstance(GUI ui, String id) {
		return new Text(ui, "");
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

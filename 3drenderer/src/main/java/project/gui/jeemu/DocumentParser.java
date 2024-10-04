package project.gui.jeemu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.gui.AGUIElement;
import project.gui.Body;
import project.gui.Div;
import project.gui.GUI;
import project.gui.Image;
import project.gui.Text;
import project.gui.Theme;

public class DocumentParser {
	public class Result {
		
	}

	private GUI targetUI;
	private List<Token> tokens;
	private int cursor;
	private Map<String, AGUIElement> collections;
	
	public DocumentParser() {
		this.targetUI = null;
		this.tokens = null;
		this.cursor = 0;
		this.collections = new HashMap<>();
	}
	
	
	public void parse(GUI targetUI, List<Token> tokens) {
		this.targetUI = targetUI;
		this.tokens = tokens;
		this.cursor = 0;
		this.collections = new HashMap<>();
		this.document();
	}
	
	private void document() {
		Theme theme;
		while( (theme = this.theme()) != null ) {
			this.targetUI.addTheme(theme.getID(), theme);
		}
		
		this.extractCollections();
		this.targetUI.setBody(this.body());
	}
	
	private Theme theme() {
		
	}
	
	private void extractCollections() {
		
	}
	
	private AGUIElement collection() {
		
	}
	
	private void addCollection(String name, AGUIElement collection) {
		this.collections.put(name, collection);
	}
	
	private Body body() {
		
	}
	
	private Div div() {
		
	}
	
	private Image image() {
		
	}
	
	private Text text() {
		
	}
}

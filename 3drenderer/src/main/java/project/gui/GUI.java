package project.gui;

import java.util.ArrayList;
import java.util.List;

public class GUI {
	private List<AGUIElement> elements;
	
	public GUI() {
		this.elements = null;
	}
	
	
	public void initialize() {
		this.elements = new ArrayList<>();
	}
	
	public void addElement(AGUIElement element) {
		this.elements.add(element);
	}
	
	
	public List<AGUIElement> getElements() {
		return this.elements;
	}
}

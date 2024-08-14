package project.gui;

import java.util.ArrayList;
import java.util.List;

import project.scene.Scene;

public class GUI {
	private List<AGUIElement> elements;
	
	public GUI(Scene scene) {
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

package project.gui;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;

import project.scene.Scene;

public class GUI {
	private List<AGUIElement> elements;
	private Matrix4f projectionMatrix;
	private Scene scene;
	
	public GUI(Scene scene) {
		this.elements = null;
		this.projectionMatrix = new Matrix4f();
		this.scene = scene;
	}
	
	
	public void init() {
		this.elements = new ArrayList<>();
	}
	
	public void addElement(AGUIElement element) {
		this.elements.add(element);
	}
	
	public Matrix4f calculateAndGetProjection() {
		float windowCenterX = this.scene.getApp().getWindow().getWidth() / 2;
		float windowCenterY = this.scene.getApp().getWindow().getHeight() / 2;
		this.projectionMatrix = new Matrix4f();
		this.projectionMatrix
		.identity()
		.setOrtho2D(0, windowCenterX * 2, windowCenterY * 2, 0);
		
		return this.projectionMatrix;
	}
	
	public List<AGUIElement> getElements() {
		return this.elements;
	}
}

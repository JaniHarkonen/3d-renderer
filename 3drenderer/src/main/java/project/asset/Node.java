package project.asset;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;

public class Node {
	private String nodeName;
	private Node parent;
	private Matrix4f nodeTransform;
	private List<Node> children;
	
	public Node(String nodeName, Node parent, Matrix4f nodeTransform) {
		this.nodeName = nodeName;
		this.parent = parent;
		this.nodeTransform = nodeTransform;
		this.children = new ArrayList<>();
	}
	
	
	public void addChild(Node child) {
		this.children.add(child);
	}
	
	public String getName() {
		return this.nodeName;
	}
	
	public Node getParentNode() {
		return this.parent;
	}
	
	public Matrix4f getNodeTransform() {
		return this.nodeTransform;
	}
	
	public List<Node> getChildren() {
		return this.children;
	}
}

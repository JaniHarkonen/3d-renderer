package project.opengl.vao;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL46;

import project.Application;
import project.asset.sceneasset.Mesh;
import project.core.asset.IGraphics;
import project.core.asset.IGraphicsAsset;
import project.utils.GeometryUtils;

public class VAO implements IGraphics {

	private int vaoHandle;
	private List<VBO> vbos;
	private Mesh targetMesh;
	private int vertexCount;
	
	public VAO(Mesh targetMesh) {
		this.vaoHandle = -1;
		this.vbos = new ArrayList<>();
		this.targetMesh = targetMesh;
		this.vertexCount = -1;
	}
	
	private VAO(VAO src) {
		this.vaoHandle = src.vaoHandle;
		this.vbos = new ArrayList<>(src.vbos);
		this.targetMesh = src.targetMesh;
		this.vertexCount = src.vertexCount;
	}
	
	
	public VBO addVBO(VBO vbo) {
		vbo.setAttributeArrayIndex(this.vbos.size());
		this.vbos.add(vbo);
		return vbo;
	}
	
	@Override
	public boolean generate() {
		this.vaoHandle = GL46.glGenVertexArrays();
		
		Mesh mesh = this.targetMesh;
		VBO indicesVBO = new VBO(0, -1, GL46.GL_ELEMENT_ARRAY_BUFFER, GL46.GL_STATIC_DRAW) {
			@Override
			public void attach() {} // Index VBO doesn't have to be attached
		};
		
		this
		.addVBO(new VBO(3))
		.generateAndAttach(this, GeometryUtils.vector3fArrayToFloatArray(mesh.getVertices()))
		.addVBO(new VBO(3))
		.generateAndAttach(this, GeometryUtils.vector3fArrayToFloatArray(mesh.getNormals()))
		.addVBO(new VBO(3))
		.generateAndAttach(this, GeometryUtils.vector3fArrayToFloatArray(mesh.getTangents()))
		.addVBO(new VBO(3))
		.generateAndAttach(this, GeometryUtils.vector3fArrayToFloatArray(mesh.getBitangents()))
		.addVBO(new VBO(2))
		.generateAndAttach(this, GeometryUtils.vector2fArrayToFloatArray(mesh.getUVs()))
		.addVBO(new VBO(4))
		.generateAndAttach(this, mesh.getBoneWeights())
		.addVBO(new VBO(4))
		.generateAndAttach(this, mesh.getBoneIDs())
		.addVBO(indicesVBO)
		.generateAndAttach(this, GeometryUtils.faceArrayToIntArray(mesh.getFaces()));
		
		this.unbind();
		this.vertexCount = mesh.getVertexCount();
		this.targetMesh.setGraphics(this);
		
		return true;
	}
	
	@Override
	public boolean regenerate() {
		if( !this.isNull() ) {
			this.dispose();
		}
		
		return new VAO(this.targetMesh).generate();
	}
	
	@Override
	public boolean dispose() {
		for( VBO vbo : this.vbos ) {
			vbo.dispose();
		}
		
		GL46.glDeleteVertexArrays(this.vaoHandle);
		return true;
	}
	
	public void bind() {
		GL46.glBindVertexArray(this.vaoHandle);
	}
	
	public void unbind() {
		GL46.glBindVertexArray(0);
	}

	@Override
	public VAO createReference(IGraphicsAsset graphicsAsset) {
		VAO reference = new VAO(this);
		reference.targetMesh = (Mesh) graphicsAsset;
		graphicsAsset.setGraphics(reference);
		return reference;
	}

	@Override
	public void dropGraphicsAsset() {
		this.targetMesh = null;
	}
	
	public int getVertexCount() {
		return this.vertexCount;
	}
	
	public int getHandle() {
		return this.vaoHandle;
	}
	
	@Override
	public IGraphicsAsset getGraphicsAsset() {
		return this.targetMesh;
	}
	
	@Override
	public boolean isGenerated() {
		return (this.vaoHandle >= 0);
	}
	
	@Override
	public boolean isNull() {
		VAO defaultVAO = (VAO) Application.getApp().getRenderer().getDefaultMeshGraphics();
		return (this.vaoHandle == defaultVAO.vaoHandle);
	}
}

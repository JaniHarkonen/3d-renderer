package project.component;

import org.joml.Vector4f;

import project.asset.texture.Texture;

public class Material {
	
	public static final int DEFAULT_TEXTURE_SLOT_COUNT = 2;
	public static final Vector4f DEFAULT_AMBIENT_COLOR = 
		new Vector4f(0.0f, 0.0f, 0.0f, 1.0f); // pulled from fbx via Assimp
	public static final Vector4f DEFAULT_DIFFUSE_COLOR = 
		new Vector4f(0.8f, 0.8f, 0.8f, 1.0f); // pulled from fbx via Assimp
	public static final Vector4f DEFAULT_SPECULAR_COLOR = 
		new Vector4f(1.0f, 1.0f, 1.0f, 1.0f); // pulled from fbx via Assimp

	
	private final String name;
	private Texture[] textureSlot;
	private Vector4f ambientColor;
	private Vector4f diffuseColor;
	private Vector4f specularColor;
	private float reflectance;
	
	public Material(String name) {
		this.name = name;
		this.textureSlot = new Texture[Material.DEFAULT_TEXTURE_SLOT_COUNT];
		this.ambientColor = Material.DEFAULT_AMBIENT_COLOR;
		this.diffuseColor = Material.DEFAULT_DIFFUSE_COLOR;
		this.specularColor = Material.DEFAULT_SPECULAR_COLOR;
		this.reflectance = 0.0f;
	}
	
	
	public void setTexture(int textureSlot, Texture texture) {
		this.textureSlot[textureSlot] = texture;
	}
	
	public void setAmbientColor(Vector4f ambientColor) {
		this.ambientColor = ambientColor;
	}
	
	public void setDiffuseColor(Vector4f diffuseColor) {
		this.diffuseColor = diffuseColor;
	}
	
	public void setSpecularColor(Vector4f specularColor) {
		this.specularColor = specularColor;
	}
	
	public void setReflectance(float reflectance) {
		this.reflectance = reflectance;
	}
	
	public Texture[] getTextures() {
		return this.textureSlot;
	}
	
	public Texture getTexture(int textureSlot) {
		return this.textureSlot[textureSlot];
	}
	
	public Vector4f getAmbientColor() {
		return this.ambientColor;
	}
	
	public Vector4f getDiffuseColor() {
		return this.diffuseColor;
	}
	
	public Vector4f getSpecularColor() {
		return this.specularColor;
	}
	
	public float getReflectance() {
		return this.reflectance;
	}
	
	public String getName() {
		return this.name;
	}
}

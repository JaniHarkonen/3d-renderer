package project.opengl.shader.custom.material;

import org.joml.Vector4f;

import project.opengl.shader.test.IShaderStruct;

public class SSMaterial implements IShaderStruct {

	public int hasNormalMap;
	public Vector4f ambient;
	public Vector4f diffuse;
	public Vector4f specular;
	public float reflectance;
}

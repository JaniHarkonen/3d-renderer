package project.opengl.shader.custom.amlight;

import org.joml.Vector3f;

import project.opengl.shader.uniform.IShaderStruct;

public class SSAmbientLight implements IShaderStruct {

	public float factor;
	public Vector3f color;
}

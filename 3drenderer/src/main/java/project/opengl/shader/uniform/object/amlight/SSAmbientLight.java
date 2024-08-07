package project.opengl.shader.uniform.object.amlight;

import org.joml.Vector3f;

import project.opengl.shader.uniform.IShaderStruct;

public class SSAmbientLight implements IShaderStruct {

	public float factor;
	public Vector3f color;
}

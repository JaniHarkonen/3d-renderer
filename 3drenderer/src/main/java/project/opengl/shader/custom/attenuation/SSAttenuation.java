package project.opengl.shader.custom.attenuation;

import project.opengl.shader.uniform.IShaderStruct;

public class SSAttenuation implements IShaderStruct {

	public float constant;
	public float exponent;
	public float linear;
}

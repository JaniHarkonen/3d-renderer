package project.opengl.shader.custom.ptlight;

import org.joml.Vector3f;

import project.opengl.shader.custom.attenuation.SSAttenuation;
import project.opengl.shader.test.IShaderStruct;

public class SSPointLight implements IShaderStruct {

	public Vector3f position;
	public Vector3f color;
	public float intensity;
	public SSAttenuation att;
}

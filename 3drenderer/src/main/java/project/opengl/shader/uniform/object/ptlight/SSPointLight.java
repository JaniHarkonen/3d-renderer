package project.opengl.shader.uniform.object.ptlight;

import org.joml.Vector3f;

import project.opengl.shader.uniform.IShaderStruct;
import project.opengl.shader.uniform.object.attenuation.SSAttenuation;

public class SSPointLight implements IShaderStruct {

	public Vector3f position;
	public Vector3f color;
	public float intensity;
	public SSAttenuation attenuation;
}

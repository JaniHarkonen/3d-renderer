package project.opengl.shader.custom.cshadow;

import org.joml.Matrix4f;

import project.opengl.shader.uniform.IShaderStruct;

public class SSCascadeShadow implements IShaderStruct {

	public Matrix4f lightView;
    public float splitDistance;
}

package project.opengl.shader.test;

import java.util.ArrayList;
import java.util.List;

import project.opengl.shader.ShaderProgram;

public abstract class AUniformObject<T extends IShaderStruct> implements IUniform<T> {
	private class ObjectField {
		private String fieldName;
		private IUniform<?> uniform;
		
		private ObjectField(String fieldName, IUniform<?> uniform) {
			this.fieldName = fieldName;
			this.uniform = uniform;
		}
	}
	
	private String name;
	private List<ObjectField> fields;
	
	public AUniformObject(String name) {
		this.name = name;
		this.fields = new ArrayList<>();
	}
	

	@Override
	public void initialize(ShaderProgram shaderProgram) {
		for( ObjectField field : this.fields ) {
			field.uniform.setName(this.name + "." + field.fieldName);
			field.uniform.initialize(shaderProgram);
		}
	}
	
	public AUniformObject<?> addField(String fieldName, IUniform<?> uniform) {
		this.fields.add(new ObjectField(fieldName, uniform));
		return this;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return this.name;
	}
}

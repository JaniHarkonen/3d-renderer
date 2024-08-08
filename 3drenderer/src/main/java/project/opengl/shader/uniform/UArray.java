package project.opengl.shader.uniform;

import java.util.function.Supplier;

import project.opengl.shader.ShaderProgram;
import project.utils.DebugUtils;

public class UArray<T> implements IUniform<T[]> {
	private String name;
	private IUniform<T>[] array;
	
	public UArray(String name, IUniform<T>[] array) {
		this.name = name;
		this.array = array;
	}
	
	public UArray(IUniform<T>[] array) {
		this("", array);
	}

	
	@Override
	public void initialize(ShaderProgram shaderProgram) {
		for( int i = 0; i < this.array.length; i++ ) {
			this.array[i].setName(this.name + "[" + i + "]");
			this.array[i].initialize(shaderProgram);
		}
	}

	@Override
	public void update(T[] values) {
		if( this.array.length != values.length) {
			DebugUtils.log(
				this, 
				"WARNING: Unable to set array uniform '" + this.name + "'!",
				"Uniform array of size " + this.array.length + 
				" is being set with an array of size " + values.length + "."
			);
			return;
		}
		
		for( int i = 0; i < values.length; i++ ) {
			this.array[i].update(values[i]);
		}
	}
	
	public void update(T value, int index) {
		this.array[index].update(value);
	}
	
	public void fill(Supplier<IUniform<T>> generator) {
		for( int i = 0; i < this.array.length; i++ ) {
			this.array[i] = generator.get();
		}
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

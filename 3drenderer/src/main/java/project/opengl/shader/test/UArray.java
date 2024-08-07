package project.opengl.shader.test;

import java.util.function.Supplier;

import project.opengl.shader.ShaderProgram;
import project.utils.DebugUtils;

public class UArray<T> implements IUniform<T[]> {
	
	private enum ArrayType {
		PRIMITIVE,
		OBJECT,
		ARRAY
	}
	
	private int location;
	private String name;
	private IUniform<T>[] array;
	private ArrayType arrayType;
	
	public UArray(int size, IUniform<T>[] array) {
		this.location = -1;
		this.name = "";
		this.array = array;
		
			// Cache array type as it will determine, how the UArray is initialized
		if( array instanceof AUniformObject[] ) {
			this.arrayType = ArrayType.OBJECT;
		} else if( array instanceof UArray[] ) {
			this.arrayType = ArrayType.ARRAY;
		} else {
			this.arrayType = ArrayType.PRIMITIVE;
		}
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
	
	public void fill(Supplier<IUniform<T>> generator) {
		for( int i = 0; i < this.array.length; i++ ) {
			this.array[i] = generator.get();
		}
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

}

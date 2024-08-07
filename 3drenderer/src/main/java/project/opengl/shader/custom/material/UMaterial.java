package project.opengl.shader.custom.material;

import project.opengl.shader.uniform.AUniformObject;
import project.opengl.shader.uniform.UFloat1;
import project.opengl.shader.uniform.UInteger1;
import project.opengl.shader.uniform.UVector4f;

public class UMaterial extends AUniformObject<SSMaterial> {

	private UInteger1 hasNormalMap;
	private UVector4f ambient;
	private UVector4f diffuse;
	private UVector4f specular;
	private UFloat1 reflectance;
	
	public UMaterial(String name) {
		super(name);
		
		this.hasNormalMap = new UInteger1();
		this.ambient = new UVector4f();
		this.diffuse = new UVector4f();
		this.specular = new UVector4f();
		this.reflectance = new UFloat1();
		
		this
		.addField("hasNormalMap", this.hasNormalMap)
		.addField("ambient", this.ambient)
		.addField("diffuse", this.diffuse)
		.addField("specular", this.specular)
		.addField("reflectance", this.reflectance);
	}
	
	public UMaterial() {
		this("");
	}
	
	
	@Override
	public void update(SSMaterial value) {
		this.hasNormalMap.update(value.hasNormalMap);
		this.ambient.update(value.ambient);
		this.diffuse.update(value.diffuse);
		this.specular.update(value.specular);
		this.reflectance.update(value.reflectance);
	}
}

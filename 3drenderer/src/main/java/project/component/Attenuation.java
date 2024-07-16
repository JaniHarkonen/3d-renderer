package project.component;

public class Attenuation {

	private float constant;
	private float exponent;
	private float linear;
	
	public Attenuation(float constant, float exponent, float linear) {
		this.constant = constant;
		this.exponent = exponent;
		this.linear = linear;
	}
	
	
	public void setConstant(float constant) {
		this.constant = constant;
	}
	
	public void setExponent(float exponent) {
		this.exponent = exponent;
	}
	
	public void setLinear(float linear) {
		this.linear = linear;
	}
	
	public float getConstant() {
		return this.constant;
	}
	
	public float getExponent() {
		return this.exponent;
	}
	
	public float getLinear() {
		return this.linear;
	}
}

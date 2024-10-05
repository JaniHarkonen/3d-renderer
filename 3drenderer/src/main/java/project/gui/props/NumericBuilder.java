package project.gui.props;

public class NumericBuilder extends PropertyBuilder {
	public NumericBuilder(Object value, String dataType) {
		super(value, dataType);
	}
	
	@Override
	public String toString() {
		return this.value + this.dataType;
	}
}

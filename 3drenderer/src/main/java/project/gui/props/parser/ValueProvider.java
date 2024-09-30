package project.gui.props.parser;

import project.gui.props.Property;

class ValueProvider extends AEvaluator {
	Property value;
	
	ValueProvider(Property value) {
		this.value = value;
	}
	
	ValueProvider() {
		this(null);
	}
	
	@Override
	public Property evaluate(IStyleCascade cascade) {
		if( !this.value.isNumeric() ) {
			return this.value;
		}
			// So far, only numeric values must evaluated by the cascade as
			// they may depend on the parent or the element dimensions
		return new Property(
			this.value.getName(), cascade.evaluateFloat(this.value), Property.PX
		);
	}

	@Override
	protected AEvaluator createInstance() {
		return new ValueProvider();
	}
}

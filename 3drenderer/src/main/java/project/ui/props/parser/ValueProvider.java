package project.ui.props.parser;

import project.ui.props.IStyleCascade;
import project.ui.props.Property;

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
			this.value.getName(), cascade.evaluateNumeric(this.value, 0.0f), Property.PX
		);
	}

	@Override
	public AEvaluator createInstance() {
		return new ValueProvider();
	}
}

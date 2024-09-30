package project.gui.props.parser;

import project.gui.props.Property;

class ValueProvider extends Evaluator {
	Property value;
	
	ValueProvider(Property value) {
		this.value = value;
	}
	
	ValueProvider() {
		this(null);
	}

	@Override
	public Property evaluate(IStyleCascade context) {
		Property initial = value;
		String type = initial.getType();
		
		if( type.equals(Property.STRING) ) {
			return initial;
		}
		
		return new Property(initial.getName(), context.evaluate(initial), Property.PX, true);
	}

	@Override
	public void setParent(Evaluator parent) {
		
	}
}

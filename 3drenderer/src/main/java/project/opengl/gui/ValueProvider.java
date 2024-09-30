package project.opengl.gui;

import project.gui.props.Property;

class ValueProvider implements IEvaluator {
	Property value;
	
	ValueProvider(Property value) {
		this.value = value;
	}
	
	ValueProvider() {
		this(null);
	}

	@Override
	public Property evaluate(Context context) {
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

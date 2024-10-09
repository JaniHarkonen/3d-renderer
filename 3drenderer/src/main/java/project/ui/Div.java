package project.ui;

public class Div extends AUIElement {

	public Div(UI ui, String id) {
		super(ui, id);
	}
	
	private Div(AUIElement src) {
		super(src);
	}

	
	@Override
	public AUIElement rendererCopy() {
		return new Div(this);
	}

	@Override
	public boolean rendererEquals(AUIElement previous) {
		if( !(previous instanceof Div) ) {
			return false;
		}
		return this.statistics.equals(((Div) previous).statistics);
	}
	
	@Override
	public Div createInstance(UI ui, String id) {
		return new Div(ui, id);
	}
}

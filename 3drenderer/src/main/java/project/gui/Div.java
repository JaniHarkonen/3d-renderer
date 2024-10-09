package project.gui;

public class Div extends AUIElement {

	public Div(GUI gui, String id) {
		super(gui, id);
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
	public Div createInstance(GUI ui, String id) {
		return new Div(ui, id);
	}
}

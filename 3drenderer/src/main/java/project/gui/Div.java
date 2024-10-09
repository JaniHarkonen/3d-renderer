package project.gui;

public class Div extends AGUIElement {

	public Div(GUI gui, String id) {
		super(gui, id);
	}
	
	private Div(AGUIElement src) {
		super(src);
	}

	
	@Override
	public AGUIElement rendererCopy() {
		return new Div(this);
	}

	@Override
	public boolean rendererEquals(AGUIElement previous) {
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

package project.gui;

import java.util.ArrayList;
import java.util.List;

import project.core.IRenderable;
import project.core.ITickable;
import project.gui.props.IStyleCascade;
import project.gui.props.Properties;

public abstract class AGUIElement implements IRenderable, ITickable {
	public static boolean validateID(String id) {
		for( int i = 0; i < id.length(); i++ ) {
			char c = id.charAt(i);
			if(
				c == '#' || c == '.' || c == '?' || c == '\'' || c == '"' || c == '`' || c == '´' ||
				c == '(' || c == ')' || c == '|' || c == '{' || c == '}' || c == ';'
			) {
				return false;
			}
		}
		return true;
	}
	
	protected final GUI gui;
	protected final String id;
	
	protected Properties properties;
	protected Properties.Statistics statistics; // This will contain property evaluations from last cascade run
	protected List<AGUIElement> children;
	protected Text text;
	
	public AGUIElement(GUI gui, String id) {
		this.id = id;
		this.gui = gui;
		this.properties = new Properties(this);
		this.statistics = new Properties.Statistics();
		this.children = new ArrayList<>();
		this.text = null;
	}
	
		// Renderer copy constructor
	protected AGUIElement(AGUIElement src) {
		this.gui = null;
		this.id = src.id;
		this.properties = new Properties(src.properties);
		this.statistics = new Properties.Statistics(src.statistics);
		
		if( src.text != null ) {
			this.text = new Text(src.text);
		}
		
		this.children = new ArrayList<>(src.children.size());
		for( AGUIElement child : src.children ) {
			this.addChild(child.rendererCopy());
		}
	}
	
	
	@Override
	public void tick(float deltaTime) {
		for( AGUIElement child : this.children ) {
			child.tick(deltaTime);
		}
	}
	
	@Override
	public void submitToRenderer() {
		//Application.getApp().getRenderer().getBackGameState().listGUIRoot(this);
	}
	
	public abstract AGUIElement rendererCopy();
	
	public abstract boolean rendererEquals(AGUIElement previous);
	
	public abstract AGUIElement createInstance(GUI ui, String id);
	
	public void evaluateStatistics(IStyleCascade cascade) {
		this.statistics = cascade.evaluateProperties(this.properties);
	}
	
	void addChild(AGUIElement... children) {
		for( AGUIElement child : children ) {
			this.children.add(child);
		}
	}
	
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	
	public void setText(Text text) {
		this.text = text;
	}
	
	public GUI getGUI() {
		return this.gui;
	}
	
	public String getID() {
		return this.id;
	}
	
	public Properties getProperties() {
		return this.properties;
	}
	
	public List<AGUIElement> getChildren() {
		return this.children;
	}
	
	public Text getText() {
		return this.text;
	}
	
	public Properties.Statistics getStatistics() {
		return this.statistics;
	}
	
	public boolean hasText() {
		return (this.text != null);
	}
}

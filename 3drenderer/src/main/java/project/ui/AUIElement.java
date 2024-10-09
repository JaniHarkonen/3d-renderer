package project.ui;

import java.util.ArrayList;
import java.util.List;

import project.core.IRenderable;
import project.core.ITickable;
import project.ui.props.IStyleCascade;
import project.ui.props.Properties;

public abstract class AUIElement implements IRenderable, ITickable {
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
	
	protected final UI ui;
	protected final String id;
	
	protected Properties properties;
	protected Properties.Statistics statistics; // This will contain property evaluations from last cascade run
	protected List<AUIElement> children;
	protected Text text;
	
	public AUIElement(UI ui, String id) {
		this.id = id;
		this.ui = ui;
		this.properties = new Properties(this);
		this.statistics = new Properties.Statistics();
		this.children = new ArrayList<>();
		this.text = null;
	}
	
		// Renderer copy constructor
	protected AUIElement(AUIElement src) {
		this.ui = null;
		this.id = src.id;
		this.properties = new Properties(src.properties);
		this.statistics = new Properties.Statistics(src.statistics);
		
		if( src.text != null ) {
			this.text = new Text(src.text);
		}
		
		this.children = new ArrayList<>(src.children.size());
		for( AUIElement child : src.children ) {
			this.addChild(child.rendererCopy());
		}
	}
	
	
	@Override
	public void tick(float deltaTime) {
		for( AUIElement child : this.children ) {
			child.tick(deltaTime);
		}
	}
	
	@Override
	public void submitToRenderer() {
		//Application.getApp().getRenderer().getBackGameState().listUIRoot(this);
	}
	
	public abstract AUIElement rendererCopy();
	
	public abstract boolean rendererEquals(AUIElement previous);
	
	public abstract AUIElement createInstance(UI ui, String id);
	
	public void evaluateStatistics(IStyleCascade cascade) {
		this.statistics = cascade.evaluateProperties(this.properties);
	}
	
	void addChild(AUIElement... children) {
		for( AUIElement child : children ) {
			this.children.add(child);
		}
	}
	
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	
	public void setText(Text text) {
		this.text = text;
	}
	
	public UI getUI() {
		return this.ui;
	}
	
	public String getID() {
		return this.id;
	}
	
	public Properties getProperties() {
		return this.properties;
	}
	
	public List<AUIElement> getChildren() {
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

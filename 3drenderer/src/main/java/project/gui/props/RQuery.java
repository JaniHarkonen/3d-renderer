package project.gui.props;

public class RQuery {

	private float maxWidth;
	private float maxHeight;
	private float maxAspectRatio;
	
	public RQuery(float maxWidth, float maxHeight, float maxAspectRatio) {
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
		this.maxAspectRatio = maxAspectRatio;
	}
	
	public RQuery(float maxWidth, float maxHeight) {
		this(maxWidth, maxHeight, Float.MAX_VALUE);
	}
	
	public RQuery(float maxAspectRatio) {
		this(Float.MAX_VALUE, Float.MAX_VALUE, maxAspectRatio);
	}
	
	public RQuery(RQuery src) {
		this(src.maxWidth, src.maxHeight, src.maxAspectRatio);
	}
	
	public RQuery() {
		this(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
	}
	
	
	public boolean check(float windowWidth, float windowHeight) {
		return (
			windowWidth < this.maxWidth && 
			windowHeight < this.maxHeight && 
			(windowWidth / windowHeight) < this.maxAspectRatio
		);
	}
	
	public void setWidth(float maxWidth) {
		this.maxWidth = maxWidth;
	}
	
	public void setHeight(float maxHeight) {
		this.maxHeight = maxHeight;
	}
	
	public void setAspectRatio(float maxAspectRatio) {
		this.maxAspectRatio = maxAspectRatio;
	}
	
	@Override
	public boolean equals(Object o) {
		if( !(o instanceof RQuery) ) {
			return false;
		}
		
		RQuery r = (RQuery) o;
		return (
			this.maxWidth == r.maxWidth && 
			this.maxHeight == r.maxHeight && 
			this.maxAspectRatio == r.maxAspectRatio
		);
	}
}

package project.asset;

public interface IGraphics {

	public boolean generate();
	
	public boolean regenerate();
	
	public boolean dispose();
	
	public IGraphics createReference(IGraphicsAsset graphicsAsset);
	
	public void dropGraphicsAsset();
	
	public IGraphicsAsset getGraphicsAsset();
	
	public boolean isGenerated();
	
	public boolean isNull();
}

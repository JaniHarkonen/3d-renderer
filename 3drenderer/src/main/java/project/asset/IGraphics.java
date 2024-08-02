package project.asset;

public interface IGraphics {

	public boolean generate();
	
	public boolean dispose();
	
	public IGraphicsAsset getGraphicsAsset();
	
	public boolean isGenerated();
}

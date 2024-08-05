package project.asset;

public interface IAsset {

	public String getName();
	
	public long getLastUpdateTimestamp();
	
	public boolean deload();
}

package project.core.asset;

public interface IAsset {

	public String getName();
	
	public long getLastUpdateTimestamp();
	
	public boolean deload();
}

package project.core.asset;

public interface IAsset {
	
	public boolean deload();

	public void update(long timestamp);
	
	public String getName();
	
	public long getLastUpdateTimestamp();
}

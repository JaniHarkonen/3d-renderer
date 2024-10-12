package project.core.asset;

import java.util.List;

public interface ILoadTask {

	public boolean load();
	
	public List<IAsset> getTargetAssets();
}

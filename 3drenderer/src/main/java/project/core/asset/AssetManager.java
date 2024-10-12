package project.core.asset;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import project.shared.logger.Logger;

public class AssetManager {

	/************************* LoadProcess-class *************************/
	
	private class LoadProcess extends Thread {
		private ILoadTask loadTask;
		
		private LoadProcess(ILoadTask loadTask) {
			super();
			this.loadTask = loadTask;
		}
		
		
		@Override
		public void run() {
			this.loadTask.load();
		}
	}
	
	/************************* TaskResult-class *************************/
	
	private class TaskResult {
		private IAsset targetAsset;
		private IAssetData assetData;
		private ISystem assetSystem;
		
		private TaskResult(IAsset targetAsset, IAssetData assetData, ISystem assetSystem) {
			this.targetAsset = targetAsset;
			this.assetData = assetData;
			this.assetSystem = assetSystem;
		}
	}
	
	
	/************************* AssetManager-class *************************/
	
	private final Queue<TaskResult> taskResults;
	private final Map<String, IAsset> assetTable;
	
	public AssetManager() {
		this.taskResults = new ConcurrentLinkedQueue<>();
		this.assetTable = new HashMap<>();
	}
	
	
	public void scheduleLoadTask(ILoadTask loadTask) {
		LoadProcess process = new LoadProcess(loadTask);
		for( IAsset targetAsset : loadTask.getTargetAssets() ) {
			this.assetTable.put(targetAsset.getName(), targetAsset);			
		}
		process.start();
	}
	
	public void processTaskResults(long timestamp) {
		TaskResult result;
		while( (result = this.taskResults.poll()) != null ) {
			result.assetData.assign(timestamp);
			
			if( result.assetSystem != null ) {
				result.assetSystem.assetLoaded(result.targetAsset);
			}
		}
	}
	
	public void notifyResult(IAsset targetAsset, IAssetData assetData, ISystem system) {
		this.taskResults.add(new TaskResult(targetAsset, assetData, system));
	}
	
	public void notifyResult(IAsset targetAsset, IAssetData assetData) {
		this.notifyResult(targetAsset, assetData, null);
	}
	
	public IAsset getAsset(String assetName) {
		return this.assetTable.get(assetName);
	}
	
	public IAsset getAssetOrDefault(String assetName, IAsset defaultAsset) {
		IAsset asset = this.getAsset(assetName);
		return (asset != null) ? asset : defaultAsset;
	}
	
	public void logAssets() {
		Logger.get().info(this, (batch) -> {
			for( IAsset asset : this.assetTable.values() ) {
				batch.addMessage(asset.getName());
			}
			return true;
		});
	}
}

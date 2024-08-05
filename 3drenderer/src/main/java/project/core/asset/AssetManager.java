package project.core.asset;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

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
	
	public AssetManager() {
		this.taskResults = new ConcurrentLinkedQueue<>();
	}
	
	
	public void scheduleLoadTask(ILoadTask loadTask, ISystem system) {
		LoadProcess process = new LoadProcess(loadTask);
		process.start();
	}
	
	public void scheduleLoadTask(ILoadTask loadTask) {
		this.scheduleLoadTask(loadTask, null);
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
}

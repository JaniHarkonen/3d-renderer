package project.shared;

public class Networker implements Runnable {

	private final INetworkingStrategy networkingStrategy;
	
	private volatile boolean isRunning;
	
	public Networker(INetworkingStrategy networkingStrategy) {
		this.networkingStrategy = networkingStrategy;
		this.isRunning = false;
	}

	
	@Override
	public void run() {
		this.isRunning = true;
		this.networkingStrategy.startUp();
		
		while( this.isRunning ) {
			this.networkingStrategy.loop();
		}
		
		this.networkingStrategy.close();
		this.networkingStrategy.dispose();
	}
	
	public void shutdown() {
		this.isRunning = false;
	}
	
	public boolean isRunning() {
		return this.isRunning;
	}
}

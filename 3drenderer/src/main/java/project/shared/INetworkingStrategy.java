package project.shared;

public interface INetworkingStrategy {

	public void startUp();
	
	public void loop();
	
	public void close();

	public void dispose();
}

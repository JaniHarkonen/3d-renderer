package project.serversketch;

public class Server {
	
	public static void main(String[] args) {
		Server server = new Server(12345);
		server.execute();
	}
	
	
	private int port;
	
	private Server(int port) {
		this.port = port;
	}
	
	public void execute() {
		System.out.println("JOHNEngine server started!");
		
		Thread networkerThread = new Thread(new Networker(this));
		networkerThread.start();
		
		long time = System.nanoTime();
		while( true ) {
			if( System.nanoTime() - time >= 1000000000 ) {
				System.out.println("server tick");
				time = System.nanoTime();
			}
		}
	}
	
	public int getPort() {
		return this.port;
	}
}

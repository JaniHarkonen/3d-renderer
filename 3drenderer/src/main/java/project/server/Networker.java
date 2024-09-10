package project.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import project.server.game.AGameObject;
import project.server.game.GameState;
import project.server.game.IGameComponent;
import project.server.game.Transform;
import project.server.netstrt.ComponentMessageManager;
import project.server.netstrt.IComponentMessageStrategy;
import project.server.netstrt.TransformUpdated;
import project.shared.ConnectionHandler;
import project.shared.INetworkMessage;
import project.shared.MObjectCreated;
import project.shared.MObjectDeleted;
import project.shared.MTransformUpdate;
import project.shared.NetworkStandard;
import project.utils.DebugUtils;


public class Networker implements Runnable {

	private final NetworkStandard networkStandard;
	
	private volatile boolean isSessionAuthorized;
	
	private int port;
	private ServerSocket serverSocket;
	private Map<ConnectionHandler, Boolean> connections;
	private Queue<INetworkMessage> fullWorldUpdateMessages;
	private ComponentMessageManager gameComponentMessages;
	
	public Networker(NetworkStandard networkStandard, int port) {
		this.networkStandard = networkStandard;
		this.port = port;
		this.isSessionAuthorized = false;
		this.serverSocket = null;
		this.connections = new ConcurrentHashMap<>();
		this.fullWorldUpdateMessages = null;
		
		this.gameComponentMessages = new ComponentMessageManager()
		.addStrategy(Transform.class, new TransformUpdated());
	}

	
	@Override
	public void run() {
		this.startUp();
		DebugUtils.log(this, "JOHNEngine server started!");
		this.listenConnections();
		this.closeDown();
	}
	
	private void startUp() {
		try {
			this.serverSocket = new ServerSocket(this.port);
			this.isSessionAuthorized = true;
		} catch( IOException e ) {
			DebugUtils.log(this, "ERROR: Unable to open server socket!", "PORT: " + this.port);
			e.printStackTrace();
		}
	}
	
	private void listenConnections() {
		DebugUtils.log(this, "Listening to port " + this.port + "...");
		while( this.isSessionAuthorized ) {
			try {
				Socket accepted = this.serverSocket.accept();
				if( accepted != null ) {
					ConnectionHandler handler = new ConnectionHandler(
						this.networkStandard,
						accepted,
						new ConcurrentLinkedQueue<>(),
						new ConcurrentLinkedQueue<>()
					);
					handler.initialize();
					handler.queueMessage(this.fullWorldUpdateMessages);
					this.connections.put(handler, true);
				}
			} catch( IOException e ) {
				DebugUtils.log(this, "ERROR: Unable to accept a connection!");
				e.printStackTrace();
			}
		}
	}
	
	private void closeDown() {
		for( ConnectionHandler handler : this.connections.keySet() ) {
			try {
				handler.close();
			} catch( IOException e ) {
				DebugUtils.log(this, "ERROR: Unable to close a connection handler!");
				e.printStackTrace();
			}
		}
		
		this.connections.clear();
	}
	
	public void handleInboundMessages() {
		for( ConnectionHandler handler : this.connections.keySet() ) {
			try {
				handler.receiveMessages();
			} catch( IOException e ) {
				DebugUtils.log(this, "ERROR: Unable to receive messages from a client!");
				e.printStackTrace();
			}
			
			INetworkMessage message;
			while( (message = handler.pollMessage()) != null ) {
				message.resolve();
			}
		}
	}
	
	public void handleOutboundMessages() {
		GameState latestGameState = Application.getApp().getGame().getLatestGameState();
		this.fullWorldUpdateMessages = this.generateFullWorldUpdateMessages(latestGameState);
		Queue<INetworkMessage> deltaUpdate = 
			this.generateWorldDeltaUpdateMessages(latestGameState.getDelta());
		
		
			// Send delta update if changes occurred 
		if( deltaUpdate.size() <= 0 ) {
			return;
		}
		
		for( ConnectionHandler handler : this.connections.keySet() ) {
			handler.queueMessage(deltaUpdate);
				
				// THIS SHOULD BE HANDLED ON A SEPARATE THREAD
			try {
				handler.sendMessages();
			} catch( Exception e ) {
				
			}
		}
	}
	
	private Queue<INetworkMessage> generateFullWorldUpdateMessages(GameState gameState) {
		Queue<INetworkMessage> messages = new LinkedList<>();
		for( AGameObject object : gameState.getObjects().values() ) {
			Transform objectTransform = object.getTransform();
			messages.add(new MObjectCreated(object.getObjectType(), object.getID()));
			messages.add(new MTransformUpdate(
				object.getID(), 
				objectTransform.getPosition(), 
				objectTransform.getRotator().getAsEulerAngles(), 
				objectTransform.getScale()
			));
		}
		return messages;
	}
	
	private Queue<INetworkMessage> generateWorldDeltaUpdateMessages(GameState.Delta delta) {
		Queue<INetworkMessage> messages = new LinkedList<>();
		
		for( AGameObject object : delta.getAdditions() ) {
			messages.add(new MObjectCreated(object.getObjectType(), object.getID()));
		}
		
		for( IGameComponent component : delta.getAlteredComponents() ) {
			IComponentMessageStrategy messageStrategy = 
				this.gameComponentMessages.getStrategy(component.getClass());
			INetworkMessage message = messageStrategy.createMessage(component);
			messages.add(message);
		}
		
		for( Integer id : delta.getDeletions() ) {
			messages.add(new MObjectDeleted(id));
		}
		
		return messages;
	}
	
	public void shutdown() {
		this.isSessionAuthorized = false;
		
		try {
			this.serverSocket.close();	// Server socket must be closed here to break the accept() block
		} catch( IOException e ) {
			DebugUtils.log(this, "ERROR: Unable to close the server socket!");
			e.printStackTrace();
		}
	}
}

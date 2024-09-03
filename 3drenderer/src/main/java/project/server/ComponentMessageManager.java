package project.server;

import java.util.HashMap;
import java.util.Map;

import project.server.NEW.IGameComponent;
import project.server.netstrt.IComponentMessageStrategy;

public class ComponentMessageManager {

	private Map<Class<? extends IGameComponent>, IComponentMessageStrategy> messageStrategies;
	
	public ComponentMessageManager() {
		this.messageStrategies = new HashMap<>();
	}
	
	
	public ComponentMessageManager addStrategy(
		Class<? extends IGameComponent> clazz, IComponentMessageStrategy strategy
	) {
		this.messageStrategies.put(clazz, strategy);
		return this;
	}
	
	public IComponentMessageStrategy getStrategy(Class<? extends IGameComponent> clazz) {
		return this.messageStrategies.get(clazz);
	}
}

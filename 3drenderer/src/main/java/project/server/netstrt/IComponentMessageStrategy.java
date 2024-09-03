package project.server.netstrt;

import project.server.NEW.IGameComponent;
import project.shared.INetworkMessage;

public interface IComponentMessageStrategy {

	public INetworkMessage createMessage(IGameComponent gameComponent);
}

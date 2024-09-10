package project.server.netstrt;

import project.server.game.IGameComponent;
import project.shared.INetworkMessage;

public interface IComponentMessageStrategy {

	public INetworkMessage createMessage(IGameComponent gameComponent);
}

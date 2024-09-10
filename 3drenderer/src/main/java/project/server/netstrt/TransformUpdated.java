package project.server.netstrt;

import project.server.game.IGameComponent;
import project.server.game.Transform;
import project.shared.MTransformUpdate;

public class TransformUpdated implements IComponentMessageStrategy {

	@Override
	public MTransformUpdate createMessage(IGameComponent gameComponent) {
		Transform transform = (Transform) gameComponent;
		return new MTransformUpdate(
			gameComponent.getHostID(),
			transform.getPosition(),
			transform.getRotator().getAsEulerAngles(),
			transform.getScale()
		);
	}
}

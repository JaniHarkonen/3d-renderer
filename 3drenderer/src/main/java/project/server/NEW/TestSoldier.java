package project.server.NEW;

import org.joml.Vector3f;

public class TestSoldier extends AGameObject {
	
	public TestSoldier() {
		super();
		this.transform.setPosition(1, -10, -100);
	}
	

	@Override
	public void tick(float deltaTime) {
		Vector3f position = this.transform.getPosition();
		this.transform.setPosition(position.x, position.y, position.z + 4 * deltaTime);
		this.transform.updateTransformMatrix();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TestSoldier deepCopy() {
		TestSoldier copy = new TestSoldier();
		copy.transform = new Transform(this.transform);
		return copy;
	}

	@Override
	public boolean notifyChanges(AGameObject comparison, GameState gameState) {
		TestSoldier other = (TestSoldier) comparison;
		
		if( this.transform.equals(other.transform) ) {
			return false;
		}
		
		gameState.notifyComponentChange(this.transform);
		return true;
	}
	
	@Override
	public int getObjectType() {
		return 1;
	}
}

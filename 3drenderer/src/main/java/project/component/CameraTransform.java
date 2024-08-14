package project.component;

public class CameraTransform extends Transform {
	
	public CameraTransform() {
		super();
	}
	
	public CameraTransform(CameraTransform src) {
		super(src);
	}

	@Override
	public void updateTransformMatrix() {
		this.transformMatrix.identity()
		.rotate(this.rotator.getAsQuaternion())
		.translate(-this.position.x, -this.position.y, -this.position.z);
	}
}

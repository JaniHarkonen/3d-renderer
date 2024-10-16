package project.component;

import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import project.scene.Camera;

public class CascadeShadow {

	public static final int SHADOW_MAP_CASCADE_COUNT = 3;
	
	private Matrix4f lightView;
	private float splitDistance;
	
	public CascadeShadow() {
		this.lightView = new Matrix4f();
		this.splitDistance = 0.0f;
	}
	
	
    public static void updateCascadeShadows(List<CascadeShadow> cascadeShadows, Camera activeCamera, Vector3f light) {
    	activeCamera.getProjection().update(800, 600);
        Matrix4f viewMatrix = activeCamera.getTransform().getAsMatrix();
        Matrix4f projMatrix = activeCamera.getProjection().getMatrix();
        Vector4f lightPos = new Vector4f(light.x, light.y, light.z, 0);

        float cascadeSplitLambda = 0.95f;

        float[] cascadeSplits = new float[SHADOW_MAP_CASCADE_COUNT];

        float nearClip = projMatrix.perspectiveNear();
        float farClip = projMatrix.perspectiveFar();
        float clipRange = farClip - nearClip;

        float minZ = nearClip;
        float maxZ = nearClip + clipRange;

        float range = maxZ - minZ;
        float ratio = maxZ / minZ;

        // Calculate split depths based on view camera frustum
        // Based on method presented in https://developer.nvidia.com/gpugems/GPUGems3/gpugems3_ch10.html
        for( int i = 0; i < SHADOW_MAP_CASCADE_COUNT; i++ ) {
            float p = (i + 1) / (float) (SHADOW_MAP_CASCADE_COUNT);
            float log = (float) (minZ * java.lang.Math.pow(ratio, p));
            float uniform = minZ + range * p;
            float d = cascadeSplitLambda * (log - uniform) + uniform;
            cascadeSplits[i] = (d - nearClip) / clipRange;
        }

        // Calculate orthographic projection matrix for each cascade
        float lastSplitDist = 0.0f;
        for( int i = 0; i < SHADOW_MAP_CASCADE_COUNT; i++ ) {
            float splitDist = cascadeSplits[i];

            Vector3f[] frustumCorners = new Vector3f[]{
                    new Vector3f(-1.0f, 1.0f, -1.0f),
                    new Vector3f(1.0f, 1.0f, -1.0f),
                    new Vector3f(1.0f, -1.0f, -1.0f),
                    new Vector3f(-1.0f, -1.0f, -1.0f),
                    new Vector3f(-1.0f, 1.0f, 1.0f),
                    new Vector3f(1.0f, 1.0f, 1.0f),
                    new Vector3f(1.0f, -1.0f, 1.0f),
                    new Vector3f(-1.0f, -1.0f, 1.0f),
            };

            // Project frustum corners into world space
            Matrix4f invCam = (new Matrix4f(projMatrix).mul(viewMatrix)).invert();
            for( int j = 0; j < 8; j++ ) {
                Vector4f invCorner = new Vector4f(frustumCorners[j], 1.0f).mul(invCam);
                frustumCorners[j] = new Vector3f(invCorner.x / invCorner.w, invCorner.y / invCorner.w, invCorner.z / invCorner.w);
            }

            for( int j = 0; j < 4; j++ ) {
                Vector3f dist = new Vector3f(frustumCorners[j + 4]).sub(frustumCorners[j]);
                frustumCorners[j + 4] = new Vector3f(frustumCorners[j]).add(new Vector3f(dist).mul(splitDist));
                frustumCorners[j] = new Vector3f(frustumCorners[j]).add(new Vector3f(dist).mul(lastSplitDist));
            }

            // Get frustum center
            Vector3f frustumCenter = new Vector3f(0.0f);
            for( int j = 0; j < 8; j++ ) {
                frustumCenter.add(frustumCorners[j]);
            }
            frustumCenter.div(8.0f);

            float radius = 0.0f;
            for( int j = 0; j < 8; j++ ) {
                float distance = (new Vector3f(frustumCorners[j]).sub(frustumCenter)).length();
                radius = java.lang.Math.max(radius, distance);
            }
            radius = (float) Math.ceil(radius * 16.0f) / 16.0f;

            Vector3f maxExtents = new Vector3f(radius);
            Vector3f minExtents = new Vector3f(maxExtents).mul(-1);

            Vector3f lightDir = (new Vector3f(lightPos.x, lightPos.y, lightPos.z).mul(-1)).normalize();
            Vector3f eye = new Vector3f(frustumCenter).sub(new Vector3f(lightDir).mul(-minExtents.z));
            Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);
            Matrix4f lightViewMatrix = new Matrix4f().lookAt(eye, frustumCenter, up);
            Matrix4f lightOrthoMatrix = new Matrix4f().ortho
                    (minExtents.x, maxExtents.x, minExtents.y, maxExtents.y, 0.0f, maxExtents.z - minExtents.z, true);

            // Store split distance and matrix in cascade
            CascadeShadow cascadeShadow = cascadeShadows.get(i);
            cascadeShadow.splitDistance = (nearClip + splitDist * clipRange) * -1.0f;
            cascadeShadow.lightView = lightOrthoMatrix.mul(lightViewMatrix);

            lastSplitDist = cascadeSplits[i];
        }
    }
	
	public Matrix4f getLightViewMatrix() {
		return this.lightView;
	}
	
	public float getSplitDistance() {
		return this.splitDistance;
	}
}

package project.utils;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.assimp.AIMatrix4x4;
import org.lwjgl.assimp.AIVector3D;

import project.asset.sceneasset.Mesh;

public final class GeometryUtils {

	public static Matrix4f aiMatrix4ToMatrix4f(AIMatrix4x4 aiMatrix) {
        Matrix4f result = new Matrix4f();
        result.m00(aiMatrix.a1());
        result.m10(aiMatrix.a2());
        result.m20(aiMatrix.a3());
        result.m30(aiMatrix.a4());
        result.m01(aiMatrix.b1());
        result.m11(aiMatrix.b2());
        result.m21(aiMatrix.b3());
        result.m31(aiMatrix.b4());
        result.m02(aiMatrix.c1());
        result.m12(aiMatrix.c2());
        result.m22(aiMatrix.c3());
        result.m32(aiMatrix.c4());
        result.m03(aiMatrix.d1());
        result.m13(aiMatrix.d2());
        result.m23(aiMatrix.d3());
        result.m33(aiMatrix.d4());

        return result;
	}
	
	public static Vector3f[] aiVector3DBufferToVector3fArray(AIVector3D.Buffer buffer) {
		if( buffer == null ) {
			return new Vector3f[0];
		}
		
		Vector3f[] result = new Vector3f[buffer.remaining()];
		for( int i = 0; buffer.remaining() > 0; i++ ) {
			AIVector3D aiVector = buffer.get();
			result[i] = new Vector3f(aiVector.x(), aiVector.y(), aiVector.z());
		}
		
		return result;
	}
	
	public static Vector2f[] aiVector3DBufferToVector2fArray(AIVector3D.Buffer buffer) {
		if( buffer == null ) {
			return new Vector2f[0];
		}
		
		Vector2f[] result = new Vector2f[buffer.remaining()];
		for( int i = 0; buffer.remaining() > 0; i++ ) {
			AIVector3D aiVector = buffer.get();
			result[i] = new Vector2f(aiVector.x(), aiVector.y());
		}
		
		return result;
	}
	
	public static float[] vector3fArrayToFloatArray(Vector3f[] vectorArray) {
		float[] result = new float[vectorArray.length * 3];
		for( int i = 0; i < vectorArray.length; i++ ) {
			Vector3f vector = vectorArray[i];
			result[i * 3] = vector.x;
			result[i * 3 + 1] = vector.y;
			result[i * 3 + 2] = vector.z;
		}
		return result;
	}
	
	public static float[] vector2fArrayToFloatArray(Vector2f[] vectorArray) {
		float[] result = new float[vectorArray.length * 2];
		for( int i = 0; i < vectorArray.length; i++ ) {
			Vector2f vector = vectorArray[i];
			result[i * 2] = vector.x;
			result[i * 2 + 1] = vector.y;
		}
		return result;
	}
	
	public static int[] faceArrayToIntArray(Mesh.Face[] faceArray) {
		int[] result = new int[faceArray.length * Mesh.Face.INDICES_PER_FACE];
		for( int i = 0; i < faceArray.length; i++ ) {
			Mesh.Face face = faceArray[i];
			result[i * 3] = face.getIndex(0);
			result[i * 3 + 1] = face.getIndex(1);
			result[i * 3 + 2] = face.getIndex(2);
		}
		return result;
	}
}

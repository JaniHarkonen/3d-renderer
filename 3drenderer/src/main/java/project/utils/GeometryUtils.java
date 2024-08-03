package project.utils;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.assimp.AIMatrix4x4;
import org.lwjgl.assimp.AIVector3D;

import project.Globals;
import project.asset.Mesh;

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
	
	public static Mesh createPlaneMesh(String name, float x, float y, float w, float h, float... UVs) {
		Mesh mesh = Mesh.createMesh(
			name,
			new Vector3f[] {
				new Vector3f(x, y, 0.0f),
				new Vector3f(x + w, y, 0.0f),
				new Vector3f(x + w, y + h, 0.0f),
				new Vector3f(x, y + h, 0.0f)
    		}, 
    		new Vector3f[0],
    		new Vector3f[0],
    		new Vector3f[0],
    		new Vector3f[] {
				new Vector3f(UVs[0], UVs[1], 0),
				new Vector3f(UVs[2], UVs[1], 0),
				new Vector3f(UVs[2], UVs[3], 0),
				new Vector3f(UVs[0], UVs[3], 0)
    		},
    		new Mesh.Face[] {
				new Mesh.Face(new int[] {0, 1, 2}),
				new Mesh.Face(new int[] {2, 3, 0})
    		},
    		null
		);
		Globals.RENDERER.assetLoaded(mesh);
		/*Mesh.Data meshData = new Mesh.Data(
			new Vector3f[] {
				new Vector3f(x, y, 0.0f),
				new Vector3f(x + w, y, 0.0f),
				new Vector3f(x + w, y + h, 0.0f),
				new Vector3f(x, y + h, 0.0f)
    		}, 
    		new Vector3f[0],
    		new Vector3f[0],
    		new Vector3f[0],
    		new Vector3f[] {
				new Vector3f(UVs[0], UVs[1], 0),
				new Vector3f(UVs[2], UVs[1], 0),
				new Vector3f(UVs[2], UVs[3], 0),
				new Vector3f(UVs[0], UVs[3], 0)
    		},
    		new Mesh.Face[] {
				new Mesh.Face(new int[] {0, 1, 2}),
				new Mesh.Face(new int[] {2, 3, 0})
    		},
    		null
		);*/
		/*Mesh mesh = new Mesh(name);
        mesh.populate(
    		new Vector3f[] {
				new Vector3f(x, y, 0.0f),
				new Vector3f(x + w, y, 0.0f),
				new Vector3f(x + w, y + h, 0.0f),
				new Vector3f(x, y + h, 0.0f)
    		}, 
    		new Vector3f[0],
    		new Vector3f[0],
    		new Vector3f[0],
    		new Vector3f[] {
				new Vector3f(UVs[0], UVs[1], 0),
				new Vector3f(UVs[2], UVs[1], 0),
				new Vector3f(UVs[2], UVs[3], 0),
				new Vector3f(UVs[0], UVs[3], 0)
    		},
    		new Mesh.Face[] {
				new Mesh.Face(new int[] {0, 1, 2}),
				new Mesh.Face(new int[] {2, 3, 0})
    		},
    		null
		);*/
        
		/*Mesh mesh = new Mesh(name);
        Globals.ASSET_MANAGER.addResult(mesh, meshData);
        
        return mesh;*/
		return mesh;
	}
}

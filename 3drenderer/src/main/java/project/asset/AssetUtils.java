package project.asset;

import java.nio.ByteBuffer;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

import project.Application;
import project.asset.sceneasset.Mesh;
import project.asset.sceneasset.SceneAssetLoadTask;

public final class AssetUtils {

	public static ByteBuffer stringToPixels(
		String zeroOneString, int width, int height, int channelCount
	) {
        ByteBuffer pixels = MemoryUtil.memAlloc(zeroOneString.length() * channelCount);
        
            // Populate pixels upside-down due to OpenGL
        for( int i = zeroOneString.length() - 1; i >= 0; i-- )
        {
            byte value = (zeroOneString.charAt(i) == '1') ? (byte) 255 : 0;
            
            pixels.put((byte) value);
            pixels.put((byte) value);
            pixels.put((byte) value);
            pixels.put((byte) 255);
        }
        
        pixels.flip();
        
        return pixels;
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
    		new Vector2f[] {
				new Vector2f(UVs[0], UVs[1]),
				new Vector2f(UVs[2], UVs[1]),
				new Vector2f(UVs[2], UVs[3]),
				new Vector2f(UVs[0], UVs[3])
    		},
    		new Mesh.Face[] {
				new Mesh.Face(new int[] {0, 1, 2}),
				new Mesh.Face(new int[] {2, 3, 0})
    		},
    		new float[4 * 3 * SceneAssetLoadTask.MAX_WEIGHT_COUNT],
			new int[4 * 3 * SceneAssetLoadTask.MAX_WEIGHT_COUNT]
		);
		
		Application.getApp().getRenderer().assetLoaded(mesh);
		return mesh;
	}
}

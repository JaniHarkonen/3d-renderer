package project.asset;

import java.nio.ByteBuffer;

import org.lwjgl.system.MemoryUtil;

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
}

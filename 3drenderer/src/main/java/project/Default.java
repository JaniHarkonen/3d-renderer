package project;
import org.joml.Vector3f;

import project.asset.Mesh;
import project.asset.Texture;
import project.asset.Mesh.Face;

public final class Default {

	public static final Mesh MESH = Mesh.createMesh(
		"mesh-default",
		new Vector3f[] {
			new Vector3f(0, 0, 0),
			new Vector3f(10, 0, 0),
			new Vector3f(0, 10, 0),
			new Vector3f(10, 10, 0),
		}, 
		new Vector3f[] {
			new Vector3f(0, 0, 0),
			new Vector3f(0, 0, 0),
			new Vector3f(0, 0, 0),
			new Vector3f(0, 0, 0),	
		},
		new Vector3f[] {
			new Vector3f(0, 0, 0),
			new Vector3f(0, 0, 0),
			new Vector3f(0, 0, 0),
			new Vector3f(0, 0, 0),	
		},
		new Vector3f[] {
			new Vector3f(0, 0, 0),
			new Vector3f(0, 0, 0),
			new Vector3f(0, 0, 0),
			new Vector3f(0, 0, 0),	
		},
		new Vector3f[] {
			new Vector3f(0, 0, 0),
			new Vector3f(1, 0, 0),
			new Vector3f(0, 1, 0),
			new Vector3f(1, 1, 0),
		}, 
		new Face[] {
			new Face(new int[] { 0, 1, 3 } ),
			new Face(new int[] { 0, 3, 2 } )
		},
		null
	);
	
	public static final Texture TEXTURE = Texture.createTexture(
		"tex-default",
			// T_T
        "0000000000000000" + 
        "0000000000000000" + 
        "0000000000000000" + 
        "0111110000111110" + 
        "0001000000001000" + 
        "0001000000001000" + 
        "0001000000001000" + 
        "0001000000001000" + 
        "0001000000001000" + 
        "0001000000001000" + 
        "0001000000001000" + 
        "0001000000001000" + 
        "0000011111100000" + 
        "0000000000000000" + 
        "0000000000000000" + 
        "0000000000000000"
        
            // WTF
        /*
        "0001000000001000" + 
        "0001000110001000" + 
        "0000101001010000" + 
        "0000010000100000" + 
        "0000000000000000" + 
        "0000011111110000" + 
        "0000000010000000" + 
        "0000000010000000" + 
        "0000000010000000" + 
        "0000000010000000" + 
        "0000000000000000" + 
        "0000011111100000" + 
        "0000000000100000" + 
        "0000000111100000" + 
        "0000000000100000" + 
        "0000000000100000"
          */ 
    );
}

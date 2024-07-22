package project.testing;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import project.asset.Mesh;
import project.asset.SceneAssetLoadTask;
import project.component.Material;
import project.opengl.Texture;
import project.scene.Model;
import project.scene.Scene;
import project.utils.FileUtils;

public final class TestUtils {

	public static Model createModelFromMeshesAndTextures(Scene scene, String meshPath, int meshCount, String[] texturePaths/*, Texture[ normals]*/) {
		Map<String, Material> materialTextureNameMap = new HashMap<>();
		/*for( String texturePath : texturePaths ) {
			Material material = new Material();
			Texture diffuse = new Texture(FileUtils.getResourcePath(texturePath));
			material.setTexture(0, diffuse);
			materialTextureNameMap.put((new File(texturePath).getName()), material);
		}*/
		
		SceneAssetLoadTask task = new SceneAssetLoadTask(FileUtils.getResourcePath(meshPath));
		Mesh[] meshes = new Mesh[meshCount];
		for( int i = 0; i < meshCount; i++ ) {
			meshes[i] = new Mesh();
		}
		
		task.expectMesh(meshes);
		task.load();
		
		Model result = new Model(scene);
		
		for( int i = 0; i < meshCount; i++ ) {
			result.addMesh(meshes[i], materialTextureNameMap.get((new File(texturePaths[i]).getName())));
		}
		
		return result;
	}
}
;
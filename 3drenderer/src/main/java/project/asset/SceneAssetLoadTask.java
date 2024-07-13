package project.asset;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.Assimp;

import project.utils.DebugUtils;

public class SceneAssetLoadTask {

	public static final int DEFAULT_FLAGS = (
		Assimp.aiProcess_GenSmoothNormals | 
		Assimp.aiProcess_JoinIdenticalVertices |
		Assimp.aiProcess_Triangulate | 
		Assimp.aiProcess_FixInfacingNormals | 
		Assimp.aiProcess_CalcTangentSpace | 
		Assimp.aiProcess_LimitBoneWeights |
		Assimp.aiProcess_PreTransformVertices
	);
	
	private String assetPath;
	private List<Mesh> expectedMeshes;
	private int importFlags;
	
	public SceneAssetLoadTask(String assetPath, int flags) {
		this.assetPath = assetPath;
		this.expectedMeshes = new ArrayList<>();
		this.importFlags = flags;
	}
	
	public SceneAssetLoadTask(String assetPath) {
		this(assetPath, SceneAssetLoadTask.DEFAULT_FLAGS);
	}
	
	
	public void load() {
		AIScene aiScene = Assimp.aiImportFile(this.assetPath, this.importFlags);
		
		if( aiScene == null ) {
			DebugUtils.log(this, "ERROR: Failed to load scene from path: ", this.assetPath);
			return;
		}
		
		int meshCount = aiScene.mNumMeshes();
		int expectedMeshCount = this.expectedMeshes.size();
		
		if( meshCount != expectedMeshCount ) {
			DebugUtils.log(
				this, 
				"WARNING: Expected " + expectedMeshCount + " meshes, " + 
				"found " + meshCount + "!"
			);
		}
		
		PointerBuffer aiMeshBuffer = aiScene.mMeshes();
		int s = Math.min(meshCount, expectedMeshCount);
		for( int i = 0; i < s; i++ ) {
			AIMesh aiMesh = AIMesh.create(aiMeshBuffer.get(i));
			
			float[] positions;
			float[] textureCoordinates = new float[0];
			int[] indices;
			
				// Extract indices
			List<Integer> indexList = new ArrayList<>();
			int faceCount = aiMesh.mNumFaces();
			AIFace.Buffer aiFaceBuffer = aiMesh.mFaces();
			for( int j = 0; j < faceCount; j++ ) {
				AIFace aiFace = aiFaceBuffer.get(j);
				IntBuffer indexBuffer = aiFace.mIndices();
				while( indexBuffer.remaining() > 0 ) {
					indexList.add(indexBuffer.get());
				}
			}
			
			indices = new int[indexList.size()];
			for( int j = 0; j < indexList.size(); j++ ) {
				indices[j] = indexList.get(j);
			}
			
				// Extract texture coordinates
			AIVector3D.Buffer textureCoordinateBuffer = aiMesh.mTextureCoords(0);
			
			if( textureCoordinateBuffer != null ) {
				textureCoordinates = new float[textureCoordinateBuffer.remaining() * 2];
				int coordinateIndex = 0;
				while( textureCoordinateBuffer.remaining() > 0 ) {
					AIVector3D textureCoordinate = textureCoordinateBuffer.get();
					textureCoordinates[coordinateIndex++] = textureCoordinate.x();
					textureCoordinates[coordinateIndex++] = 1 - textureCoordinate.y();
				}
			}
			
				// Extract vertices
			AIVector3D.Buffer vertexBuffer = aiMesh.mVertices();
			positions = new float[vertexBuffer.remaining() * 3];
			int vertexIndex = 0;
			while( vertexBuffer.remaining() > 0 ) {
				AIVector3D vertex = vertexBuffer.get();
				positions[vertexIndex++] = vertex.x();
				positions[vertexIndex++] = vertex.y();
				positions[vertexIndex++] = vertex.z();
			}
			
			this.expectedMeshes.get(i).populate(positions, textureCoordinates, indices);
		}
	}
	
	public void expectMesh(Mesh mesh) {
		this.expectedMeshes.add(mesh);
	}
}

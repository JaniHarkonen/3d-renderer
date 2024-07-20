package project.asset;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIAnimation;
import org.lwjgl.assimp.AIBone;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AINode;
import org.lwjgl.assimp.AINodeAnim;
import org.lwjgl.assimp.AIQuatKey;
import org.lwjgl.assimp.AIQuaternion;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.AIVectorKey;
import org.lwjgl.assimp.AIVertexWeight;
import org.lwjgl.assimp.Assimp;

import project.utils.DebugUtils;
import project.utils.GeometryUtils;

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

	public static final int MAX_BONE_COUNT = 150;
	public static final int MAX_WEIGHT_COUNT = 4;
	private static final Matrix4f IDENTITY_MATRIX = new Matrix4f();
	
	private String assetPath;
	private List<Mesh> expectedMeshes;
	private List<Animation> expectedAnimations;
	private int importFlags;
	
	public SceneAssetLoadTask(String assetPath, int flags) {
		this.assetPath = assetPath;
		this.expectedMeshes = new ArrayList<>();
		this.expectedAnimations = new ArrayList<>();
		this.importFlags = flags;
	}
	
	public SceneAssetLoadTask(String assetPath) {
		this(assetPath, SceneAssetLoadTask.DEFAULT_FLAGS);
	}
	
	
	public void load() {
		int preTransformVerticesFlag = (
			this.expectedAnimations.size() > 0 ? 
			0 : Assimp.aiProcess_PreTransformVertices
		);
		
		AIScene aiScene = Assimp.aiImportFile(
			this.assetPath, 
			this.importFlags | preTransformVerticesFlag
		);
		
		if( aiScene == null ) {
			DebugUtils.log(this, "ERROR: Failed to load scene from path: ", this.assetPath);
			return;
		}
		
			//////////////////////////// Extract meshes ////////////////////////////
		int meshCount = aiScene.mNumMeshes();
		int expectedMeshCount = this.expectedMeshes.size();
		
		if( meshCount != expectedMeshCount ) {
			DebugUtils.log(
				this, 
				"WARNING: Expected " + expectedMeshCount + " meshes, " + 
				"found " + meshCount + "!"
			);
		}
		
		List<Bone> boneList = new ArrayList<>();
		PointerBuffer aiMeshBuffer = aiScene.mMeshes();
		int s = Math.min(meshCount, expectedMeshCount);
		for( int i = 0; i < s; i++ ) {
			AIMesh aiMesh = AIMesh.create(aiMeshBuffer.get(i));
			
			float[] positions;
			float[] normals;
			float[] bitangents;
			float[] tangents;
			float[] textureCoordinates = new float[0];
			int[] indices;
			
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
			
				// Extract normals
			AIVector3D.Buffer normalBuffer = aiMesh.mNormals();
			normals = new float[normalBuffer.remaining() * 3];
			int normalIndex = 0;
			while( normalBuffer.remaining() > 0 ) {
				AIVector3D normal = normalBuffer.get();
				normals[normalIndex++] = normal.x();
				normals[normalIndex++] = normal.y();
				normals[normalIndex++] = normal.z();
			}
			
				// Extract tangents
			AIVector3D.Buffer tangentBuffer = aiMesh.mTangents();
			tangents = new float[tangentBuffer.remaining() * 3];
			int tangentIndex = 0;
			while( tangentBuffer.remaining() > 0 ) {
				AIVector3D tangent = tangentBuffer.get();
				tangents[tangentIndex++] = tangent.x();
				tangents[tangentIndex++] = tangent.y();
				tangents[tangentIndex++] = tangent.z();
			}
			
			if( tangents.length == 0 ) {
				tangents = new float[normals.length];
			}
			
			
				// Extract bitangents
			AIVector3D.Buffer bitangentBuffer = aiMesh.mBitangents();
			bitangents = new float[bitangentBuffer.remaining() * 3];
			int bitangentIndex = 0;
			while( bitangentBuffer.remaining() > 0 ) {
				AIVector3D bitangent = bitangentBuffer.get();
				bitangents[bitangentIndex++] = bitangent.x();
				bitangents[bitangentIndex++] = bitangent.y();
				bitangents[bitangentIndex++] = bitangent.z();
			}
			
			if( bitangents.length == 0 ) {
				bitangents = new float[normals.length];
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
			
			if( tangents.length == 0 ) {
				tangents = new float[normals.length];
			}
			
			indices = new int[indexList.size()];
			for( int j = 0; j < indexList.size(); j++ ) {
				indices[j] = indexList.get(j);
			}
			
				// Extract animations and bones
			List<Integer> boneIDs = new ArrayList<>();
			List<Float> weights = new ArrayList<>();
			Map<Integer, List<VertexWeight>> weightSet = new HashMap<>();
			int boneCount = aiMesh.mNumBones();
			PointerBuffer aiBoneBuffer = aiMesh.mBones();
			
			for( int j = 0; j < boneCount; j++ ) {
				AIBone aiBone = AIBone.create(aiBoneBuffer.get(j));
				int boneID = boneList.size();
				Bone bone = new Bone(boneID, aiBone.mName().dataString(), GeometryUtils.aiMatrix4ToMatrix4f(aiBone.mOffsetMatrix()));
				boneList.add(bone);
				
				int weightCount = aiBone.mNumWeights();
				AIVertexWeight.Buffer aiWeights = aiBone.mWeights();
				for( int k = 0; k < weightCount; k++ ) {
					AIVertexWeight aiWeight = aiWeights.get(k);
					VertexWeight weight = new VertexWeight(bone.getID(), aiWeight.mVertexId(), aiWeight.mWeight());
					List<VertexWeight> weightList = weightSet.getOrDefault(weight.getVertexID(), new ArrayList<>());
					weightList.add(weight);
				}
			}
			
			int vertexCount = aiMesh.mNumVertices();
			for( int j = 0; j < vertexCount; j++ ) {
				List<VertexWeight> weightList = weightSet.get(j);
				int weightCount = weightList != null ? weightList.size() : 0;
				for( int k = 0; k < MAX_WEIGHT_COUNT; k++ ) {
					if( k < weightCount ) {
						VertexWeight weight = weightList.get(k);
						weights.add(weight.getWeight());
						boneIDs.add(weight.getBoneID());
					} else {
						weights.add(0.0f);
						boneIDs.add(0);
					}
				}
			}
			
			float[] finalWeights = new float[weights.size()];
			for( int j = 0; j < weights.size(); j++ ) {
				finalWeights[j] = weights.get(j);
			}
			
			int[] finalBoneIDs = new int[boneIDs.size()];
			for( int j = 0; j < boneIDs.size(); j++ ) {
				finalBoneIDs[j] = boneIDs.get(j);
			}
			
			this.expectedMeshes.get(i).populate(
				positions, 
				normals, 
				bitangents, 
				tangents, 
				textureCoordinates, 
				indices, 
				new AnimationMeshData(finalWeights, finalBoneIDs)
			);
		}
		
			////////////////////////////Extract animations ////////////////////////////
		int animationCount = aiScene.mNumAnimations();
		int expectedAnimationCount = this.expectedAnimations.size();
		
		if( animationCount != expectedAnimationCount ) {
			DebugUtils.log(
				this, 
				"WARNING: Expected " + expectedAnimationCount + " animations, " + 
				"found " + animationCount + "!"
			);
		}
		
		animationCount = Math.min(animationCount, expectedAnimationCount);
		
		if( animationCount > 0 ) {
			Node rootNode = this.buildNodesTree(aiScene.mRootNode(), null);
			Matrix4f globalInverseTransform = GeometryUtils.aiMatrix4ToMatrix4f(
				aiScene.mRootNode().mTransformation()
			).invert();
			
			PointerBuffer aiAnimations = aiScene.mAnimations();
			for( int i = 0; i < animationCount; i++ ) {
				AIAnimation aiAnimation = AIAnimation.create(aiAnimations.get(i));
				int frameCount = this.calculateAnimationFrames(aiAnimation);
				List<AnimationFrame> frames = new ArrayList<>();
				
				Animation animation = this.expectedAnimations.get(i);
				animation.setName(aiAnimation.mName().dataString());
				animation.setDuration(aiAnimation.mDuration());
				animation.setFrames(frames);
				
				for( int j = 0; j < frameCount; j++ ) {
					Matrix4f[] boneTransforms = new Matrix4f[MAX_BONE_COUNT];
					Arrays.fill(boneTransforms, new Matrix4f());
					AnimationFrame frame = new AnimationFrame(boneTransforms);
					this.buildFrameMatrices(
						aiAnimation, 
						boneList, 
						frame, 
						j, 
						rootNode, 
						rootNode.getNodeTransform(), 
						globalInverseTransform
					);
					frames.add(frame);
				}
			}
		}
		
		Assimp.aiReleaseImport(aiScene);
		
		
			////////////////////////////Extract meshes ////////////////////////////
		/*s = Math.min(aiScene.mNumMaterials(), this.expectedMaterials.size());
		s = aiScene.mNumMaterials();
		for( int i = 0; i < s; i++ ) {
			Material expected = new Material();//this.expectedMaterials.get(i);
			//try( MemoryStack stack = MemoryStack.stackPush() ) {
			AIMaterial aiMaterial = AIMaterial.create(aiScene.mMaterials().get());
			int textureCount = Assimp.aiGetMaterialTextureCount(aiMaterial, Assimp.aiTextureType_DIFFUSE);
			PointerBuffer pb = MemoryUtil.memAllocPointer(1000);
			Assimp.aiGetMaterialProperty(aiMaterial, "name", pb);
			DebugUtils.log(this, pb.remaining());
			
				
				//AIString aiMaterialName = AIString.calloc(stack);
			

				AIColor4D aiColor = AIColor4D.create();
				int result = Assimp.aiGetMaterialColor(
					aiMaterial, 
					Assimp.AI_MATKEY_COLOR_AMBIENT, 
					Assimp.aiTextureType_NONE, 
					0, 
					aiColor
				);
				
				if( result == Assimp.aiReturn_SUCCESS ) {
					expected.setAmbientColor(
						new Vector4f(aiColor.r(), aiColor.g(), aiColor.b(), aiColor.a())
					);
				}
				
				result = Assimp.aiGetMaterialColor(
					aiMaterial, 
					Assimp.AI_MATKEY_COLOR_DIFFUSE, 
					Assimp.aiTextureType_NONE, 
					0, 
					aiColor
				);
				
				if( result == Assimp.aiReturn_SUCCESS ) {
					expected.setDiffuseColor(
						new Vector4f(aiColor.r(), aiColor.g(), aiColor.b(), aiColor.a())
					);
				}
				
				result = Assimp.aiGetMaterialColor(
					aiMaterial, 
					Assimp.AI_MATKEY_COLOR_SPECULAR, 
					Assimp.aiTextureType_NONE, 
					0, 
					aiColor
				);
				
				if( result == Assimp.aiReturn_SUCCESS ) {
					expected.setSpecularColor(
						new Vector4f(aiColor.r(), aiColor.g(), aiColor.b(), aiColor.a())
					);
				}
				
				DebugUtils.log(
					this, 
					expected.getAmbientColor().x, expected.getAmbientColor().y, expected.getAmbientColor().z, expected.getAmbientColor().w,
					expected.getDiffuseColor().x, expected.getDiffuseColor().y, expected.getDiffuseColor().z, expected.getDiffuseColor().w,
					expected.getSpecularColor().x, expected.getSpecularColor().y, expected.getSpecularColor().z, expected.getSpecularColor().w
				);
				
				float reflectance = 0.0f;
				float[] roughness = new float[1];
			//}
		}*/
	}
	
	private int calculateAnimationFrames(AIAnimation aiAnimation) {
		int maxFrameCount = 0;
		int channelCount = aiAnimation.mNumChannels();
		PointerBuffer aiChannels = aiAnimation.mChannels();
		for( int i = 0; i < channelCount; i++ ) {
			AINodeAnim aiNodeAnim = AINodeAnim.create(aiChannels.get(i));
			int frameCount = Math.max(
				Math.max(aiNodeAnim.mNumPositionKeys(), aiNodeAnim.mNumScalingKeys()), 
				aiNodeAnim.mNumRotationKeys()
			);
			maxFrameCount = Math.max(maxFrameCount, frameCount);
		}
		
		return maxFrameCount;
	}
	
	private void buildFrameMatrices(
		AIAnimation aiAnimation, 
		List<Bone> boneList, 
		AnimationFrame frame, 
		int frameIndex, 
		Node node, 
		Matrix4f parentTransform, 
		Matrix4f globalInverseTransform
	) {
		String nodeName = node.getName();
		AINodeAnim aiNodeAnim = this.findAIAnimationNode(aiAnimation, nodeName);
		Matrix4f nodeTransform = node.getNodeTransform();
		
		if( aiNodeAnim != null ) {
			nodeTransform = this.buildTransformMatrix(aiNodeAnim, frameIndex);
		}
		
		Matrix4f nodeGlobalTransform = new Matrix4f(parentTransform).mul(nodeTransform);
		List<Bone> affectedBones = new ArrayList<>();
		
		for( Bone bone : boneList ) {
			if( !bone.getName().equals(nodeName) ) {
				affectedBones.add(bone);
			}
		}
		
		for( Bone bone : affectedBones ) {
			Matrix4f boneTransform = new Matrix4f(globalInverseTransform)
			.mul(nodeGlobalTransform)
			.mul(bone.getOffsetTransform());
			frame.setBoneTransform(bone.getID(), boneTransform);
		}
		
		for( Node childNode : node.getChildren() ) {
			this.buildFrameMatrices(
				aiAnimation, 
				boneList, 
				frame, 
				frameIndex, 
				childNode, 
				nodeGlobalTransform, 
				globalInverseTransform
			);
		}
	}
	
	private Node buildNodesTree(AINode aiNode, Node parentNode) {
		String nodeName = aiNode.mName().dataString();
		Node node = new Node(
			nodeName, parentNode, GeometryUtils.aiMatrix4ToMatrix4f(aiNode.mTransformation())
		);
		int childCount = aiNode.mNumChildren();
		PointerBuffer aiChildren = aiNode.mChildren();
		
		for( int i = 0; i < childCount; i++ ) {
			AINode aiChildNode = AINode.create(aiChildren.get(i));
			node.addChild(this.buildNodesTree(aiChildNode, node));
		}
		
		return node;
	}
	
	private Matrix4f buildTransformMatrix(AINodeAnim aiNodeAnim, int frameIndex) {
		AIVectorKey.Buffer positionKeys = aiNodeAnim.mPositionKeys();
		AIQuatKey.Buffer rotationKeys = aiNodeAnim.mRotationKeys();
		AIVectorKey.Buffer scalingKeys = aiNodeAnim.mScalingKeys();
		
		AIVectorKey aiVectorKey;
		AIVector3D vector;
		
        Matrix4f nodeTransform = new Matrix4f();
        int positionCount = aiNodeAnim.mNumPositionKeys();
        if (positionCount > 0) {
        	aiVectorKey = positionKeys.get(Math.min(positionCount - 1, frameIndex));
        	vector = aiVectorKey.mValue();
            nodeTransform.translate(vector.x(), vector.y(), vector.z());
        }
        
        int numRotations = aiNodeAnim.mNumRotationKeys();
        if (numRotations > 0) {
            AIQuatKey quatKey = rotationKeys.get(Math.min(numRotations - 1, frameIndex));
            AIQuaternion aiQuat = quatKey.mValue();
            Quaternionf quat = new Quaternionf(aiQuat.x(), aiQuat.y(), aiQuat.z(), aiQuat.w());
            nodeTransform.rotate(quat);
        }
        
        int numScalingKeys = aiNodeAnim.mNumScalingKeys();
        if (numScalingKeys > 0) {
        	aiVectorKey = scalingKeys.get(Math.min(numScalingKeys - 1, frameIndex));
        	vector = aiVectorKey.mValue();
            nodeTransform.scale(vector.x(), vector.y(), vector.z());
        }

        return nodeTransform;
	}
	
	private AINodeAnim findAIAnimationNode(AIAnimation aiAnimation, String nodeName) {
		AINodeAnim result = null;
		int animationNodeCount = aiAnimation.mNumChannels();
		PointerBuffer aiChannels = aiAnimation.mChannels();
		for( int i = 0; i < animationNodeCount; i++ ) {
			AINodeAnim aiNodeAnim = AINodeAnim.create(aiChannels.get(i));
			
			if( nodeName.equals(aiNodeAnim.mNodeName().dataString()) ) {
				result = aiNodeAnim;
				break;
			}
		}
		
		return result;
	}
	
	public void expectMesh(Mesh... meshes) {
		for( Mesh mesh : meshes ) {
			this.expectedMeshes.add(mesh);
		}
	}
	
	public void expectAnimation(Animation... animations) {
		for( Animation animation : animations ) {
			this.expectedAnimations.add(animation);
		}
	}
}

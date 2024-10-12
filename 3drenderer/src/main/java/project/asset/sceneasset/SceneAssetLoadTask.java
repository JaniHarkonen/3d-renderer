package project.asset.sceneasset;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
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

import project.Application;
import project.core.asset.IAsset;
import project.core.asset.IAssetData;
import project.core.asset.ILoadTask;
import project.core.asset.ISystem;
import project.shared.logger.Logger;
import project.utils.GeometryUtils;

public class SceneAssetLoadTask implements ILoadTask {

	public static final int DEFAULT_FLAGS = (
		Assimp.aiProcess_GenSmoothNormals | 
		//Assimp.aiProcess_JoinIdenticalVertices |
		Assimp.aiProcess_Triangulate | 
		Assimp.aiProcess_FixInfacingNormals | 
		Assimp.aiProcess_CalcTangentSpace | 
		Assimp.aiProcess_LimitBoneWeights
	);

	public static final int MAX_BONE_COUNT = 354;
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
	
	@Override
	public boolean load() {
		int preTransformVerticesFlag = (
			this.expectedAnimations.size() > 0 ? 
			0 : Assimp.aiProcess_PreTransformVertices
		);
		AIScene aiScene = Assimp.aiImportFile(
			this.assetPath, 
			this.importFlags | preTransformVerticesFlag
		);
		
		if( aiScene == null ) {
			Logger.get().error(this, "Failed to load scene from path: ", this.assetPath);
			return false;
		}
		
			//////////////////////////// Extract meshes ////////////////////////////
		int meshCount = aiScene.mNumMeshes();
		int expectedMeshCount = this.expectedMeshes.size();
		
		if( meshCount != expectedMeshCount ) {
			Logger.get().warn(
				this, 
				"Expected " + expectedMeshCount + " meshes, " + 
				"found " + meshCount + "!"
			);
		}
		
		List<Bone> boneList = new ArrayList<>();
		PointerBuffer aiMeshBuffer = aiScene.mMeshes();
		int s = Math.min(meshCount, expectedMeshCount);
		for( int i = 0; i < s; i++ ) {
			AIMesh aiMesh = AIMesh.create(aiMeshBuffer.get(i));
			Vector3f[] normals = GeometryUtils.aiVector3DBufferToVector3fArray(aiMesh.mNormals());
			Vector3f[] tangents = GeometryUtils.aiVector3DBufferToVector3fArray(aiMesh.mTangents());
			Vector3f[] bitangents = GeometryUtils.aiVector3DBufferToVector3fArray(aiMesh.mBitangents());
			Vector2f[] UVs = GeometryUtils.aiVector3DBufferToVector2fArray(aiMesh.mTextureCoords(0));

				// By default, set tangents array should be the same length as the normals array
			if( tangents.length == 0 ) {
				tangents = new Vector3f[normals.length];
				Arrays.fill(tangents, new Vector3f(0.0f));
				Logger.get().warn(this, "Scene asset contains no tangents!");
			}
			
				// By default, set bitangents array should be the same length as the normals array
			if( bitangents.length == 0 ) {
				bitangents = new Vector3f[normals.length];
				Arrays.fill(bitangents, new Vector3f(0.0f));
				Logger.get().warn(this, "Scene asset contains no bitangents!");
			}
			
				// Fix UV-coordinates
			for( Vector2f uv : UVs ) {
				uv.y = 1 - uv.y;
			}
			
				// Extract indices
			List<Mesh.Face> faces = new ArrayList<>();
			int faceCount = aiMesh.mNumFaces();
			AIFace.Buffer aiFaceBuffer = aiMesh.mFaces();
			
			for( int j = 0; j < faceCount; j++ ) {
				AIFace aiFace = aiFaceBuffer.get(j);
				IntBuffer indexBuffer = aiFace.mIndices();
				int[] indices = new int[Mesh.Face.INDICES_PER_FACE];
				int index = 0;
				while( indexBuffer.remaining() > 0 ) {
					indices[index++] = indexBuffer.get();
				}
				
				faces.add(new Mesh.Face(indices));
			}
			
				// Extract vertices and the bones that affect them
			Map<Integer, List<VertexWeight>> weightSet = this.generateWeightSet(boneList, aiMesh.mBones());
			int vertexCount = aiMesh.mNumVertices();
			int[] boneIDs = new int[vertexCount * MAX_WEIGHT_COUNT];
			float[] weights = new float[vertexCount * MAX_WEIGHT_COUNT];
			AIVector3D.Buffer buffer = aiMesh.mVertices();
			Vector3f[] vertices = new Vector3f[buffer.remaining()];
			
			for( int j = 0; buffer.remaining() > 0; j++ ) {
				AIVector3D aiVector = buffer.get();
				vertices[j] = new Vector3f(aiVector.x(), aiVector.y(), aiVector.z());
				this.extractBoneIndicesAndWeights(weights, boneIDs, weightSet, j);
			}
			
				// Package mesh data and send to asset manager
			Mesh.Data data = new Mesh.Data();
			data.targetMesh = this.expectedMeshes.get(i);
			data.vertices = vertices; 
			data.normals = normals; 
			data.tangents = tangents; 
			data.bitangents = bitangents;
			data.UVs = UVs;
			data.faces = faces.toArray(new Mesh.Face[faces.size()]);
			data.weights = weights;
			data.boneIDs = boneIDs;
			this.notifyAssetManager(data.targetMesh, data, Application.getApp().getRenderer());
		}
		
			////////////////////////////Extract animations ////////////////////////////
		int animationCount = aiScene.mNumAnimations();
		int expectedAnimationCount = this.expectedAnimations.size();
		
			// Animation count mismatch
		if( animationCount != expectedAnimationCount ) {
			Logger.get().warn(
				this, 
				"Expected " + expectedAnimationCount + " animations, " + 
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
				this.loadAnimation(
					AIAnimation.create(aiAnimations.get(i)), 
					this.expectedAnimations.get(i), 
					boneList, 
					rootNode, 
					globalInverseTransform
				);
			}
		}
		
		Assimp.aiReleaseImport(aiScene);
		
		return true;
	}
	
	private void notifyAssetManager(IAsset asset, IAssetData data, ISystem system) {
		Application.getApp().getAssetManager().notifyResult(
			asset, data, system
		);
	}
	
	private Map<Integer, List<VertexWeight>> generateWeightSet(
		List<Bone> boneList, PointerBuffer aiBoneBuffer
	) {
		Map<Integer, List<VertexWeight>> weightSet = new HashMap<>();
		
		if( aiBoneBuffer == null ) {
			return weightSet;
		}
		
		while( aiBoneBuffer.remaining() > 0 ) {
			AIBone aiBone = AIBone.create(aiBoneBuffer.get());
			int boneID = boneList.size();
			Bone bone = new Bone(
				boneID, 
				aiBone.mName().dataString(), 
				GeometryUtils.aiMatrix4ToMatrix4f(aiBone.mOffsetMatrix())
			);
			boneList.add(bone);
			
			int weightCount = aiBone.mNumWeights();
			AIVertexWeight.Buffer aiWeights = aiBone.mWeights();
			for( int k = 0; k < weightCount; k++ ) {
				AIVertexWeight aiWeight = aiWeights.get(k);
				VertexWeight weight = new VertexWeight(
					bone.getID(), aiWeight.mVertexId(), aiWeight.mWeight()
				);
				List<VertexWeight> weightList = weightSet.get(weight.getVertexID());
				
				if( weightList == null ) {
					weightList = new ArrayList<>();
					weightSet.put(weight.getVertexID(), weightList);
				}
				
				weightList.add(weight);
			}
		}
		
		aiBoneBuffer.flip(); // Reset the buffer cursor
		return weightSet;
	}
	
	private void extractBoneIndicesAndWeights(
		float[] weights, int[] boneIDs, Map<Integer, List<VertexWeight>> weightSet, int vertexIndex
	) {
		List<VertexWeight> weightList = weightSet.get(vertexIndex);
		int weightCount = (weightList != null) ? weightList.size() : 0;
		for( int j = 0; j < MAX_WEIGHT_COUNT; j++ ) {
			int index = vertexIndex * MAX_WEIGHT_COUNT + j;
			if( j < weightCount ) {
				VertexWeight weight = weightList.get(j);
				weights[index] = weight.getWeight();
				boneIDs[index] = weight.getBoneID();
			} else {
				weights[index] = 0.0f;
				boneIDs[index] = 0;
			}
		}
	}
	
	private void loadAnimation(
		AIAnimation aiAnimation, 
		Animation targetAnimation, 
		List<Bone> boneList, 
		Node rootNode, 
		Matrix4f globalInverseTransform
	) {
			// Build frame transforms for each key frame of the animation
		Animation.Frame[] frames = new Animation.Frame[targetAnimation.getExpectedFrameCount()];
		for( int i = 0; i < targetAnimation.getExpectedFrameCount(); i++ ) {
			Matrix4f[] boneTransforms = new Matrix4f[MAX_BONE_COUNT];
			Arrays.fill(boneTransforms, IDENTITY_MATRIX);
			Animation.Frame frame = new Animation.Frame(boneTransforms);
			this.buildFrameTransforms(
				aiAnimation, 
				boneList, 
				frame, 
				i, 
				rootNode, 
				rootNode.getNodeTransform(), 
				globalInverseTransform
			);
			frames[i] = frame;
		}
		
		Animation.Data animationData = new Animation.Data();
		animationData.targetAnimation = targetAnimation;
		animationData.duration = (float) aiAnimation.mDuration();
		animationData.frames = frames;
		this.notifyAssetManager(animationData.targetAnimation, animationData, null);
	}
	
	private void buildFrameTransforms(
		AIAnimation aiAnimation, 
		List<Bone> boneList, 
		Animation.Frame frame, 
		int frameIndex, 
		Node node, 
		Matrix4f parentTransform, 
		Matrix4f globalInverseTransform
	) {
		String nodeName = node.getName();
		AINodeAnim aiNodeAnim = this.findAIAnimationNode(aiAnimation, nodeName);
		Matrix4f nodeTransform = node.getNodeTransform();
		
			// Build node transform matrix if this node is associated with an animation node
		if( aiNodeAnim != null ) {
			nodeTransform = this.buildNodeTransform(aiNodeAnim, frameIndex);
		}
		
			// Apply node's transform to each Bone of the Assimp scene
		Matrix4f nodeGlobalTransform = new Matrix4f(parentTransform).mul(nodeTransform);
		for( Bone bone : boneList ) {
			if( bone.getName().equals(nodeName) ) {
				Matrix4f boneTransform = new Matrix4f(globalInverseTransform)
				.mul(nodeGlobalTransform)
				.mul(bone.getOffsetTransform());
				frame.setBoneTransform(bone.getID(), boneTransform);
			}
		}
		
			// Recurse through child nodes
		for( Node childNode : node.getChildren() ) {
			this.buildFrameTransforms(
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
		for( int i = 0; i < childCount; i++ ) {
			node.addChild(this.buildNodesTree(AINode.create(aiNode.mChildren().get(i)), node));
		}
		
		return node;
	}
	
	private Matrix4f buildNodeTransform(AINodeAnim aiNodeAnim, int frameIndex) {
        Matrix4f nodeTransform = new Matrix4f();
        
        	// Apply frame translation
        int positionCount = aiNodeAnim.mNumPositionKeys();
        if( positionCount > 0 ) {
        	AIVectorKey.Buffer positionKeys = aiNodeAnim.mPositionKeys();
        	AIVector3D aiTranslation = positionKeys.get(Math.min(positionCount - 1, frameIndex)).mValue();
            nodeTransform.translate(aiTranslation.x(), aiTranslation.y(), aiTranslation.z());
        }
        
        	// Apply frame rotation
        int rotationKeyCount = aiNodeAnim.mNumRotationKeys();
        if( rotationKeyCount > 0 ) {
        	AIQuatKey.Buffer rotationKeys = aiNodeAnim.mRotationKeys();
            AIQuaternion aiQuat = rotationKeys.get(Math.min(rotationKeyCount - 1, frameIndex)).mValue();
            Quaternionf quat = new Quaternionf(aiQuat.x(), aiQuat.y(), aiQuat.z(), aiQuat.w());
            nodeTransform.rotate(quat);
        }
        
        	// Apply frame scaling
        int scalingKeyCount = aiNodeAnim.mNumScalingKeys();
        if( scalingKeyCount > 0 ) {
        	AIVectorKey.Buffer scalingKeys = aiNodeAnim.mScalingKeys();
        	AIVector3D aiScaling = scalingKeys.get(Math.min(scalingKeyCount - 1, frameIndex)).mValue();
            nodeTransform.scale(aiScaling.x(), aiScaling.y(), aiScaling.z());
        }

        return nodeTransform;
	}
	
	private AINodeAnim findAIAnimationNode(AIAnimation aiAnimation, String nodeName) {
		int animationNodeCount = aiAnimation.mNumChannels();
		PointerBuffer aiChannels = aiAnimation.mChannels();
		
		for( int i = 0; i < animationNodeCount; i++ ) {
			AINodeAnim aiNodeAnim = AINodeAnim.create(aiChannels.get(i));
			if( nodeName.equals(aiNodeAnim.mNodeName().dataString()) ) {
				return aiNodeAnim;
			}
		}
		
		return null;
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
	
	@Override
	public List<IAsset> getTargetAssets() {
		List<IAsset> targetAssets = new ArrayList<>();
		for( IAsset asset : this.expectedMeshes ) {
			targetAssets.add(asset);
		}
		for( IAsset asset : this.expectedAnimations ) {
			targetAssets.add(asset);
		}
		return targetAssets;
	}
}

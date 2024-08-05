package project.asset;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
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
import project.utils.DebugUtils;
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
			DebugUtils.log(this, "ERROR: Failed to load scene from path: ", this.assetPath);
			return false;
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
			Vector3f[] vertices = GeometryUtils.aiVector3DBufferToVector3fArray(aiMesh.mVertices());
			Vector3f[] normals = GeometryUtils.aiVector3DBufferToVector3fArray(aiMesh.mNormals());
			Vector3f[] tangents = GeometryUtils.aiVector3DBufferToVector3fArray(aiMesh.mTangents());
			Vector3f[] bitangents = GeometryUtils.aiVector3DBufferToVector3fArray(aiMesh.mBitangents());
			Vector3f[] UVs = GeometryUtils.aiVector3DBufferToVector3fArray(aiMesh.mTextureCoords(0));

				// By default, set tangents array should be the same length as the normals array
			if( tangents.length == 0 ) {
				tangents = new Vector3f[normals.length];
				Arrays.fill(tangents, new Vector3f(0.0f));
				DebugUtils.log(this, "WARNING: Scene asset contains no tangents!");
			}
			
				// By default, set bitangents array should be the same length as the normals array
			if( bitangents.length == 0 ) {
				bitangents = new Vector3f[normals.length];
				Arrays.fill(bitangents, new Vector3f(0.0f));
				DebugUtils.log(this, "WARNING: Scene asset contains no bitangents!");
			}
			
				// Fix UV-coordinates
			for( Vector3f uv : UVs ) {
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
			
			Mesh.Data data = new Mesh.Data();
			data.targetMesh = this.expectedMeshes.get(i);
			data.vertices = vertices; 
			data.normals = normals; 
			data.tangents = tangents; 
			data.bitangents = bitangents;
			data.UVs = UVs;
			data.faces = faces.toArray(new Mesh.Face[faces.size()]);
			data.animationMeshData = this.processBones(aiMesh, boneList);
			
			Application.getApp().getAssetManager().notifyResult(
				data.targetMesh, 
				data,
				Application.getApp().getRenderer()
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
			this.processAnimations(aiScene, boneList, rootNode, globalInverseTransform);
		}
		
		Assimp.aiReleaseImport(aiScene);
		
		return true;
	}
	
	private AnimationMeshData processBones(AIMesh aiMesh, List<Bone> boneList) {
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
				List<VertexWeight> weightList = weightSet.get(weight.getVertexID());
				
				if( weightList == null ) {
					weightList = new ArrayList<>();
					weightSet.put(weight.getVertexID(), weightList);
				}
				
				weightList.add(weight);
			}
		}
		
		int vertexCount = aiMesh.mNumVertices();
		for( int j = 0; j < vertexCount; j++ ) {
			List<VertexWeight> weightList = weightSet.get(j);
			int weightCount = (weightList != null) ? weightList.size() : 0;
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
		
		return new AnimationMeshData(finalWeights, finalBoneIDs);
	}
	
	private void processAnimations(AIScene aiScene, List<Bone> boneList, Node rootNode, Matrix4f globalInverseTransform) {
		PointerBuffer aiAnimations = aiScene.mAnimations();
		int animationCount = Math.min(this.expectedAnimations.size(), aiScene.mNumAnimations());
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
				Arrays.fill(boneTransforms, IDENTITY_MATRIX);
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
			if( bone.getName().equals(nodeName) ) {
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

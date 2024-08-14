#version 460

layout (location=0) in vec3 position;
layout (location=1) in vec3 normal;
layout (location=2) in vec3 tangent;
layout (location=3) in vec3 bitangent;
layout (location=4) in vec2 textureCoordinate;
layout (location=5) in vec4 boneWeights;
layout (location=6) in ivec4 boneIndices;

const int MAX_WEIGHTS = 4;
const int MAX_BONES = 150;

out vec3 outNormal;
out vec3 outTangent;
out vec3 outBitangent;
out vec2 outTextureCoordinate;
out vec3 outViewPosition;
out vec4 outWorldPosition;

uniform mat4 uProjection;
uniform mat4 uCameraTransform;
uniform mat4 uObjectTransform;
uniform mat4 uBoneMatrices[MAX_BONES];

void main()
{
    vec4 fixedPosition = vec4(0, 0, 0, 0);
    vec4 fixedNormal = vec4(0, 0, 0, 0);
    vec4 fixedTangent = vec4(0, 0, 0, 0);
    vec4 fixedBitangent = vec4(0, 0, 0, 0);
    
    vec4 defaultPosition = vec4(position, 1.0);
    vec4 defaultNormal = vec4(normal, 0.0);
    vec4 defaultTangent = vec4(tangent, 0.0);
    vec4 defaultBitangent = vec4(bitangent, 0.0);

    int activeWeightCount = 0;
    for( int i = 0; i < MAX_WEIGHTS; i++ ) {
        float weight = boneWeights[i];
        if( weight > 0 ) {
            activeWeightCount++;
            int boneIndex = boneIndices[i];
            
            fixedPosition += weight * uBoneMatrices[boneIndex] * defaultPosition;
            fixedNormal += weight * uBoneMatrices[boneIndex] * defaultNormal;
            fixedTangent += weight * uBoneMatrices[boneIndex] * defaultTangent;
            fixedBitangent += weight * uBoneMatrices[boneIndex] * defaultBitangent;
        }
    }

    if( activeWeightCount == 0 ) {
        fixedPosition = defaultPosition;
        fixedNormal = defaultNormal;
        fixedTangent = defaultTangent;
        fixedBitangent = defaultBitangent;
    }

    mat4 modelViewMatrix = uCameraTransform * uObjectTransform;
    vec4 mvPosition = modelViewMatrix * fixedPosition;
    gl_Position = uProjection * mvPosition;
    outNormal = normalize(modelViewMatrix * fixedNormal).xyz;
    outTangent = normalize(modelViewMatrix * fixedTangent).xyz;
    outBitangent = normalize(modelViewMatrix * fixedBitangent).xyz;
    outViewPosition  = mvPosition.xyz;
    outWorldPosition = uObjectTransform * fixedPosition;
    outTextureCoordinate = textureCoordinate;
}

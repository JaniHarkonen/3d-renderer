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

uniform mat4 uObjectTransform;
uniform mat4 uLightView;
uniform mat4 uBoneMatrices[MAX_BONES];

void main()
{
    vec4 fixedPosition = vec4(0, 0, 0, 0);
    vec4 defaultPosition = vec4(position, 1.0);
    int activeWeightCount = 0;
    for( int i = 0; i < MAX_WEIGHTS; i++ ) {
        float weight = boneWeights[i];
        if( weight > 0 ) {
            activeWeightCount++;

            int boneIndex = boneIndices[i];
            fixedPosition += weight * uBoneMatrices[boneIndex] * defaultPosition;
        }
    }
    
    if( activeWeightCount == 0 ) {
        fixedPosition = defaultPosition;
    }

    gl_Position = uLightView * uObjectTransform * fixedPosition;
}

#version 460

const int MAX_WEIGHTS = 4;
const int MAX_BONES = 150;

layout (location=0) in vec3 position;
layout (location=1) in vec3 normal;
layout (location=2) in vec3 tangent;
layout (location=3) in vec3 bitangent;
layout (location=4) in vec2 textureCoordinate;
layout (location=5) in vec4 boneWeights;
layout (location=6) in ivec4 boneIndices;

uniform mat4 uObjectTransform; //modelMatrix;
uniform mat4 uLightView;//projViewMatrix;
uniform mat4 uBoneMatrices[MAX_BONES]; //uniform mat4 bonesMatrices[MAX_BONES];

void main()
{
    vec4 initPos = vec4(position, 1.0);
    /*vec4 initPos = vec4(0, 0, 0, 0);
    int count = 0;
    for (int i = 0; i < MAX_WEIGHTS; i++) {
        float weight = boneWeights[i];
        if (weight > 0) {
            count++;
            int boneIndex = boneIndices[i];
            vec4 tmpPos = uBoneMatrices[boneIndex] * vec4(position, 1.0);
            initPos += weight * tmpPos;
        }
    }
    if (count == 0) {
        initPos = vec4(position, 1.0);
    }*/

    gl_Position = uLightView * uObjectTransform * initPos;
}

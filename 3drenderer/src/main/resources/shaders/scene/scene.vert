#version 460

layout (location=0) in vec3 position;
layout (location=1) in vec3 normal;
layout (location=2) in vec3 tangent;
layout (location=3) in vec3 bitangent;
layout (location=4) in vec2 textureCoordinate;

out vec3 outNormal;
out vec3 outTangent;
out vec3 outBitangent;
out vec2 outTextureCoordinate;
out vec3 outViewPosition;
out vec4 outWorldPosition;

uniform mat4 uProjection; //uniform mat4 projectionMatrix;
uniform mat4 uCameraTransform; //uniform mat4 viewMatrix;
uniform mat4 uObjectTransform; //uniform mat4 modelMatrix;

void main()
{
    mat4 modelViewMatrix = uCameraTransform * uObjectTransform;
    vec4 mvPosition = modelViewMatrix * vec4(position, 1.0);
    gl_Position = uProjection * mvPosition;
    outNormal = normalize(modelViewMatrix * vec4(normal, 0.0)).xyz;
    outTangent = normalize(modelViewMatrix * vec4(tangent, 0)).xyz;
    outBitangent = normalize(modelViewMatrix * vec4(bitangent, 0)).xyz;
    outViewPosition  = mvPosition.xyz;
    outWorldPosition = uObjectTransform * vec4(position, 1.0);
    outTextureCoordinate = textureCoordinate;
}
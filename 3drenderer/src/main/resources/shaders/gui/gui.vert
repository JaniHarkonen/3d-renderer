#version 460

layout (location=0) in vec3 position;
layout (location=1) in vec2 normal;
layout (location=2) in vec3 tangent;
layout (location=3) in vec3 bitangent;
layout (location=4) in vec2 textureCoordinate;
layout (location=5) in vec4 boneWeights;
layout (location=6) in ivec4 boneIndices;

out vec2 outTextureCoordinate;

uniform mat4 uProjection;
uniform mat4 uObjectTransform;

void main()
{
  gl_Position = uProjection * (uObjectTransform * vec4(position, 1.0));
  outTextureCoordinate = textureCoordinate;
}

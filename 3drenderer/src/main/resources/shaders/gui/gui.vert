#version 460

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 normal;
layout (location = 2) in vec2 textureCoordinate;

out vec2 outTextureCoordinate;

uniform mat4 uProjection;
uniform mat4 uObjectTransform;

void main()
{
  gl_Position = uProjection * (uObjectTransform * vec4(position, 1.0));
  outTextureCoordinate = textureCoordinate;
}


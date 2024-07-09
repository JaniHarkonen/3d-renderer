#version 460

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 textureCoordinate;

out vec2 outTextureCoordinate;

uniform mat4 projection;

void main()
{
    gl_Position = projection * vec4(position, 1.0);
    outTextureCoordinate = textureCoordinate;
}

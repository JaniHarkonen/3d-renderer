#version 460

in vec2 outTextureCoordinate;

out vec4 fragColor;

uniform sampler2D diffuseSampler;

void main()
{
    fragColor = texture(diffuseSampler, outTextureCoordinate);
}

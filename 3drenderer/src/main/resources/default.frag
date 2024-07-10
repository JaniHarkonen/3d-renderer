#version 460

in vec2 outTextureCoordinate;

out vec4 fragColor;

uniform sampler2D uDiffuseSampler;

void main()
{
    fragColor = texture(uDiffuseSampler, outTextureCoordinate);
}

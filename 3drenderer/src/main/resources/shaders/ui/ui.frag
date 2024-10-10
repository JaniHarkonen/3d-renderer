#version 460

in vec2 outTextureCoordinate;

out vec4 fragColor;

uniform sampler2D uDiffuseSampler;
uniform vec4 uColor;
uniform int uHasTexture;

void main() 
{
  if( uHasTexture == 1 ) {
    fragColor = texture(uDiffuseSampler, outTextureCoordinate) * uColor;
  } else {
    fragColor = uColor;
  }
}

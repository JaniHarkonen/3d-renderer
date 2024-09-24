#version 460

in vec2 outTextureCoordinate;

out vec4 fragColor;

uniform sampler2D uDiffuseSampler;
uniform vec4 uPrimaryColor;
uniform int uHasTexture;

void main() 
{
  if( uHasTexture == 1 ) {
    fragColor = texture(uDiffuseSampler, outTextureCoordinate) * uPrimaryColor;
  } else {
    fragColor = uPrimaryColor;
  }
}

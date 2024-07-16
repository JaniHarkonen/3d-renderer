/*#version 460

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 normal;
layout (location = 2) in vec2 textureCoordinate;

out vec2 outTextureCoordinate;

uniform mat4 uProjection;
uniform mat4 uCameraTransform;
uniform mat4 uObjectTransform;

void main()
{
    gl_Position = uProjection * uCameraTransform * uObjectTransform * vec4(position, 1.0);
    outTextureCoordinate = textureCoordinate;
}
*/

#version 460

layout (location=0) in vec3 position;
layout (location=1) in vec3 normal;
layout (location=2) in vec2 textureCoordinate;

out vec3 outPosition;
out vec3 outNormal;
out vec2 outTextureCoordinate;

uniform mat4 uProjection; //uniform mat4 projectionMatrix;
uniform mat4 uCameraTransform; //uniform mat4 viewMatrix;
uniform mat4 uObjectTransform; //uniform mat4 modelMatrix;

void main()
{
    /*
    mat4 modelViewMatrix = viewMatrix * modelMatrix;
    vec4 mvPosition =  modelViewMatrix * vec4(position, 1.0);
    gl_Position = projectionMatrix * mvPosition;
    outPosition = mvPosition.xyz;
    outNormal = normalize(modelViewMatrix * vec4(normal, 0.0)).xyz;
    outTextCoord = texCoord;
    */

    mat4 modelViewMatrix = uCameraTransform * uObjectTransform;
    vec4 mvPosition =  modelViewMatrix * vec4(position, 1.0);
    gl_Position = uProjection * mvPosition;
    outPosition = mvPosition.xyz;
    outNormal = normalize(modelViewMatrix * vec4(normal, 0.0)).xyz;
    outTextureCoordinate = textureCoordinate;
}
#version 460

const int MAX_POINT_LIGHTS = 5;
//const int MAX_SPOT_LIGHTS = 5; // UNUSED AS OF NOW
const float SPECULAR_POWER = 10;

const float BIAS = 0.0005;
const float SHADOW_FACTOR = 0.75;
const int NUM_CASCADES = 3;

/*
    // UNUSED AS OF NOW
struct SpotLight
{
    PointLight pl;
    vec3 conedir;
    float cutoff;
};

    // UNUSED AS OF NOW
struct DirLight
{
    vec3 color;
    vec3 direction;
    float intensity;
};
*/

    // Struct definitions
struct Attenuation
{
    float constant;
    float linear;
    float exponent;
};

struct Material
{
    vec4 ambient;
    vec4 diffuse;
    vec4 specular;
    float reflectance;
    int hasNormalMap;
};

struct AmbientLight
{
    float factor;
    vec3 color;
};

struct PointLight 
{
    vec3 position;
    vec3 color;
    float intensity;
    Attenuation attenuation;
};

struct CascadeShadow {
    mat4 lightView;
    float splitDistance;
};

in vec3 outNormal;
in vec3 outTangent;
in vec3 outBitangent;
in vec2 outTextureCoordinate;
in vec3 outViewPosition;
in vec4 outWorldPosition;

out vec4 fragColor;

uniform sampler2D uDiffuseSampler;
uniform sampler2D uNormalSampler;
uniform sampler2D uShadowMap[NUM_CASCADES];
uniform Material uMaterial;
uniform AmbientLight uAmbientLight;
uniform PointLight uPointLights[MAX_POINT_LIGHTS];
uniform CascadeShadow uCascadeShadows[NUM_CASCADES];
uniform int uDebugShowShadowCascades;
// uniform SpotLight spotLights[MAX_SPOT_LIGHTS]; // UNUSED AS OF NOW
//uniform DirLight dirLight; // UNUSED AS OF NOW

vec4 calculateAmbientLight(AmbientLight ambientLight, vec4 ambient) {
    return vec4(ambientLight.factor * ambientLight.color, 1) * ambient;
}

vec4 calculateLightColor(
    vec4 diffuse, 
    vec4 specular, 
    vec3 lightColor, 
    float lightIntensity, 
    vec3 position, 
    vec3 toLightDirection, 
    vec3 normal
) {
        // Diffuse Light
    float diffuseFactor = max(dot(normal, toLightDirection), 0.0);
    vec4 diffuseColor = diffuse * vec4(lightColor, 1.0) * lightIntensity * diffuseFactor;

        // Specular Light
    vec3 lightReflection = normalize(reflect(-toLightDirection, normal));
    vec3 cameraDirection = normalize(-position);
    float specularFactor = max(dot(cameraDirection, lightReflection), 0.0);
    specularFactor = pow(specularFactor, SPECULAR_POWER);
    vec4 specColor = 
        specular * lightIntensity  * specularFactor * uMaterial.reflectance * vec4(lightColor, 1.0);

    return (diffuseColor + specColor);
}

vec4 calculatePointLight(vec4 diffuse, vec4 specular, PointLight light, vec3 position, vec3 normal) {
    vec3 lightDirection = light.position - position;
    vec3 toLightDirection  = normalize(lightDirection);
    vec4 lightColor = calculateLightColor(
        diffuse, specular, light.color, light.intensity, position, toLightDirection, normal
    );

        // Apply Attenuation
    float distanceToLight = length(lightDirection);
    float attenuationInv = 
        light.attenuation.constant + 
        light.attenuation.linear * 
        distanceToLight + 
        light.attenuation.exponent * 
        distanceToLight * 
        distanceToLight;

    return lightColor / attenuationInv;
}

/*
    // UNUSED AS OF NOW AND NOT INTEGRATED
vec4 calcSpotLight(vec4 diffuse, vec4 specular, SpotLight light, vec3 position, vec3 normal) {
    vec3 light_direction = light.pl.position - position;
    vec3 to_light_dir  = normalize(light_direction);
    vec3 from_light_dir  = -to_light_dir;
    float spot_alfa = dot(from_light_dir, normalize(light.conedir));

    vec4 color = vec4(0, 0, 0, 0);

    if (spot_alfa > light.cutoff)
    {
        color = calculatePointLight(diffuse, specular, light.pl, position, normal);
        color *= (1.0 - (1.0 - spot_alfa)/(1.0 - light.cutoff));
    }
    return color;
}
*/

/*

    // UNUSED AS OF NOW AND NOT INTEGRATED
vec4 calcDirLight(vec4 diffuse, vec4 specular, DirLight light, vec3 position, vec3 normal) {
    return calculateLightColor(diffuse, specular, light.color, light.intensity, position, normalize(light.direction), normal);
}
*/

vec3 calculateNormal(vec3 normal, vec3 tangent, vec3 bitangent, vec2 textureCoordinates) {
    mat3 tbn = mat3(tangent, bitangent, normal);
    return normalize(tbn * normalize(texture(uNormalSampler, textureCoordinates).rgb * 2.0 - 1.0));
}

float projectShadow(vec4 shadowCoordinate, vec2 offset, int shadowMapIndex) {
    float shadow = 1.0;

    if( shadowCoordinate.z > -1.0 && shadowCoordinate.z < 1.0 ) {
        float dist = 0.0;
        dist = texture(uShadowMap[shadowMapIndex], vec2(shadowCoordinate.xy + offset)).r;
        if( shadowCoordinate.w > 0 && dist < shadowCoordinate.z - BIAS ) {
            shadow = SHADOW_FACTOR;
        }
    }

    return shadow;
}

float calculateShadow(vec4 worldPosition, int index) {
    vec4 shadowMapPosition = uCascadeShadows[index].lightView * worldPosition;
    float shadow = 1.0;
    vec4 shadowCoord = (shadowMapPosition / shadowMapPosition.w) * 0.5 + 0.5;
    shadow = projectShadow(shadowCoord, vec2(0, 0), index);
    return shadow;
}

void main()
{
    vec4 textureColor = texture(uDiffuseSampler, outTextureCoordinate);
    vec4 ambient = calculateAmbientLight(uAmbientLight, textureColor + uMaterial.ambient);
    vec4 diffuse = textureColor + uMaterial.diffuse;
    vec4 specular = textureColor + uMaterial.specular;
    vec3 normal = outNormal;

    if( uMaterial.hasNormalMap > 0 ) {
        normal = calculateNormal(outNormal, outTangent, outBitangent, outTextureCoordinate);
    }

    vec4 diffuseSpecularComposite = vec4(0.0, 0.0, 0.0, 0.0);//calcDirLight(diffuse, specular, dirLight, outPosition, normal); // Directional light calculated here UNUSED AS OF NOW

    int cascadeIndex = 0;
    for( int i = 0; i < NUM_CASCADES - 1; i++ ) {
        if( outViewPosition.z < uCascadeShadows[i].splitDistance ) {
            cascadeIndex = i + 1;
        }
    }

    float shadowFactor = calculateShadow(outWorldPosition, cascadeIndex);

    for( int i = 0; i < MAX_POINT_LIGHTS; i++ ) {
        if( uPointLights[i].intensity > 0 ) {
            //diffuseSpecularComposite += calculatePointLight(diffuse, specular, uPointLights[i], outPosition, normal);
            diffuseSpecularComposite += calculatePointLight(diffuse, specular, uPointLights[i], outViewPosition, normal);
        }
    }

    /*
        // UNUSED AS OF NOW
    for( int i = 0; i < MAX_SPOT_LIGHTS; i++ ) {
        if( spotLights[i].pl.intensity > 0 ) {
            diffuseSpecularComposite += calcSpotLight(diffuse, specular, spotLights[i], outPosition, normal);
        }
    }
    */

    fragColor = ambient + diffuseSpecularComposite;
    fragColor.rgb = fragColor.rgb * shadowFactor;

    if( uDebugShowShadowCascades == 1 ) {
        switch( cascadeIndex ) {
            case 0:
                fragColor.rgb *= vec3(1.0f, 0.25f, 0.25f);
                break;
            case 1:
                fragColor.rgb *= vec3(0.25f, 1.0f, 0.25f);
                break;
            case 2:
                fragColor.rgb *= vec3(0.25f, 0.25f, 1.0f);
                break;
            default :
                fragColor.rgb *= vec3(1.0f, 1.0f, 0.25f);
                break;
        }
    }
}

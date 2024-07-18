#version 460

const int MAX_POINT_LIGHTS = 5;
//const int MAX_SPOT_LIGHTS = 5; // UNUSED AS OF NOW
const float SPECULAR_POWER = 10;

const float BIAS = 0.0005;
const float SHADOW_FACTOR = 0.25;
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
    Attenuation att;
};

struct CascadeShadow {
    mat4 lightView;
    float splitDistance;
};

//in vec3 outPosition;
in vec3 outNormal;
in vec3 outTangent;
in vec3 outBitangent;
in vec2 outTextureCoordinate;//in vec2 outTextCoord;
in vec3 outViewPosition;
in vec4 outWorldPosition;

out vec4 fragColor;

uniform sampler2D uDiffuseSampler; //uniform sampler2D txtSampler;
uniform sampler2D uNormalSampler;
uniform sampler2D uShadowMap[NUM_CASCADES];
uniform Material uMaterial; //uniform Material material;
uniform AmbientLight uAmbientLight; //uniform AmbientLight ambientLight;
uniform PointLight uPointLights[MAX_POINT_LIGHTS]; //uniform PointLight pointLights[MAX_POINT_LIGHTS];
uniform CascadeShadow uCascadeShadows[NUM_CASCADES];
// uniform SpotLight spotLights[MAX_SPOT_LIGHTS]; // UNUSED AS OF NOW
//uniform DirLight dirLight; // UNUSED AS OF NOW

vec4 calcAmbient(AmbientLight ambientLight, vec4 ambient) {
    return vec4(ambientLight.factor * ambientLight.color, 1) * ambient;
}

vec4 calcLightColor(vec4 diffuse, vec4 specular, vec3 lightColor, float light_intensity, vec3 position, vec3 to_light_dir, vec3 normal) {
    vec4 diffuseColor = vec4(0, 0, 0, 1);
    vec4 specColor = vec4(0, 0, 0, 1);

    // Diffuse Light
    float diffuseFactor = max(dot(normal, to_light_dir), 0.0);
    diffuseColor = diffuse * vec4(lightColor, 1.0) * light_intensity * diffuseFactor;

    // Specular Light
    vec3 camera_direction = normalize(-position);
    vec3 from_light_dir = -to_light_dir;
    vec3 reflected_light = normalize(reflect(from_light_dir, normal));
    float specularFactor = max(dot(camera_direction, reflected_light), 0.0);
    specularFactor = pow(specularFactor, SPECULAR_POWER);
    specColor = specular * light_intensity  * specularFactor * uMaterial.reflectance * vec4(lightColor, 1.0);

    return (diffuseColor + specColor);
}

vec4 calcPointLight(vec4 diffuse, vec4 specular, PointLight light, vec3 position, vec3 normal) {
    vec3 light_direction = light.position - position;
    vec3 to_light_dir  = normalize(light_direction);
    vec4 light_color = calcLightColor(diffuse, specular, light.color, light.intensity, position, to_light_dir, normal);

    // Apply Attenuation
    float distance = length(light_direction);
    float attenuationInv = light.att.constant + light.att.linear * distance +
    light.att.exponent * distance * distance;
    return light_color / attenuationInv;
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
        color = calcPointLight(diffuse, specular, light.pl, position, normal);
        color *= (1.0 - (1.0 - spot_alfa)/(1.0 - light.cutoff));
    }
    return color;
}
*/

/*

    // UNUSED AS OF NOW AND NOT INTEGRATED
vec4 calcDirLight(vec4 diffuse, vec4 specular, DirLight light, vec3 position, vec3 normal) {
    return calcLightColor(diffuse, specular, light.color, light.intensity, position, normalize(light.direction), normal);
}
*/

vec3 calcNormal(vec3 normal, vec3 tangent, vec3 bitangent, vec2 textCoords) {
    mat3 TBN = mat3(tangent, bitangent, normal);
    vec3 newNormal = texture(uNormalSampler, textCoords).rgb;
    newNormal = normalize(newNormal * 2.0 - 1.0);
    newNormal = normalize(TBN * newNormal);
    return newNormal;
}

float textureProj(vec4 shadowCoord, vec2 offset, int idx) {
    float shadow = 1.0;

    if (shadowCoord.z > -1.0 && shadowCoord.z < 1.0) {
        float dist = 0.0;
        dist = texture(uShadowMap[idx], vec2(shadowCoord.xy + offset)).r;
        if (shadowCoord.w > 0 && dist < shadowCoord.z - BIAS) {
            shadow = SHADOW_FACTOR;
        }
    }
    return shadow;
}

float calcShadow(vec4 worldPosition, int idx) {
    vec4 shadowMapPosition = uCascadeShadows[idx].lightView * worldPosition;
    float shadow = 1.0;
    vec4 shadowCoord = (shadowMapPosition / shadowMapPosition.w) * 0.5 + 0.5;
    shadow = textureProj(shadowCoord, vec2(0, 0), idx);
    return shadow;
}

void main()
{
    vec4 text_color = texture(uDiffuseSampler, outTextureCoordinate);
    vec4 ambient = calcAmbient(uAmbientLight, text_color + uMaterial.ambient);
    vec4 diffuse = text_color + uMaterial.diffuse;
    vec4 specular = text_color + uMaterial.specular;

    vec3 normal = outNormal;
    if (uMaterial.hasNormalMap > 0) {
        normal = calcNormal(outNormal, outTangent, outBitangent, outTextureCoordinate);
    }

    vec4 diffuseSpecularComp = vec4(0.0, 0.0, 0.0, 0.0);//calcDirLight(diffuse, specular, dirLight, outPosition, normal); // Directional light calculated here UNUSED AS OF NOW

    int cascadeIndex = 0;
    for (int i=0; i<NUM_CASCADES - 1; i++) {
        if (outViewPosition.z < uCascadeShadows[i].splitDistance) {
            cascadeIndex = i + 1;
        }
    }

    float shadowFactor = calcShadow(outWorldPosition, cascadeIndex);

    for (int i=0; i<MAX_POINT_LIGHTS; i++) {
        if (uPointLights[i].intensity > 0) {
            //diffuseSpecularComp += calcPointLight(diffuse, specular, uPointLights[i], outPosition, normal);
            diffuseSpecularComp += calcPointLight(diffuse, specular, uPointLights[i], outViewPosition, normal);
        }
    }

    /*
        // UNUSED AS OF NOW
    for (int i=0; i<MAX_SPOT_LIGHTS; i++) {
        if (spotLights[i].pl.intensity > 0) {
            diffuseSpecularComp += calcSpotLight(diffuse, specular, spotLights[i], outPosition, normal);
        }
    }
    */

    fragColor = ambient + diffuseSpecularComp;
    fragColor.rgb = fragColor.rgb * shadowFactor;
}

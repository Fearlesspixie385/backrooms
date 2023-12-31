#version 330
//in vec3 Normal;
//in vec3 FragPos;

struct DirLight
{
    vec3 direction;
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};
uniform DirLight dirLight;

struct PointLight
{
    vec3 position;
    float constant;
    float linear;
    float quadratic;

    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};
//Array
#define NR_POINT_LIGHTS 7
uniform PointLight pointLights[NR_POINT_LIGHTS];

struct SpotLight{
    vec3 position;
    vec3 direction;

    float cutOff;
    float outerCutOff;

    vec3 ambient;
    vec3 diffuse;
    vec3 specular;

    float constant;
    float linear;
    float quadratic;
};
uniform SpotLight spotLight;

uniform vec4 uni_color;
in  vec2 outTexCoord;
out vec4 frag_color;

uniform vec3 viewPos;
uniform sampler2D texture_sampler;

in vec3 Normal;
in vec3 FragPos;

uniform int hasTexture;

vec3 CalcDirLight(DirLight light, vec3 normal, vec3 viewDir){
    vec3 lightDir = normalize(-light.direction);

    //diffuse
    float diff = max(dot(normal,lightDir),0.0);
    //specular
    vec3 reflectDir = reflect(-lightDir,normal);
    float spec = pow(max(dot(viewDir, reflectDir),0.0),64.0);

    vec3 ambient = light.ambient;
    vec3 diffuse = light.diffuse * diff;
    vec3 specular = light.specular * spec;
    return(ambient + diffuse + specular);
}
vec3 CalcPointLight(PointLight light, vec3 normal, vec3 fragPos, vec3 viewDir)
{
    vec3 lightDir = normalize(light.position - fragPos);
    //diffuse shading
    float diff = max(dot(normal, lightDir), 0.0);
    //specular shading
    vec3 reflectDir = reflect(-lightDir, normal);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0),3072.0);
    //attenuation
    float distance    = length(light.position - fragPos);
    float attenuation = 1.0 / (light.constant + light.linear * distance +
    light.quadratic * (distance * distance));
    //combine results
    vec3 ambient  = light.ambient;
    vec3 diffuse  = light.diffuse  * diff;
    vec3 specular = light.specular * spec;
    ambient  *= attenuation;
    diffuse  *= attenuation;
    specular *= attenuation;
    return (ambient + diffuse + specular);
}

vec3 CalcSpotLight(SpotLight light, vec3 normal, vec3 fragPos, vec3 viewDir)
{

    //diffuse shading
    vec3 lightDir = normalize(light.position - FragPos);
    float diff = max(dot(normal, lightDir), 0.0);

    //specular shading
    vec3 reflectDir = reflect(-lightDir, normal);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0),3072.0);

    //attenuation
    float distance    = length(light.position - FragPos);
    float attenuation = 1.0 / (light.constant + light.linear * distance +
    light.quadratic * (distance * distance));

    //spotlight intensity
    float theta     = dot(lightDir, normalize(-light.direction));
    float epsilon   = light.cutOff - light.outerCutOff;
    float intensity = clamp((theta - light.outerCutOff) / epsilon, 0.0, 1.0);

    //combine results
    vec3 ambient = light.ambient;
    vec3 diffuse = light.diffuse * diff;
    vec3 specular = light.specular * spec;
    ambient  *= attenuation;
    diffuse  *= attenuation * intensity;
    specular *= attenuation * intensity;
    return (ambient + diffuse + specular);
}

void main(){
    vec3 normal = normalize(Normal);
    vec3 viewDir = normalize (viewPos - FragPos);

    //Directional light
    vec3 result = CalcDirLight(dirLight, normal, viewDir);

    //Point lights
    for(int i = 0;i<NR_POINT_LIGHTS;i++){
        result += CalcPointLight(pointLights[i],normal,FragPos, viewDir);
    }
    //SpotLight
    result += CalcSpotLight(spotLight,normal,FragPos, viewDir);

    if (hasTexture == 1){
//        frag_color = vec4(result * texture(texture_sampler, outTexCoord),1.0);
        frag_color = vec4(result * vec3(uni_color),1.0)* texture(texture_sampler, outTexCoord);
    }
    else{
        frag_color = vec4(result * vec3(uni_color),1.0);
    }

//    frag_color = vec4(result * vec3(uni_color),1.0);
//    frag_color = texture(texture_sampler, outTexCoord)
//    frag_color = uni_color;
}
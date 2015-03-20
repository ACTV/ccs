#version 430

in vec2 tc;
//  interpolated inputs from the rasterizer 
in vec3 varyingNormalG; 
in vec3 varyingLightDirG; 
in vec3 varyingVertPosG; 
in vec3 varyingHalfVectorG;


out vec4 fragColor;

uniform mat4 mv_matrix;
uniform mat4 proj_matrix;
uniform sampler2D s;

struct PositionalLight 
{ vec4 ambient;  
vec4 diffuse;  
vec4 specular;  
vec3 position; 
}; 

struct Material 
{ vec4 ambient;   
 vec4 diffuse;   
 vec4 specular;    
float shininess; 
};

uniform vec4 globalAmbient; // global ambient light 
uniform PositionalLight light; // current positional light 
uniform Material material; // current material 
uniform mat4 normalMat;

void main(void)
{
        // light intensity values  
    vec4 lightAmb = light.ambient;  
    vec4 lightDiff = light.diffuse;  
    vec4 lightSpec = light.specular;  
     // material reflection coefficients  
    vec4 matAmb = material.ambient;    
    vec4 matDiff = material.diffuse;  
    vec4 matSpec = material.specular;  
    float matShine = material.shininess;   
     // compute the normalized light, normal, and eye direction vectors  
    vec3 L = normalize(varyingLightDirG); 
     vec3 N = normalize(varyingNormalG); 
     vec3 V = normalize(-varyingVertPosG);   
     // calculate half vector, or get interpolated one from vertex shader  
     //vec3 H = normalize(L+V); 
    vec3 H = varyingHalfVectorG; 
   
    fragColor = texture2D(s,tc) * 0.3f +  globalAmbient * matAmb  +  lightAmb * matAmb  + lightDiff * matDiff * max(dot(L,N),3)  + lightSpec * matSpec * pow(max(dot(H,N),4), matShine*0.7f);
};
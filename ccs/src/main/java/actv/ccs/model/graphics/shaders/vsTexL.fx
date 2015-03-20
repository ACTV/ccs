#version 430 core


in vec3 vertPos;
in vec2 texPos;
in vec4 vertNormal;


out vec3 varyingNormal; // eye-space vertex normal 
out vec3 varyingLightDir; // vector pointing to the light  
out vec3 varyingVertPos; // vertex position in eye space 
out vec3 varyingHalfVector; // half vector for specular highlight 
out vec2 tc;
out int count;

uniform mat4 mv_matrix;
uniform mat4 proj_matrix;
uniform mat4 normalMat;

//uniform sampler2D s;



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


void main(void)
{
    
    // get the light position   
    // (already transformed to Eye Coords by the application)  
    vec4 lightPosInEyeCoords = vec4(light.position,1.0f);    
    // convert the vertex position to Eye Coordinates  
    vec4 vertPosInEyeCoords = mv_matrix * vec4(vertPos,1.0);  
     // vertex position (interpolated by rasterizer)  
    varyingVertPos = vertPosInEyeCoords.xyz;           
    // vector from vertex to light (interpolated by rasterizer)  
    varyingLightDir = (lightPosInEyeCoords - vertPosInEyeCoords).xyz;  
     // vertex normal vector in eye space (interpolated by rasterizer)  
    varyingNormal = (normalMat * vertNormal).xyz;    
     // calculate the half vector (L+V) – or do it in fragment shader  
    varyingHalfVector = normalize(normalize(varyingLightDir) + normalize(-varyingVertPos)).xyz;

    gl_Position = proj_matrix * mv_matrix * vec4(vertPos,1.0);
    tc = texPos;
    count = gl_VertexID/3;
 } 
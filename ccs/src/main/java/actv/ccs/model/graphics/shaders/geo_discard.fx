#version 430  

layout (triangles) in;  


in vec3 varyingNormal[]; // pass through 
in vec3 varyingLightDir[]; 
in vec3 varyingVertPos[]; 
//in vec3 originalVertex[]; 
in vec3 varyingHalfVector[];
in vec2 tc[]; 
in int count[];  
out vec3 varyingNormalG; // pass through 
out vec3 varyingLightDirG;
 out vec3 varyingVertPosG; 
//out vec3 originalVertexG; 
out vec3 varyingHalfVectorG;
 out vec2 tcG;  


layout (triangle_strip) out; 
layout (max_vertices=3) out;  

void main (void) 
{
    
       if(mod(count[0],3) != 0)
       {

        for (int i = 0; i <= gl_in.length(); i++)   
        { 
            gl_Position = gl_in[i].gl_Position;    
            varyingNormalG = varyingNormal[i];    
            varyingLightDirG = varyingLightDir[i];   
            varyingVertPosG = varyingVertPos[i];    
            //originalVertexG = originalVertex[i];   
            varyingHalfVectorG = varyingHalfVector[i];   
            tcG = tc[i];
            EmitVertex();
        }  

        }
    
    EndPrimitive(); 
} 
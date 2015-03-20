#version 430 core 
in vec2 tc[];  // vertex attributes are now arrays 
out vec2 tcs_out[];  
uniform float uOuter02; // patch tesselation levels 
uniform float uOuter13; // (get from application) 
uniform float uInner0; 
uniform float uInner1;  

layout (vertices = 16) out;  


void main(void) { //  tessellation levels sent to the tessellator  
    


    gl_TessLevelOuter[0] = uOuter02; 
     gl_TessLevelOuter[2] = uOuter02; 
     gl_TessLevelOuter[1] = uOuter13;  
    gl_TessLevelOuter[3] = uOuter13;  
    gl_TessLevelInner[0] = uInner0; 
     gl_TessLevelInner[1] = uInner1;  
     // forward the position for this control point to the TES  
    gl_out[0].gl_Position =  gl_in[0].gl_Position;  // also forward the texture coordinates 
     tcs_out[0] = tc[0]; //GORDON!!!! my AMD card does not like varibles in there, only constants
}
//gl_InvocationID
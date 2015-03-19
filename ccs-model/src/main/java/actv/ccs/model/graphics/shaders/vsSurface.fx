#version 430 core


out vec2 tc;  // out vertex attributes are scalars 

uniform mat4 mvp;  

void main(void) 
{ 
    // cubic bezier patch is 16 control points  


    const vec4 vertices[] = vec4[] (vec4(-1.0, 0.5, -1.0, 1.0), vec4(-0.5, 0.5, -1.0, 1.0),     vec4( 0.5, 0.5, -1.0, 1.0), vec4( 1.0, 0.5, -1.0, 1.0),     vec4(-1.0, 0.0, -0.5, 1.0), vec4(-0.5, 0.0, -0.5, 1.0),     vec4( 0.5, 0.0, -0.5, 1.0), vec4( 1.0, 0.0, -0.5, 1.0),     vec4(-1.0, 0.0,  0.5, 1.0), vec4(-0.5, 0.0,  0.5, 1.0),     vec4( 0.5, 0.0,  0.5, 1.0), vec4( 1.0, 0.0,  0.5, 1.0),     vec4(-1.0, -0.5,  1.0, 1.0), vec4(-0.5, 0.3,  1.0, 1.0),     vec4( 0.5, 0.3,  1.0, 1.0), vec4( 1.0, 0.3,  1.0, 1.0));  
     // build terrain coordinates by moving to range [0,1]  


    tc = vec2((vertices[gl_VertexID].x + 1.0)/2.0,(vertices[gl_VertexID].z + 1.0)/2.0);  


     // send control points to the TCS 
     gl_Position = vertices[gl_VertexID]; 
} 
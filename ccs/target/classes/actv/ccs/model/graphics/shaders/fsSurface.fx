#version 430 core 

in vec2 tes_out; 
out vec4 color; 
layout (binding = 0) uniform sampler2D tex_color;  

void main(void) 
{ 
    color = texture2D(tex_color, tes_out); 
    //color = vec4(1.0,0.0,0.0,0.0); 
} 
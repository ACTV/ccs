#version 430
//get all the offsets and colors 

uniform mat4 mv_matrix;
uniform mat4 proj_matrix;

out vec4 vs_color;

out gl_PerVertex 
{ 
    vec4 gl_Position; 
}; 

void main(void)
{
    const vec4 vertices[6] = vec4[6](vec4(1.6,0.0,0.0,1.0),
                                     vec4(0,0,0,1.0),
                                     vec4(0.0,1.6,0.0,1.0),
                                     vec4(0,0,0,1.0),
                                     vec4(0.0,0.0,1.6,1.0),
                                     vec4(0,0,0,1.0)); //hard coded ver

    const vec4 colors[6] = vec4[6](  vec4(1.0,0.0,0.0,0.0),
                                     vec4(1.0,0.0,0.0,0.0),
                                     vec4(0.0,1.0,0.0,0.0),
                                     vec4(0.0,1.0,0.0,0.0),
                                     vec4(0.0,0.0,1.0,0.0),
                                     vec4(0.0,0.0,1.0,0.0)); //make an array color

    gl_Position = proj_matrix * mv_matrix * vertices[gl_VertexID] ; //offset all the hardcoded verts
    vs_color = colors[gl_VertexID]; // color gradiant if different colors
};
#version 430 core
in vec3 vertPos;
in vec2 texPos;
//in vec4 vertNormal;

out vec2 tc;



uniform mat4 mv_matrix;
uniform mat4 proj_matrix;
uniform sampler2D s;

out int count;
void main(void)
{
    gl_Position = proj_matrix * mv_matrix * vec4(vertPos,1.0);
    tc = texPos;
    count = gl_VertexID/3;
 } 
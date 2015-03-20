#version 430

in vec2 tcG;

out vec4 fragColor;

uniform mat4 mv_matrix;
uniform mat4 proj_matrix;
uniform sampler2D s;


//in int count;
void main(void)
{
    gl_FragColor = texture2D(s,tcG);
};
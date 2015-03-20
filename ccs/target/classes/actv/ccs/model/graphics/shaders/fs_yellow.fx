#version 430
out vec4 color;

in VS_OUT
{
    vec4 color;
} fs_in;

void main(void)
{
    color = vec4(1.0,1.0,0.0,0.0); // simple print out all the color
};
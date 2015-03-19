#version 430
out vec4 color;

in VS_OUT
{
    vec4 color;
} fs_in;



void main(void)
{
    color = fs_in.color; // simple print out all the color
};
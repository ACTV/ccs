#version 430
in vec4 vs_color;
out vec4 color;
void main(void)
{
    color = vs_color; // simple print out all the color
};
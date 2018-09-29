attribute vec4 a_Position;
attribute vec4 a_TextureCoordinates;
varying vec2 v_TextureCoordinates;

uniform float texelWidthOffset;
uniform float texelHeightOffset;
uniform mat4 u_textureTransform;

void main()
{
    v_TextureCoordinates = (u_textureTransform * a_TextureCoordinates).xy;
    gl_Position = a_Position;
}
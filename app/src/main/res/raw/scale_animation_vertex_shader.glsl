attribute vec4 a_Position;
attribute vec2 a_TextureCoordinates;
varying vec2 v_TextureCoordinates;

uniform float texelWidthOffset;
uniform float texelHeightOffset;
uniform mat4 uMvpMatrix;

void main()
{
    v_TextureCoordinates = a_TextureCoordinates;
    gl_Position = uMvpMatrix * a_Position;
}
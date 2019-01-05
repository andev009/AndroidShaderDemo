precision mediump float;
uniform sampler2D u_TextureUnit;
varying vec2 v_TextureCoordinates;

uniform float texelWidthOffset;
uniform float texelHeightOffset;

const highp vec3 W = vec3(0.2125, 0.7154, 0.0721);
void main()
{
   gl_FragColor = texture2D(u_TextureUnit, v_TextureCoordinates);

   //gl_FragColor = vec4(vec3(1.0, 0.0, 0.0), 1.0);
}
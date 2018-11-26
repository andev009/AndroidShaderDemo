precision mediump float;
uniform sampler2D u_TextureUnit;
varying vec2 v_TextureCoordinates;

uniform float u_alpha;

void main()
{
   gl_FragColor = vec4(texture2D(u_TextureUnit, v_TextureCoordinates).rgb, u_alpha);
}
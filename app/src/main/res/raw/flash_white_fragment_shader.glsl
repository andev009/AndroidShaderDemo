precision mediump float;
uniform sampler2D u_TextureUnit;
varying vec2 v_TextureCoordinates;

uniform float texelWidthOffset;
uniform float texelHeightOffset;

uniform float uflashParam;
void main()
{
   vec4 fragColor = texture2D(u_TextureUnit, v_TextureCoordinates);

   gl_FragColor = vec4(fragColor.r + uflashParam,fragColor.g + uflashParam,fragColor.b + uflashParam,fragColor.a);
}
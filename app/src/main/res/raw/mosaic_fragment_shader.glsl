precision mediump float;
uniform sampler2D u_TextureUnit;
varying vec2 v_TextureCoordinates;

uniform float texelWidthOffset;
uniform float texelHeightOffset;

const float pixel = 30.0;

void main()
{
   //四边形
   vec2 uv  = v_TextureCoordinates.xy;
   float dx = pixel * texelWidthOffset;
   float dy = pixel * texelHeightOffset;
   vec2 coord = vec2(dx * floor(uv.x / dx), dy * floor(uv.y / dy));
   vec3 tc = texture2D(u_TextureUnit, coord).xyz;

   gl_FragColor = vec4(tc, 1.0);

}
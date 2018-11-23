precision mediump float;
uniform sampler2D u_TextureUnit;
varying vec2 v_TextureCoordinates;

uniform float uBurrParam;


float nrand(in float x, in float y)
{
  return fract(sin(dot(vec2(x, y), vec2(12.9898, 78.233))) * 43758.5453);
}

void main()
{
   float cx = v_TextureCoordinates.x;
   float cy = v_TextureCoordinates.y;

   float range = nrand(0, cy) * 2.0 - 1;

   float willOffset = step(0.5, abs(range));

   float offset = range * willOffset * 0.13 * uBurrParam;


   gl_FragColor = texture2D(u_TextureUnit, fract(vec2(cx + offset, cy)));
}
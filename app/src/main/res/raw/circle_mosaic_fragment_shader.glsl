precision mediump float;
uniform sampler2D u_TextureUnit;
varying vec2 v_TextureCoordinates;

uniform float texelWidthOffset;
uniform float texelHeightOffset;

const float pixel = 30.0;

void main()
{
   vec2 uv  = v_TextureCoordinates.xy;
   float dx = pixel * texelWidthOffset;
   float dy = pixel * texelHeightOffset;
   // 计算一个马赛克的中心坐标
   vec2 coord = vec2(dx * floor(uv.x / dx) + 0.5 * dx, dy * floor(uv.y / dy) + 0.5 * dy);
   // 计算当前像素距离所在马赛克中心的长度
   vec2 delXY = coord - uv;
   float delL = length(delXY);

   vec4 finalColor;
   if(delL < 0.5 * dx)
   {
      finalColor = texture2D(u_TextureUnit, coord);
   }
   else
   {
      finalColor = vec4(0., 0., 0., 1.);
   }

   gl_FragColor = finalColor;
}
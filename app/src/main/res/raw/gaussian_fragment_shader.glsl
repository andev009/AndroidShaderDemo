precision mediump float;

uniform sampler2D u_TextureUnit0;//texture

const lowp int GAUSSIAN_SAMPLES = 9;

varying highp vec2 v_TextureCoordinates;

varying highp vec2 blurCoordinates[GAUSSIAN_SAMPLES];

const highp vec3 W = vec3(0.2125, 0.7154, 0.0721);

void main()
{
  lowp vec3 sum = vec3(0.0);
  lowp vec4 fragColor=texture2D(u_TextureUnit0, v_TextureCoordinates);

  sum += texture2D(u_TextureUnit0, blurCoordinates[0]).rgb * 0.05;
  sum += texture2D(u_TextureUnit0, blurCoordinates[1]).rgb * 0.09;
  sum += texture2D(u_TextureUnit0, blurCoordinates[2]).rgb * 0.12;

  sum += texture2D(u_TextureUnit0, blurCoordinates[3]).rgb * 0.15;
  sum += texture2D(u_TextureUnit0, blurCoordinates[4]).rgb * 0.18;
  sum += texture2D(u_TextureUnit0, blurCoordinates[5]).rgb * 0.15;

  sum += texture2D(u_TextureUnit0, blurCoordinates[6]).rgb * 0.12;
  sum += texture2D(u_TextureUnit0, blurCoordinates[7]).rgb * 0.09;
  sum += texture2D(u_TextureUnit0, blurCoordinates[8]).rgb * 0.05;

  float luminance = dot(sum.rgb, W);
  gl_FragColor = vec4(vec3(luminance), fragColor.a);


//  gl_FragColor = vec4(sum,fragColor.a);

}
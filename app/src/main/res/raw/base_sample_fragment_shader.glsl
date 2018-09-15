precision mediump float;

uniform sampler2D u_TextureUnit0;//orgin
varying vec2 v_TextureCoordinates;

varying highp vec2 v_leftTextureCoordinate;
varying highp vec2 v_rightTextureCoordinate;
varying highp vec2 v_topTextureCoordinate;
varying highp vec2 v_bottomTextureCoordinate;

varying highp vec2 v_leftTopTextureCoordinate;//左上角
varying highp vec2 v_rightTopTextureCoordinate;//右上角
varying highp vec2 v_leftBottomTextureCoordinate;//左下角
varying highp vec2 v_rightBottomTextureCoordinate;//右上角


void main()
{
     mediump vec3 textureColor = texture2D(u_TextureUnit0, v_TextureCoordinates).rgb;

     mediump vec3 leftTextureColor = texture2D(u_TextureUnit0, v_leftTextureCoordinate).rgb;
     mediump vec3 rightTextureColor = texture2D(u_TextureUnit0, v_rightTextureCoordinate).rgb;
     mediump vec3 topTextureColor = texture2D(u_TextureUnit0, v_topTextureCoordinate).rgb;
     mediump vec3 bottomTextureColor = texture2D(u_TextureUnit0, v_bottomTextureCoordinate).rgb;

     mediump vec3 leftTopTextureColor = texture2D(u_TextureUnit0, v_leftTopTextureCoordinate).rgb;
     mediump vec3 rightTopTextureColor = texture2D(u_TextureUnit0, v_rightTopTextureCoordinate).rgb;
     mediump vec3 leftBottomTextureColor = texture2D(u_TextureUnit0, v_leftBottomTextureCoordinate).rgb;
     mediump vec3 rightBottomTextureColor = texture2D(u_TextureUnit0, v_rightBottomTextureCoordinate).rgb;


     gl_FragColor = vec4(textureColor, 1.0);
}
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

const highp vec3 W = vec3(0.2125, 0.7154, 0.0721);

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

      //求灰度值
     float leftl = dot(leftTextureColor.rgb, W);
     float rightl = dot(rightTextureColor.rgb, W);
     float topl = dot(topTextureColor.rgb, W);
     float bottoml = dot(bottomTextureColor.rgb, W);

     float leftTopl = dot(leftTopTextureColor.rgb, W);
     float rightTopl = dot(rightTopTextureColor.rgb, W);
     float leftBottoml = dot(leftBottomTextureColor.rgb, W);
     float rightBottoml = dot(rightBottomTextureColor.rgb, W);

     //水平方向
     float hColor = leftl * -2.0 + rightl * 2.0 +
                    topl * 0.0 + bottoml * 0.0 +
                    leftTopl * -1.0 + rightTopl * 1.0 +
                    leftBottoml * -1.0 +rightBottoml * 1.0;

     //垂直方向
     float vColor = leftl * 0.0 + rightl * 0.0 +
                    topl * -2.0 + bottoml * 2.0 +
                    leftTopl * -1.0 + rightTopl * -1.0 +
                    leftBottoml * 1.0 +rightBottoml * 1.0;

     //求梯度
     float finalColor = length(vec2(hColor, vColor));

     gl_FragColor = vec4(vec3(finalColor), 1.0);
}
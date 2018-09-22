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
     float textureColor = texture2D(u_TextureUnit0, v_TextureCoordinates).r;

     float leftTextureColor = texture2D(u_TextureUnit0, v_leftTextureCoordinate).r;
     float rightTextureColor = texture2D(u_TextureUnit0, v_rightTextureCoordinate).r;
     float topTextureColor = texture2D(u_TextureUnit0, v_topTextureCoordinate).r;
     float bottomTextureColor = texture2D(u_TextureUnit0, v_bottomTextureCoordinate).r;

     float leftTopTextureColor = texture2D(u_TextureUnit0, v_leftTopTextureCoordinate).r;
     float rightTopTextureColor = texture2D(u_TextureUnit0, v_rightTopTextureCoordinate).r;
     float leftBottomTextureColor = texture2D(u_TextureUnit0, v_leftBottomTextureCoordinate).r;
     float rightBottomTextureColor = texture2D(u_TextureUnit0, v_rightBottomTextureCoordinate).r;


     float pixelIntensitySum = textureColor + leftTextureColor + rightTextureColor +
                            topTextureColor + bottomTextureColor + leftTopTextureColor +
                             rightTopTextureColor + leftBottomTextureColor + rightBottomTextureColor;
     float sumTest = step(1.5, pixelIntensitySum);
     float pixelTest = step(0.01, textureColor);

     gl_FragColor = vec4(vec3(sumTest * pixelTest), 1.0);
}
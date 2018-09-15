attribute vec4 a_Position;
attribute vec2 a_TextureCoordinates;
varying vec2 v_TextureCoordinates;

uniform float u_imageWidthFactor;
uniform float u_imageHeightFactor;
varying vec2 v_leftTextureCoordinate;//左边
varying vec2 v_rightTextureCoordinate;//右边
varying vec2 v_topTextureCoordinate;//上边
varying vec2 v_bottomTextureCoordinate;//下边

varying vec2 v_leftTopTextureCoordinate;//左上角
varying vec2 v_rightTopTextureCoordinate;//右上角
varying vec2 v_leftBottomTextureCoordinate;//左下角
varying vec2 v_rightBottomTextureCoordinate;//右上角

void main()
{
    v_TextureCoordinates = a_TextureCoordinates;
    gl_Position = a_Position;

    mediump vec2 widthStep = vec2(u_imageWidthFactor, 0.0);
    mediump vec2 heightStep = vec2(0.0, u_imageHeightFactor);
    mediump vec2 widthHeightStep = vec2(u_imageWidthFactor, u_imageHeightFactor);
    mediump vec2 widthNegativeHeightStep = vec2(u_imageWidthFactor, -u_imageHeightFactor);

    v_leftTextureCoordinate = a_TextureCoordinates - widthStep;
    v_rightTextureCoordinate = a_TextureCoordinates + widthStep;
    v_topTextureCoordinate = a_TextureCoordinates + heightStep;
    v_bottomTextureCoordinate = a_TextureCoordinates - heightStep;

    v_leftTopTextureCoordinate = a_TextureCoordinates - widthHeightStep;
    v_rightTopTextureCoordinate = a_TextureCoordinates + widthNegativeHeightStep ;
    v_leftBottomTextureCoordinate = a_TextureCoordinates - widthNegativeHeightStep;
    v_rightBottomTextureCoordinate = a_TextureCoordinates + widthHeightStep;
}
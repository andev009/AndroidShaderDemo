attribute vec4 a_Position;
attribute vec2 a_TextureCoordinates;
varying vec2 v_TextureCoordinates;
const int GAUSSIAN_SAMPLES = 9;

uniform float texelWidthOffset;
uniform float texelHeightOffset;

varying vec2 blurCoordinates[GAUSSIAN_SAMPLES];

void main()
{
   gl_Position = a_Position;
   v_TextureCoordinates = a_TextureCoordinates.xy;

   int multiplier = 0;
   vec2 blurStep;
   vec2 singleStepOffset = vec2(texelHeightOffset, texelWidthOffset);

   for (int i = 0; i < GAUSSIAN_SAMPLES; i++){
      multiplier = (i - ((GAUSSIAN_SAMPLES - 1) / 2));
      blurStep = float(multiplier) * singleStepOffset;
      blurCoordinates[i] = a_TextureCoordinates.xy + blurStep;
   }
}
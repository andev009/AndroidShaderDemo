precision mediump float;
 
varying highp vec2 v_TextureCoordinates;
 
uniform sampler2D u_TextureUnit0;


uniform highp float texelWidthOffset;
uniform highp float texelHeightOffset;
const mediump float upperThreshold = 0.4;
const mediump float lowerThreshold = 0.1;

void main()
 {
     vec3 currentGradientAndDirection = texture2D(u_TextureUnit0, v_TextureCoordinates).rgb;
     vec2 gradientDirection = ((currentGradientAndDirection.gb * 2.0) - 1.0) * vec2(texelWidthOffset, texelHeightOffset);
     
     float firstSampledGradientMagnitude = texture2D(u_TextureUnit0, v_TextureCoordinates + gradientDirection).r;
     float secondSampledGradientMagnitude = texture2D(u_TextureUnit0, v_TextureCoordinates - gradientDirection).r;
     
     float multiplier = step(firstSampledGradientMagnitude, currentGradientAndDirection.r);
     multiplier = multiplier * step(secondSampledGradientMagnitude, currentGradientAndDirection.r);
     
     float thresholdCompliance = smoothstep(lowerThreshold, upperThreshold, currentGradientAndDirection.r);
     multiplier = multiplier * thresholdCompliance;
     
     gl_FragColor = vec4(multiplier, multiplier, multiplier, 1.0);
 }
precision mediump float;
uniform sampler2D u_TextureUnit;
varying vec2 v_TextureCoordinates;

uniform float u_Offset;

void main()
{
    vec4 origin = texture2D(u_TextureUnit, v_TextureCoordinates);
    vec4 green = texture2D(u_TextureUnit, vec2(v_TextureCoordinates.x - u_Offset,v_TextureCoordinates.y - u_Offset));
    vec4 red = texture2D(u_TextureUnit, vec2(v_TextureCoordinates.x + u_Offset,v_TextureCoordinates.y + u_Offset));
    gl_FragColor = vec4(red.x, green.y, origin.z, origin.w);
}
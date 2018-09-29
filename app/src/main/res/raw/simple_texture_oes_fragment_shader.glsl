#extension GL_OES_EGL_image_external : require

precision mediump float;
varying vec2 v_TextureCoordinates;
uniform samplerExternalOES u_TextureOESUnit;
void main()
{
    gl_FragColor = texture2D(u_TextureOESUnit, v_TextureCoordinates);
}

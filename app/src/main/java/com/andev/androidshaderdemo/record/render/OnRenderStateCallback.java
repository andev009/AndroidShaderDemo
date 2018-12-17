package com.andev.androidshaderdemo.record.render;


import android.graphics.SurfaceTexture;
import android.opengl.EGLContext;

import javax.microedition.khronos.opengles.GL10;

public interface OnRenderStateCallback {
	void onSurfaceCreatedCallback(GL10 gl, EGLContext eglContext, SurfaceTexture surfaceTexture);
	void onSurfaceChangedCallback(GL10 gl, int width, int height);
}

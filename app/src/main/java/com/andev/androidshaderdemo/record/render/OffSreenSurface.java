package com.andev.androidshaderdemo.record.render;


import android.opengl.EGL14;
import android.opengl.EGLSurface;
import android.view.Surface;

import com.andev.androidshaderdemo.gles.EglCore;

public class OffSreenSurface {
	private EglCore eglCore;
	private EGLSurface eglSurface;
	private Surface surface;


	public OffSreenSurface(EglCore eglCore, Surface surface) {
		this.eglCore = eglCore;
		this.surface = surface;
		eglSurface = eglCore.createWindowSurface(surface);
	}


	public void makeCurrent() {
		eglCore.makeCurrent(eglSurface);
	}

	public void setPresentationTime(long nanoSeconds) {
		eglCore.setPresentationTime(eglSurface, nanoSeconds);
	}


	public boolean swapBuffers() {
		boolean result = eglCore.swapBuffers(eglSurface);

		return result;
	}

	public void release() {
		eglCore.releaseSurface(eglSurface);
		eglSurface = EGL14.EGL_NO_SURFACE;

		if (surface != null) {
			surface.release();
		}
	}

}

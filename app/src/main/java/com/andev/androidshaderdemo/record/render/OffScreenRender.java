package com.andev.androidshaderdemo.record.render;


import android.content.Context;
import android.opengl.EGLContext;
import android.util.Log;
import android.view.Surface;

import com.andev.androidshaderdemo.filter.CameraInputFilter;
import com.andev.androidshaderdemo.gles.EglCore;
import com.andev.androidshaderdemo.record.filter.ImageFilter;

public class OffScreenRender {
	private static final String TAG = "OffScreenRender";
	private EglCore eglCore;
	private OffSreenSurface offSreenSurface;

	ImageFilter imageFilter;
	Context context;

	public OffScreenRender(	Context context, EGLContext eglContext, Surface surface) {
		eglCore = new EglCore(eglContext, EglCore.FLAG_RECORDABLE);

		offSreenSurface = new OffSreenSurface(eglCore, surface);
		offSreenSurface.makeCurrent();
	}

	public void release() {
		if (offSreenSurface == null) {
			return;
		}

		offSreenSurface.release();
		eglCore.release();
		offSreenSurface = null;
		eglCore = null;
	}

	public void draw(CameraInputFilter cameraInputFilter, float[] matrix, int textureId, long time) {
		offSreenSurface.makeCurrent();
//		if (imageFilter != filter && imageFilter != null) {
//			imageFilter.release();
//		}
//		imageFilter = filter;
//		filter.init();
//		filter.draw(textureId, matrix,0,0);

		cameraInputFilter.onDrawFrame(textureId);

		Log.w(TAG, "textureId :" + textureId);
		Log.w(TAG, "draw :" + time);

		offSreenSurface.setPresentationTime(time);


		offSreenSurface.swapBuffers();
	}

}

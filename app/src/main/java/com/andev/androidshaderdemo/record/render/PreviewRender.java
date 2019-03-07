package com.andev.androidshaderdemo.record.render;


import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.andev.androidshaderdemo.filter.CameraInputFilter;
import com.andev.androidshaderdemo.record.filter.ImageFilter;
import com.andev.androidshaderdemo.record.utils.VideoFrameData;
import com.andev.androidshaderdemo.util.TextureHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.glViewport;

public class PreviewRender implements GLSurfaceView.Renderer{
	private SurfaceTexture surfaceTexture;
	private int textureId = TextureHelper.NO_TEXTURE;
	private CameraInputFilter cameraInputFilter;
	private ImageFilter mFilter;

	private OnRenderStateCallback onRenderStateCallback;

	private OnFrameAvailableCallback onFrameAvailableCallback;

	public void setOnRenderStateCallback(OnRenderStateCallback onRenderStateCallback){
		this.onRenderStateCallback = onRenderStateCallback;
	}

	public void setOnFrameAvailableCallback(OnFrameAvailableCallback onFrameAvailableCallback){
		this.onFrameAvailableCallback = onFrameAvailableCallback;
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		initSurfaceTexture();
		if(onRenderStateCallback != null){
			onRenderStateCallback.onSurfaceCreatedCallback(gl, EGL14.eglGetCurrentContext(), surfaceTexture);
		}

		mFilter = new ImageFilter();
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		glViewport(0, 0, width, height);
		if(onRenderStateCallback != null){
			onRenderStateCallback.onSurfaceChangedCallback(gl, width, height);
		}

		if(cameraInputFilter != null){
			cameraInputFilter.onDisplaySizeChanged(width, height);
		}
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		GLES20.glClearColor(0.0f, 1.0f, 0.0f, 0.0f);
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

		Log.d("onDrawFrame", "Thread : " + Thread.currentThread().getName());

		if(surfaceTexture == null)
			return;
		surfaceTexture.updateTexImage();
		float[] mtx = new float[16];
		surfaceTexture.getTransformMatrix(mtx);



		if(onFrameAvailableCallback != null){
			onFrameAvailableCallback.onFrameAvailableCallback(new VideoFrameData(cameraInputFilter,
					mtx, surfaceTexture.getTimestamp(), textureId));
		}

//		mFilter.init();
//		surfaceTexture.getTransformMatrix(mtx);
//		mFilter.draw(textureId, mtx, 0, 0);

		if(cameraInputFilter != null){
			cameraInputFilter.setTextureTransformMatrix(mtx);
			cameraInputFilter.onDrawFrame(textureId);
		}

	}

	private void initSurfaceTexture(){
		if (textureId == TextureHelper.NO_TEXTURE) {
			textureId = TextureHelper.getExternalOESTextureID();
			if (textureId != TextureHelper.NO_TEXTURE) {
				surfaceTexture = new SurfaceTexture(textureId);
			}
		}
	}

	public void setCameraInputFilter(CameraInputFilter cameraInputFilter){
		this.cameraInputFilter = cameraInputFilter;
	}
}

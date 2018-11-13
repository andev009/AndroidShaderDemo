package com.andev.androidshaderdemo.activity;


import android.app.Activity;
import android.opengl.EGLSurface;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.LinearLayout;

import com.andev.androidshaderdemo.R;
import com.andev.androidshaderdemo.gles.EglCore;
import com.andev.androidshaderdemo.render.EGLRender;

public class EGLActivity extends Activity implements SurfaceHolder.Callback{

	private LinearLayout root;

	private SurfaceView surfaceView;

	private EglCore eglCore;
	private EGLSurface eglSurface;

	private EGLRender eglRender;

	private Handler renderHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.java_egl_layout);

		initViews();
	}

	private void initViews(){
		root = (LinearLayout)findViewById(R.id.root);
		surfaceView = (SurfaceView)findViewById(R.id.surfaceView);
		surfaceView.getHolder().addCallback(this);
	}

	@Override
	public void surfaceCreated(final SurfaceHolder holder) {
	}

	@Override
	public void surfaceChanged(final SurfaceHolder holder, int format, final int width, final int height) {
		renderHandler = newHandlerThread("render");
		renderHandler.post(new Runnable() {
			@Override
			public void run() {
				initEGL();
				eglSurface = eglCore.createWindowSurface(holder.getSurface());
				eglCore.makeCurrent(eglSurface);

				eglRender = new EGLRender(EGLActivity.this);
				eglRender.onSurfaceChanged(width, height);
				eglRender.onDraw();

				eglCore.swapBuffers(eglSurface);
			}
		});
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

	}

	private void initEGL(){
		eglCore = new EglCore(null, EglCore.FLAG_RECORDABLE);
	}

	private Handler newHandlerThread(String tag) {
		HandlerThread thread = new HandlerThread(tag);
		thread.start();
		return new Handler(thread.getLooper());
	}

	@Override
	public void onDestroy(){
		super.onDestroy();

		renderHandler.post(new Runnable() {
			@Override
			public void run() {
				eglCore.release();
				eglCore.releaseSurface(eglSurface);
			}
		});
	}
}

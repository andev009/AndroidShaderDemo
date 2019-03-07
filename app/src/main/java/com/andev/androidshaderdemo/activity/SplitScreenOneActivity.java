package com.andev.androidshaderdemo.activity;


import android.graphics.SurfaceTexture;
import android.opengl.EGLContext;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.andev.androidshaderdemo.R;
import com.andev.androidshaderdemo.record.render.OffScreenRender;
import com.andev.androidshaderdemo.record.render.OnFrameAvailableCallback;
import com.andev.androidshaderdemo.record.render.OnRenderStateCallback;
import com.andev.androidshaderdemo.record.utils.VideoFrameData;
import com.andev.androidshaderdemo.record.widget.CameraSurfaceView;

import javax.microedition.khronos.opengles.GL10;

public class SplitScreenOneActivity extends AppCompatActivity implements OnRenderStateCallback,
		OnFrameAvailableCallback {

	CameraSurfaceView cameraSurfaceView;
	SurfaceView surfaceView;
	EGLContext eglContext;

	OffScreenRender offScreenRender;

	private HandlerThread handlerThread;
	private Handler previewHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.split_screen_one_layout);

		initViews();
	}

	private void initViews(){
		cameraSurfaceView = (CameraSurfaceView)findViewById(R.id.cameraSurfaceView);
		surfaceView = (SurfaceView)findViewById(R.id.surfaceView);

		handlerThread = new HandlerThread("preview");
		handlerThread.start();
		previewHandler = new Handler(handlerThread.getLooper());

		cameraSurfaceView.setOnRenderStateCallback(this);
		cameraSurfaceView.setOnFrameAvailableCallback(this);

		surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
			@Override
			public void surfaceCreated(SurfaceHolder surfaceHolder) {
				createOffScreenRender();
			}

			@Override
			public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

			}

			@Override
			public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
				Log.d("surfaceDestroyed","surfaceDestroyed 111111");
			}
		});
	}


	@Override
	public void onSurfaceCreatedCallback(GL10 gl, final EGLContext eglContext, SurfaceTexture surfaceTexture) {
		this.eglContext = eglContext;

		surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
			@Override
			public void surfaceCreated(SurfaceHolder surfaceHolder) {
				createOffScreenRender();
			}

			@Override
			public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

			}

			@Override
			public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
				Log.d("surfaceDestroyed","surfaceDestroyed 22222");
			}
		});
	}

	@Override
	public void onSurfaceChangedCallback(GL10 gl, int width, int height) {

	}


	@Override
	public void onFrameAvailableCallback(final VideoFrameData frameData) {
		previewHandler.post(new Runnable() {
			@Override
			public void run() {
				if(offScreenRender != null){
					offScreenRender.draw(frameData.getFilter(), frameData.getMatrix(), frameData.getTextureId()
							,frameData.getTimestamp());
				}
			}
		});
	}

	private synchronized void createOffScreenRender(){
		previewHandler.post(new Runnable() {
			@Override
			public void run() {
				if(offScreenRender == null){
					offScreenRender = new OffScreenRender(eglContext, surfaceView.getHolder().getSurface());
				}
			}
		});
	}


	@Override
	public void onPause(){
		super.onPause();

		cameraSurfaceView.onPause();
	}

	@Override
	public void onStop(){
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		cameraSurfaceView.stopPreview();


		previewHandler.post(new Runnable() {
			@Override
			public void run() {
				if(offScreenRender != null){
					offScreenRender.release();
					offScreenRender = null;
				}
			}
		});
	}
}

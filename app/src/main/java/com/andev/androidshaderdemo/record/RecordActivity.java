package com.andev.androidshaderdemo.record;


import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.opengl.EGLContext;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.andev.androidshaderdemo.R;
import com.andev.androidshaderdemo.camera.CameraEngine;
import com.andev.androidshaderdemo.camera.CameraInfo;
import com.andev.androidshaderdemo.record.codec.VideoCodec;
import com.andev.androidshaderdemo.record.codec.VideoConfig;
import com.andev.androidshaderdemo.record.render.OnFrameAvailableCallback;
import com.andev.androidshaderdemo.record.render.OnRenderStateCallback;
import com.andev.androidshaderdemo.record.utils.StorageUtil;
import com.andev.androidshaderdemo.record.utils.VideoFrameData;
import com.andev.androidshaderdemo.record.widget.CameraSurfaceView;

import java.io.File;
import java.io.IOException;

import javax.microedition.khronos.opengles.GL10;

public class RecordActivity extends Activity implements OnRenderStateCallback, OnFrameAvailableCallback {
	public static final String VIDEO_TEMP_FILE_NAME = StorageUtil.getExternalStoragePath() +
			File.separator + "temp" + ".mp4";
	public static final int BIT_RATE = 15 * 1000000;

	CameraSurfaceView cameraSurfaceView;
	Button bt_record;

	VideoCodec videoCodec;
	EGLContext eglContext;

	boolean isRecord;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record_layout);

		initViews();
	}

	private void initViews(){
		cameraSurfaceView = (CameraSurfaceView)findViewById(R.id.cameraSurfaceView);
		cameraSurfaceView.setOnRenderStateCallback(this);

		bt_record = (Button)findViewById(R.id.bt_record);
		bt_record.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!isRecord){
					startRecord();

					bt_record.setText("Stop");
				}else{
					stopRecord();
					bt_record.setText("Start");
				}
			}
		});
	}

	private void startRecord(){
		isRecord = true;

		CameraInfo info = CameraEngine.getCameraInfo();


		//VideoConfig videoConfig = new VideoConfig(VIDEO_TEMP_FILE_NAME, eglContext, 640, 480, BIT_RATE);
		VideoConfig videoConfig = new VideoConfig(VIDEO_TEMP_FILE_NAME, eglContext, info.previewWidth, info.previewHeight, BIT_RATE);


		Log.d("tag", "VIDEO_TEMP_FILE_NAME : " + VIDEO_TEMP_FILE_NAME);

		videoCodec = new VideoCodec(this, videoConfig);
		try {
			videoCodec.prepareCodec();
		}catch (IOException e){

		}
		cameraSurfaceView.setOnFrameAvailableCallback(this);

		videoCodec.start();
	}

	private void stopRecord(){
		isRecord = false;
		videoCodec.stop();
	}

	@Override
	public void onSurfaceCreatedCallback(GL10 gl, EGLContext eglContext, SurfaceTexture surfaceTexture) {
		this.eglContext = eglContext;
	}

	@Override
	public void onSurfaceChangedCallback(GL10 gl, int width, int height) {

	}

	@Override
	public void onFrameAvailableCallback(VideoFrameData frameData) {
		videoCodec.frameAvailableCallback(frameData);
	}



	@Override
	protected void onPause() {
		stopRecord();
		cameraSurfaceView.stopPreview();
		cameraSurfaceView.onPause();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}

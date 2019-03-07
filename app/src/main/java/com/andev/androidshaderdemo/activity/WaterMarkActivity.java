package com.andev.androidshaderdemo.activity;


import android.app.Activity;
import android.os.Bundle;

import com.andev.androidshaderdemo.R;
import com.andev.androidshaderdemo.widget.CameraView;

public class WaterMarkActivity extends Activity implements CameraView.OnCameraStateListener{

	CameraView cameraView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_layout);

		initViews();
	}

	private void initViews(){
		cameraView = (CameraView)findViewById(R.id.cameraView);
		cameraView.setOnCameraStateListener(this);
	}


	@Override
	public void onPreviewSurfaceCreated() {
		cameraView.openWatermark();
	}

	@Override
	public void onStop() {
		super.onStop();

		cameraView.stopPreview();
	}
}

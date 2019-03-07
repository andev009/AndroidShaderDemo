package com.andev.androidshaderdemo.activity;


import android.app.Activity;
import android.os.Bundle;

import com.andev.androidshaderdemo.R;
import com.andev.androidshaderdemo.filter.SplitScreenFilter;
import com.andev.androidshaderdemo.widget.CameraView;

public class SplitScreenTwoActivity extends Activity implements CameraView.OnCameraStateListener{
	private static final String TAG = "SplitScreenTwoActivity";
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
		cameraView.setFilter(new SplitScreenFilter(this));
	}


	@Override
	public void onStop() {
		super.onStop();

		cameraView.stopPreview();
	}
}

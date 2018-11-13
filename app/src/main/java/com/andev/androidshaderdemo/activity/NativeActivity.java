package com.andev.androidshaderdemo.activity;


import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.LinearLayout;

import com.andev.androidshaderdemo.R;
import com.andev.androidshaderdemo.nativedemo.NativeController;

public class NativeActivity extends Activity implements SurfaceHolder.Callback{
	private LinearLayout root;
	private SurfaceView surfaceView;

	private NativeController nativeController;

	static {
		System.loadLibrary("drawdemo");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.native_activity);

		initViews();
	}

	private void initViews(){
		root = (LinearLayout)findViewById(R.id.root);
		surfaceView = new SurfaceView(this);
		surfaceView.getHolder().addCallback(this);

		root.addView(surfaceView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		nativeController = new NativeController();
		nativeController.init();
		nativeController.setSurface(holder.getSurface());
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		nativeController.resetSize(width, height);

		Resources resources = getResources();
		Bitmap bitmap = BitmapFactory.decodeResource(resources, R.drawable.face);
		nativeController.showBitmap(bitmap);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

	}
}

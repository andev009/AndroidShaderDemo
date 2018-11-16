package com.andev.androidshaderdemo.activity;


import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.andev.androidshaderdemo.R;
import com.andev.androidshaderdemo.render.LUTRender;

public class LUTActivity extends AppCompatActivity {


	private GLSurfaceView glSurfaceView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filter_layout);

		glSurfaceView = (GLSurfaceView)findViewById(R.id.glsurfaceView);
		glSurfaceView.setEGLContextClientVersion(2);

		LUTRender lutRender = new LUTRender(this);
		glSurfaceView.setRenderer(lutRender);

		glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}

	@Override
	protected void onPause() {
		super.onPause();
		glSurfaceView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		glSurfaceView.onResume();
	}
}

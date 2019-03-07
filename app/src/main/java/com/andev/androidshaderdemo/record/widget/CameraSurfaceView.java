package com.andev.androidshaderdemo.record.widget;


import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.EGLContext;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.andev.androidshaderdemo.camera.CameraEngine;
import com.andev.androidshaderdemo.camera.CameraInfo;
import com.andev.androidshaderdemo.data.VertexArray;
import com.andev.androidshaderdemo.filter.CameraInputFilter;
import com.andev.androidshaderdemo.record.render.OnFrameAvailableCallback;
import com.andev.androidshaderdemo.record.render.OnRenderStateCallback;
import com.andev.androidshaderdemo.record.render.PreviewRender;
import com.andev.androidshaderdemo.record.utils.VideoFrameData;
import com.andev.androidshaderdemo.util.Rotation;
import com.andev.androidshaderdemo.util.TextureRotationUtil;

import javax.microedition.khronos.opengles.GL10;

public class CameraSurfaceView extends GLSurfaceView implements OnRenderStateCallback, OnFrameAvailableCallback{
	private Context context;
	private SurfaceTexture surfaceTexture;
	private PreviewRender previewRender;
	private VertexArray vertexArray;

	protected int imageWidth, imageHeight;//图像宽高

	private OnFrameAvailableCallback onFrameAvailableCallback;
	private OnRenderStateCallback onRenderStateCallback;

	public CameraSurfaceView(Context context) {
		super(context);
		init(context);
	}

	public CameraSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public void setOnFrameAvailableCallback(OnFrameAvailableCallback onFrameAvailableCallback){
		this.onFrameAvailableCallback = onFrameAvailableCallback;
	}

	public void setOnRenderStateCallback(OnRenderStateCallback onRenderStateCallback){
		this.onRenderStateCallback = onRenderStateCallback;
	}

	private void init(Context context) {
		this.context = context;

		setEGLContextClientVersion(2);
		previewRender = new PreviewRender();
		previewRender.setOnRenderStateCallback(this);
		previewRender.setOnFrameAvailableCallback(this);

		setRenderer(previewRender);
		setRenderMode(RENDERMODE_WHEN_DIRTY);
	}

	@Override
	public void onSurfaceCreatedCallback(GL10 gl, EGLContext eglContext, SurfaceTexture surfaceTexture) {
		this.surfaceTexture = surfaceTexture;
		surfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
			@Override
			public void onFrameAvailable(SurfaceTexture surfaceTexture) {
				requestRender();
			}
		});


		if(onRenderStateCallback != null){
			onRenderStateCallback.onSurfaceCreatedCallback(gl, eglContext,surfaceTexture);
		}
	}

	@Override
	public void onSurfaceChangedCallback(GL10 gl, int width, int height) {
		openCamera();

		previewRender.setCameraInputFilter(new CameraInputFilter(context, vertexArray));

		if(onRenderStateCallback != null){
			onRenderStateCallback.onSurfaceChangedCallback(gl, width, height);
		}
	}

	private void openCamera(){
		if(CameraEngine.getCamera() == null){
			CameraEngine.openCamera();
		}

		CameraInfo info = CameraEngine.getCameraInfo();
		if(info.orientation == 90 || info.orientation == 270){
			imageWidth = info.previewHeight;
			imageHeight = info.previewWidth;
		}else{
			imageWidth = info.previewWidth;
			imageHeight = info.previewHeight;
		}

		adjustSize(info.orientation, info.isFront, true);
		if(surfaceTexture != null){
			CameraEngine.startPreview(surfaceTexture);
		}
	}

	public void stopPreview(){
		CameraEngine.releaseCamera();
	}

	protected void adjustSize(int rotation, boolean flipHorizontal, boolean flipVertical){
		float[] textureCords = TextureRotationUtil.getRotation(Rotation.fromInt(rotation),
				flipHorizontal, flipVertical);
		float[] cube = TextureRotationUtil.CUBE;


		float[] newCube = new float[]{
				cube[0], cube[1], textureCords[0],textureCords[1],
				cube[2], cube[3], textureCords[2],textureCords[3],
				cube[4], cube[5], textureCords[4],textureCords[5],
				cube[6], cube[7], textureCords[6],textureCords[7]
		};

		vertexArray = new VertexArray(newCube);
	}


	@Override
	public void onFrameAvailableCallback(VideoFrameData frameData) {
		if(onFrameAvailableCallback != null){
			onFrameAvailableCallback.onFrameAvailableCallback(frameData);
		}
	}

}

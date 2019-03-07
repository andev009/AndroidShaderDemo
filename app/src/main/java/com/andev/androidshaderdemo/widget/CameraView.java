package com.andev.androidshaderdemo.widget;


import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;

import com.andev.androidshaderdemo.camera.CameraEngine;
import com.andev.androidshaderdemo.camera.CameraInfo;
import com.andev.androidshaderdemo.data.VertexArray;
import com.andev.androidshaderdemo.filter.BaseFilter;
import com.andev.androidshaderdemo.filter.CameraInputFilter;
import com.andev.androidshaderdemo.filter.WatermarkFilter;
import com.andev.androidshaderdemo.util.Rotation;
import com.andev.androidshaderdemo.util.TextureHelper;
import com.andev.androidshaderdemo.util.TextureRotationUtil;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glViewport;

public class CameraView extends GLSurfaceView implements GLSurfaceView.Renderer{
	Context context;
	VertexArray vertexArray;

	protected int surfaceWidth, surfaceHeight;

	private SurfaceTexture surfaceTexture;
	protected int textureId = TextureHelper.NO_TEXTURE;
	protected int imageWidth, imageHeight;//图像宽高

	private CameraInputFilter cameraInputFilter;
	private BaseFilter filter;
	WatermarkFilter watermarkFilter;

	private OnCameraStateListener onCameraStateListener;

	private boolean openWatermarkFilter = false;


	public interface OnCameraStateListener{
		void onPreviewSurfaceCreated();
	}

	public void setOnCameraStateListener(OnCameraStateListener onCameraStateListener){
		this.onCameraStateListener = onCameraStateListener;
	}

	public CameraView(Context context) {
		this(context, null);
		this.context = context;
	}

	public CameraView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;

		setEGLContextClientVersion(2);
		setRenderer(this);
		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

		Log.d("CameraView", "CameraView Thread.currentThread() :" + Thread.currentThread().getName());

	}

	public void setFilter(final BaseFilter imageFilter){
		queueEvent(new Runnable() {
			@Override
			public void run() {
				filter = imageFilter;
				onFilterChanged();
			}
		});
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		glDisable(GLES20.GL_DITHER);
		glClearColor(0,0, 0, 0);
		glEnable(GLES20.GL_CULL_FACE);
		//glEnable(GLES20.GL_DEPTH_TEST);

		Log.d("CameraView", "CameraView Thread.currentThread() :" + Thread.currentThread().getName());

		initSurfaceTexture();

		if(onCameraStateListener != null){
			onCameraStateListener.onPreviewSurfaceCreated();
		}

		//filter = new ImageFilter(context);
		watermarkFilter = new WatermarkFilter(context, 400, 50, "Android Shader Demo",35);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		glViewport(0, 0, width, height);
		surfaceWidth = width;
		surfaceHeight = height;

		openCamera();

		if(cameraInputFilter == null){
			cameraInputFilter = new CameraInputFilter(context, vertexArray);
		}

		onFilterChanged();
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		//GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		//GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

		glClear(GL_COLOR_BUFFER_BIT);

		if(surfaceTexture == null)
			return;
		surfaceTexture.updateTexImage();
		float[] mtx = new float[16];
		surfaceTexture.getTransformMatrix(mtx);

		cameraInputFilter.setTextureTransformMatrix(mtx);

		//set viewport for surfaceView
		glViewport(0, 0, surfaceWidth, surfaceHeight);
		if(filter == null){
			cameraInputFilter.onDrawFrame(textureId);
		}else{
			int frameTextureID = cameraInputFilter.onDrawToTexture(textureId);
			filter.onDrawFrame(frameTextureID);
		}


		if(openWatermarkFilter){
			watermarkFilter.onDrawFrame();
		}
	}

	private void initSurfaceTexture(){
		if (textureId == TextureHelper.NO_TEXTURE) {
			textureId = TextureHelper.getExternalOESTextureID();
			if (textureId != TextureHelper.NO_TEXTURE) {
				surfaceTexture = new SurfaceTexture(textureId);
				surfaceTexture.setOnFrameAvailableListener(onFrameAvailableListener);
			}
		}
	}

	private SurfaceTexture.OnFrameAvailableListener onFrameAvailableListener = new SurfaceTexture.OnFrameAvailableListener() {

		@Override
		public void onFrameAvailable(SurfaceTexture surfaceTexture) {
			requestRender();
		}
	};

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


	private void adjustSize(int rotation, boolean flipHorizontal, boolean flipVertical){
		float[] textureCords = TextureRotationUtil.getRotation(Rotation.fromInt(rotation),
				flipHorizontal, flipVertical);
		float[] cube = TextureRotationUtil.CUBE;

		float ratio1 = (float)surfaceWidth / imageWidth;
		float ratio2 = (float)surfaceHeight / imageHeight;
		float ratioMax = Math.max(ratio1, ratio2);
		int imageWidthNew = Math.round(imageWidth * ratioMax);
		int imageHeightNew = Math.round(imageHeight * ratioMax);

		float ratioWidth = imageWidthNew / (float)surfaceWidth;
		float ratioHeight = imageHeightNew / (float)surfaceHeight;

		float distHorizontal = (1 - 1 / ratioWidth) / 2;
		float distVertical = (1 - 1 / ratioHeight) / 2;

		float[] newCube = new float[]{
				cube[0], cube[1], addDistance(textureCords[0], distVertical),addDistance(textureCords[1], distHorizontal),
				cube[2], cube[3], addDistance(textureCords[2], distVertical),addDistance(textureCords[3], distHorizontal),
				cube[4], cube[5], addDistance(textureCords[4], distVertical),addDistance(textureCords[5], distHorizontal),
				cube[6], cube[7], addDistance(textureCords[6], distVertical),addDistance(textureCords[7], distHorizontal)
		};


//		float[] newCube = new float[]{
//				cube[0], cube[1], textureCords[0],textureCords[1],
//				cube[2], cube[3], textureCords[2],textureCords[3],
//				cube[4], cube[5], textureCords[4],textureCords[5],
//				cube[6], cube[7], textureCords[6],textureCords[7]
//		};


		vertexArray = new VertexArray(newCube);
	}

	private float addDistance(float coordinate, float distance) {
		return coordinate == 0.0f ? distance : 1 - distance;
	}

	private void onFilterChanged(){
		cameraInputFilter.onDisplaySizeChanged(surfaceWidth, surfaceHeight);
		if(filter != null){
			cameraInputFilter.initCameraFrameBuffer(imageWidth, imageHeight);
		}else{
			cameraInputFilter.destroyFramebuffers();
		}
	}

	public void openWatermark(){
		openWatermarkFilter = true;
	}

	public void stopPreview(){
		CameraEngine.releaseCamera();
	}
}

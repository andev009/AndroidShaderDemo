package com.andev.androidshaderdemo.render;


import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.andev.androidshaderdemo.R;
import com.andev.androidshaderdemo.data.VertexArray;
import com.andev.androidshaderdemo.programs.ShakeProgram;
import com.andev.androidshaderdemo.util.TextureHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glViewport;
import static com.andev.androidshaderdemo.Constants.BYTES_PER_FLOAT;

public class ShakeRender implements GLSurfaceView.Renderer{
	private static final int POSITION_COMPONENT_COUNT = 2;
	private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
	private static final int STRIDE = (POSITION_COMPONENT_COUNT
			+ TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT;

	public static final float CUBE[] = {//翻转顶点信息中的纹理坐标,统一用1去减
			-1.0f, -1.0f, 0f, 1f - 0f,
			1.0f, -1.0f, 1f, 1f -0f,
			-1.0f, 1.0f, 0f, 1f -1f,
			1.0f, 1.0f, 1f, 1f -1f,
	};

	private float[] scaleMatrix = new float[16];
	private float scale;
	private final int MaxFrame = 16;
	private final int MiddleFrame = 8;
	private int curFrame;

	Context context;
	VertexArray vertexArray;
	ShakeProgram shakeProgram;
	private int texture;

	public ShakeRender(Context context){
		this.context = context;
		vertexArray = new VertexArray(CUBE);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		shakeProgram = new ShakeProgram(context);
		texture = TextureHelper.loadTexture(context, R.drawable.lena);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		glViewport(0, 0, width, height);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		glClear(GL_COLOR_BUFFER_BIT);

		float progress;
		if (curFrame <= MaxFrame) {
			progress = curFrame * 1.0f / MaxFrame;
		} else {
			curFrame = 0;
			progress = 0;
		}

		float scale = 1f + progress * 0.3f;
		float offset = progress * 0.01f;
		curFrame++;

		drawLayer(scale, offset);
	}

	private void drawLayer(float scale, float offset){
		shakeProgram.useProgram();

		Matrix.setIdentityM(scaleMatrix, 0);

		Matrix.scaleM(scaleMatrix, 0, scale, scale, scale);
		glUniformMatrix4fv(shakeProgram.getMvpMatrixLocation(), 1, false, scaleMatrix, 0);

		shakeProgram.setUniforms(texture, offset);

		draw();
	}

	private void draw(){
		vertexArray.setVertexAttribPointer(
				0,
				shakeProgram.getPositionAttributeLocation(),
				POSITION_COMPONENT_COUNT,
				STRIDE);

		vertexArray.setVertexAttribPointer(
				POSITION_COMPONENT_COUNT,
				shakeProgram.getTextureCoordinatesAttributeLocation(),
				TEXTURE_COORDINATES_COMPONENT_COUNT,
				STRIDE);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
	}
}

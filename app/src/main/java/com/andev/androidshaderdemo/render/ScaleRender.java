package com.andev.androidshaderdemo.render;


import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.andev.androidshaderdemo.R;
import com.andev.androidshaderdemo.data.VertexArray;
import com.andev.androidshaderdemo.programs.ScaleAnimationProgram;
import com.andev.androidshaderdemo.util.TextureHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glViewport;
import static com.andev.androidshaderdemo.Constants.BYTES_PER_FLOAT;

public class ScaleRender implements GLSurfaceView.Renderer{
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
	ScaleAnimationProgram scaleAnimationProgram;
	private int texture;

	public ScaleRender(Context context){
		this.context = context;
		vertexArray = new VertexArray(CUBE);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		scaleAnimationProgram = new ScaleAnimationProgram(context);
		texture = TextureHelper.loadTexture(context, R.drawable.lena);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		glViewport(0, 0, width, height);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		glClear(GL_COLOR_BUFFER_BIT);

		scaleAnimationProgram.useProgram();
		prepareDraw();
		scaleAnimationProgram.setUniforms(texture);

		vertexArray.setVertexAttribPointer(
				0,
				scaleAnimationProgram.getPositionAttributeLocation(),
				POSITION_COMPONENT_COUNT,
				STRIDE);

		vertexArray.setVertexAttribPointer(
				POSITION_COMPONENT_COUNT,
				scaleAnimationProgram.getTextureCoordinatesAttributeLocation(),
				TEXTURE_COORDINATES_COMPONENT_COUNT,
				STRIDE);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
	}

	private void prepareDraw(){
		Matrix.setIdentityM(scaleMatrix, 0);

		float progress;
		if (curFrame <= MiddleFrame) {
			progress = curFrame * 1.0f / MiddleFrame;
		} else {
			progress = 2f - curFrame * 1.0f / MiddleFrame;
		}
		float scale = 1f + progress * 0.3f;
		Matrix.scaleM(scaleMatrix, 0, scale, scale, scale);
		glUniformMatrix4fv(scaleAnimationProgram.getMvpMatrixLocation(), 1, false, scaleMatrix, 0);

		curFrame++;
		if(curFrame > MaxFrame){
			curFrame = 0;
		}
	}
}

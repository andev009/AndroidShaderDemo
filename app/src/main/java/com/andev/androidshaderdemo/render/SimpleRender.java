package com.andev.androidshaderdemo.render;


import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import com.andev.androidshaderdemo.data.VertexArray;
import com.andev.androidshaderdemo.programs.ColorShaderProgram;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static com.andev.androidshaderdemo.Constants.BYTES_PER_FLOAT;


public class SimpleRender implements GLSurfaceView.Renderer{
	private static final int POSITION_COMPONENT_COUNT = 2;
	private static final int COLOR_COMPONENT_COUNT = 3;
	private static final int STRIDE =
			(POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT)
					* BYTES_PER_FLOAT;

	public static final float CUBE[] = {
			-1.0f, -1.0f, 1f, 0f, 0f,
			1.0f, -1.0f, 1f, 0f, 0f,
			-1.0f, 1.0f, 1f, 0f, 0f,
			1.0f, 1.0f, 1f, 0f, 0f,
	};

	Context context;
	VertexArray vertexArray;

	ColorShaderProgram colorProgram;

	public SimpleRender(Context context) {
		this.context = context;

		vertexArray = new VertexArray(CUBE);
	}


	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		colorProgram = new ColorShaderProgram(context);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		glViewport(0, 0, width, height);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		glClear(GL_COLOR_BUFFER_BIT);

		colorProgram.useProgram();
		vertexArray.setVertexAttribPointer(
				0,
				colorProgram.getPositionAttributeLocation(),
				POSITION_COMPONENT_COUNT,
				STRIDE);

		vertexArray.setVertexAttribPointer(
				POSITION_COMPONENT_COUNT,
				colorProgram.getColorAttributeLocation(),
				COLOR_COMPONENT_COUNT,
				STRIDE);

		//GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
	}
}

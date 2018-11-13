package com.andev.androidshaderdemo.render;


import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import com.andev.androidshaderdemo.R;
import com.andev.androidshaderdemo.data.VertexArray;
import com.andev.androidshaderdemo.programs.SimpleTextureShaderProgram;
import com.andev.androidshaderdemo.util.TextureHelper;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static com.andev.androidshaderdemo.Constants.BYTES_PER_FLOAT;

public class EGLRender {
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

	Context context;
	VertexArray vertexArray;
	SimpleTextureShaderProgram simpleTextureShaderProgram;
	private int texture;

	public EGLRender(Context context) {
		this.context = context;

		vertexArray = new VertexArray(CUBE);
		simpleTextureShaderProgram = new SimpleTextureShaderProgram(context);
		texture = TextureHelper.loadTexture(context, R.drawable.lena);
	}

	public void onSurfaceChanged(int width, int height) {
		glClearColor(0.0f, 1.0f, 0.0f, 0.0f);
		glViewport(0, 0, width, height);

		Log.d("EGLRender", "onSurfaceChanged width:" + width + " height : " + height);
	}


	public void onDraw(){
		glClear(GL_COLOR_BUFFER_BIT);

		simpleTextureShaderProgram.useProgram();
		simpleTextureShaderProgram.setUniforms(texture);

		vertexArray.setVertexAttribPointer(
				0,
				simpleTextureShaderProgram.getPositionAttributeLocation(),
				POSITION_COMPONENT_COUNT,
				STRIDE);

		vertexArray.setVertexAttribPointer(
				POSITION_COMPONENT_COUNT,
				simpleTextureShaderProgram.getTextureCoordinatesAttributeLocation(),
				TEXTURE_COORDINATES_COMPONENT_COUNT,
				STRIDE);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

		Log.d("EGLRender", "EGLRender  onDraw");
	}

}

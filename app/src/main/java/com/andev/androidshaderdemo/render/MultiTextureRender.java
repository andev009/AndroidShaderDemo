package com.andev.androidshaderdemo.render;


import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import com.andev.androidshaderdemo.R;
import com.andev.androidshaderdemo.data.VertexArray;
import com.andev.androidshaderdemo.programs.MultiTextureShaderProgram;
import com.andev.androidshaderdemo.util.TextureHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static com.andev.androidshaderdemo.Constants.BYTES_PER_FLOAT;

public class MultiTextureRender implements GLSurfaceView.Renderer{

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
	MultiTextureShaderProgram multiTextureShaderProgram;
	private int originTexture;
	private int edgeTexture;
	private int hefeMapTexture;
	private int hefemetalTexture;
	private int hefesoftlightTexture;
	private int hefegradientmapTexture;
	private int[] textureIDs;


	public MultiTextureRender(Context context){
		this.context = context;
		vertexArray = new VertexArray(CUBE);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		multiTextureShaderProgram = new MultiTextureShaderProgram(context);
		originTexture = TextureHelper.loadTexture(context, R.drawable.lena);
		edgeTexture = TextureHelper.loadTexture(context, R.drawable.edgeburn);
		hefeMapTexture = TextureHelper.loadTexture(context, R.drawable.hefemap);
		hefemetalTexture = TextureHelper.loadTexture(context, R.drawable.hefemetal);
		hefesoftlightTexture = TextureHelper.loadTexture(context, R.drawable.hefesoftlight);
		hefegradientmapTexture = TextureHelper.loadTexture(context, R.drawable.hefegradientmap);

		textureIDs = new int[]{originTexture, edgeTexture, hefeMapTexture,
				hefemetalTexture, hefesoftlightTexture,hefegradientmapTexture};
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		glViewport(0, 0, width, height);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		glClear(GL_COLOR_BUFFER_BIT);

		multiTextureShaderProgram.useProgram();
		multiTextureShaderProgram.setUniforms(textureIDs, 0.5f);

		vertexArray.setVertexAttribPointer(
				0,
				multiTextureShaderProgram.getPositionAttributeLocation(),
				POSITION_COMPONENT_COUNT,
				STRIDE);

		vertexArray.setVertexAttribPointer(
				POSITION_COMPONENT_COUNT,
				multiTextureShaderProgram.getTextureCoordinatesAttributeLocation(),
				TEXTURE_COORDINATES_COMPONENT_COUNT,
				STRIDE);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

	}
}

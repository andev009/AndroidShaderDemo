package com.andev.androidshaderdemo.render;


import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import com.andev.androidshaderdemo.R;
import com.andev.androidshaderdemo.data.VertexArray;
import com.andev.androidshaderdemo.programs.TwoFilterOneProgram;
import com.andev.androidshaderdemo.programs.TwoFilterTwoProgram;
import com.andev.androidshaderdemo.util.TextureHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static com.andev.androidshaderdemo.Constants.BYTES_PER_FLOAT;

public class TwoFilterRender implements GLSurfaceView.Renderer{

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

	public static final float FlipCUBE[] = {
			-1.0f, -1.0f, 0f,  0f,
			1.0f, -1.0f, 1f, 0f,
			-1.0f, 1.0f, 0f, 1f,
			1.0f, 1.0f, 1f, 1f,
	};

	Context context;
	VertexArray vertexArray;
	VertexArray flipVertexArray;

	TwoFilterOneProgram twoFilterOneProgram;
	TwoFilterTwoProgram twoFilterTwoProgram;
	private int originTexture;

	private int[] textureIDs;

	private int[] mFrameBuffers;
	private int[] mFrameBufferTextures;

	private int width;
	private int height;

	public TwoFilterRender(Context context){
		this.context = context;
		vertexArray = new VertexArray(CUBE);
		flipVertexArray = new VertexArray(FlipCUBE);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		twoFilterOneProgram = new TwoFilterOneProgram(context);
		twoFilterTwoProgram = new TwoFilterTwoProgram(context);

		originTexture = TextureHelper.loadTexture(context, R.drawable.lena);

		textureIDs = new int[]{originTexture};
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		glViewport(0, 0, width, height);

		this.width = width;
		this.height = height;

		mFrameBuffers = new int[1];
		mFrameBufferTextures = new int[1];

		GLES20.glGenFramebuffers(1, mFrameBuffers, 0);
		GLES20.glGenTextures(1, mFrameBufferTextures, 0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mFrameBufferTextures[0]);
		GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0,
				GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffers[0]);
		GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
				GLES20.GL_TEXTURE_2D, mFrameBufferTextures[0], 0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		glClear(GL_COLOR_BUFFER_BIT);

		//render to framebuffer
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffers[0]);
		GLES20.glClearColor(0, 0, 0, 0);

		twoFilterOneProgram.useProgram();
		twoFilterOneProgram.setUniforms(textureIDs, 1.0f / width, 1.0f / height);

		vertexArray.setVertexAttribPointer(
				0,
				twoFilterOneProgram.getPositionAttributeLocation(),
				POSITION_COMPONENT_COUNT,
				STRIDE);

		vertexArray.setVertexAttribPointer(
				POSITION_COMPONENT_COUNT,
				twoFilterOneProgram.getTextureCoordinatesAttributeLocation(),
				TEXTURE_COORDINATES_COMPONENT_COUNT,
				STRIDE);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);


        //render to screen
		twoFilterTwoProgram.useProgram();
		twoFilterTwoProgram.setUniforms(mFrameBufferTextures, 1.0f / width, 1.0f / height);

		flipVertexArray.setVertexAttribPointer(
				0,
				twoFilterTwoProgram.getPositionAttributeLocation(),
				POSITION_COMPONENT_COUNT,
				STRIDE);

		flipVertexArray.setVertexAttribPointer(
				POSITION_COMPONENT_COUNT,
				twoFilterTwoProgram.getTextureCoordinatesAttributeLocation(),
				TEXTURE_COORDINATES_COMPONENT_COUNT,
				STRIDE);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

	}
}
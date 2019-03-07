package com.andev.androidshaderdemo.filter;


import android.content.Context;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;

import com.andev.androidshaderdemo.data.VertexArray;
import com.andev.androidshaderdemo.programs.SimpleTextureOESProgram;
import com.andev.androidshaderdemo.util.TextureHelper;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static com.andev.androidshaderdemo.Constants.BYTES_PER_FLOAT;

public class CameraInputFilter {
	private static final int POSITION_COMPONENT_COUNT = 2;
	private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
	private static final int STRIDE = (POSITION_COMPONENT_COUNT
			+ TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT;

	private float[] mTextureTransformMatrix;

	VertexArray vertexArray;
	SimpleTextureOESProgram simpleTextureOESProgram;

    private Context context;

	private int[] mFrameBuffers = null;
	private int[] mFrameBufferTextures = null;
	private int mFrameWidth = -1;
	private int mFrameHeight = -1;
	private int mOutputWidth;
	private int mOutputHeight;


	public CameraInputFilter(Context context, VertexArray vertexArray){
		this.context = context;
		this.vertexArray = vertexArray;
		simpleTextureOESProgram = new SimpleTextureOESProgram(context);
	}

	public void onDrawFrame(int textureId) {
		glClear(GL_COLOR_BUFFER_BIT);

		simpleTextureOESProgram.useProgram();
		simpleTextureOESProgram.setUniforms(textureId, mTextureTransformMatrix);

		vertexArray.setVertexAttribPointer(
				0,
				simpleTextureOESProgram.getPositionAttributeLocation(),
				POSITION_COMPONENT_COUNT,
				STRIDE);

		vertexArray.setVertexAttribPointer(
				POSITION_COMPONENT_COUNT,
				simpleTextureOESProgram.getTextureCoordinatesAttributeLocation(),
				TEXTURE_COORDINATES_COMPONENT_COUNT,
				STRIDE);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
	}

	public void setTextureTransformMatrix(float[] mtx){
		mTextureTransformMatrix = mtx;
	}

	public int onDrawToTexture(final int textureId) {
		if(mFrameBuffers == null)
			return TextureHelper.NO_TEXTURE;

		GLES20.glViewport(0, 0, mFrameWidth, mFrameHeight);
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffers[0]);

		simpleTextureOESProgram.useProgram();
		simpleTextureOESProgram.setUniforms(textureId, mTextureTransformMatrix);

		vertexArray.setVertexAttribPointer(
				0,
				simpleTextureOESProgram.getPositionAttributeLocation(),
				POSITION_COMPONENT_COUNT,
				STRIDE);

		vertexArray.setVertexAttribPointer(
				POSITION_COMPONENT_COUNT,
				simpleTextureOESProgram.getTextureCoordinatesAttributeLocation(),
				TEXTURE_COORDINATES_COMPONENT_COUNT,
				STRIDE);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

		GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0);
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

		GLES20.glViewport(0, 0, mOutputWidth, mOutputHeight);
		return mFrameBufferTextures[0];
	}

	public void initCameraFrameBuffer(int width, int height) {
		if(mFrameBuffers != null && (mFrameWidth != width || mFrameHeight != height))
			destroyFramebuffers();
		if (mFrameBuffers == null) {
			mFrameWidth = width;
			mFrameHeight = height;
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
	}

	public void destroyFramebuffers() {
		if (mFrameBufferTextures != null) {
			GLES20.glDeleteTextures(1, mFrameBufferTextures, 0);
			mFrameBufferTextures = null;
		}
		if (mFrameBuffers != null) {
			GLES20.glDeleteFramebuffers(1, mFrameBuffers, 0);
			mFrameBuffers = null;
		}
		mFrameWidth = -1;
		mFrameHeight = -1;
	}

	public void onDisplaySizeChanged(final int width, final int height) {
		mOutputWidth = width;
		mOutputHeight = height;
	}
}

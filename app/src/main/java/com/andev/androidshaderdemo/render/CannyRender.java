package com.andev.androidshaderdemo.render;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.andev.androidshaderdemo.R;
import com.andev.androidshaderdemo.data.VertexArray;
import com.andev.androidshaderdemo.programs.BaseProgram;
import com.andev.androidshaderdemo.programs.BaseSampleProgram;
import com.andev.androidshaderdemo.programs.GaussianProgram;
import com.andev.androidshaderdemo.util.TextureHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static com.andev.androidshaderdemo.Constants.BYTES_PER_FLOAT;

public class CannyRender implements GLSurfaceView.Renderer {
	private static final int FRAMEBUFFER_SIZE = 3;

	private static final int POSITION_COMPONENT_COUNT = 2;
	private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
	private static final int STRIDE = (POSITION_COMPONENT_COUNT
			+ TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT;

	public static final float CUBE[] = {//翻转顶点信息中的纹理坐标,统一用1去减
			-1.0f, -1.0f, 0f, 1f - 0f,
			1.0f, -1.0f, 1f, 1f - 0f,
			-1.0f, 1.0f, 0f, 1f - 1f,
			1.0f, 1.0f, 1f, 1f - 1f,
	};

	public static final float FlipCUBE[] = {
			-1.0f, -1.0f, 0f, 0f,
			1.0f, -1.0f, 1f, 0f,
			-1.0f, 1.0f, 0f, 1f,
			1.0f, 1.0f, 1f, 1f,
	};

	Context context;
	VertexArray vertexArray;
	VertexArray flipVertexArray;

	GaussianProgram gaussianProgram;
	BaseSampleProgram sobelProgram;
	BaseProgram nonmaximumSuppressionProgram;
	BaseSampleProgram weakPixelInclusionProgram;

	private int originTexture;

	private int[] textureIDs;

	private int[] mFrameBuffers;
	private int[] mFrameBufferTextures;

	private int width;
	private int height;

	public CannyRender(Context context) {
		this.context = context;
		vertexArray = new VertexArray(CUBE);
		flipVertexArray = new VertexArray(FlipCUBE);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		gaussianProgram = new GaussianProgram(context);
		sobelProgram = new BaseSampleProgram(context, R.raw.base_sample_vertex_shader, R.raw.canny_sobel_step_fragment_shader);
		nonmaximumSuppressionProgram = new BaseProgram(context, R.raw.simple_texture_vertex_shader, R.raw.nonmaximumsuppression_fragment_shader);
		weakPixelInclusionProgram = new BaseSampleProgram(context, R.raw.base_sample_vertex_shader, R.raw.weakpixelinclusion_fragment_shader);

		originTexture = TextureHelper.loadTexture(context, R.drawable.lena);

		textureIDs = new int[]{originTexture};
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		glViewport(0, 0, width, height);

		this.width = width;
		this.height = height;

		mFrameBuffers = new int[FRAMEBUFFER_SIZE];
		mFrameBufferTextures = new int[FRAMEBUFFER_SIZE];

		for(int i = 0; i < FRAMEBUFFER_SIZE; i++){
			GLES20.glGenFramebuffers(1, mFrameBuffers, i);
			GLES20.glGenTextures(1, mFrameBufferTextures, i);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mFrameBufferTextures[i]);
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

			GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffers[i]);
			GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
					GLES20.GL_TEXTURE_2D, mFrameBufferTextures[i], 0);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
			GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
		}
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		glClear(GL_COLOR_BUFFER_BIT);

		//render to framebuffer0,gaussian and gray
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffers[0]);
		GLES20.glClearColor(0, 0, 0, 0);

		gaussianProgram.useProgram();
		gaussianProgram.setUniforms(textureIDs[0], 1.0f / width, 1.0f / height);

		vertexArray.setVertexAttribPointer(
				0,
				gaussianProgram.getPositionAttributeLocation(),
				POSITION_COMPONENT_COUNT,
				STRIDE);

		vertexArray.setVertexAttribPointer(
				POSITION_COMPONENT_COUNT,
				gaussianProgram.getTextureCoordinatesAttributeLocation(),
				TEXTURE_COORDINATES_COMPONENT_COUNT,
				STRIDE);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

		//render to framebuffer1, sobel
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffers[1]);
		GLES20.glClearColor(0, 0, 0, 0);

		sobelProgram.useProgram();
		sobelProgram.setUniforms(mFrameBufferTextures[0], 1.0f / width, 1.0f / height);

		vertexArray.setVertexAttribPointer(
				0,
				sobelProgram.getPositionAttributeLocation(),
				POSITION_COMPONENT_COUNT,
				STRIDE);

		vertexArray.setVertexAttribPointer(
				POSITION_COMPONENT_COUNT,
				sobelProgram.getTextureCoordinatesAttributeLocation(),
				TEXTURE_COORDINATES_COMPONENT_COUNT,
				STRIDE);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

		//render to framebuffer2, NonmaximumSuppression
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffers[2]);
		GLES20.glClearColor(0, 0, 0, 0);

		nonmaximumSuppressionProgram.useProgram();
		nonmaximumSuppressionProgram.setUniforms(mFrameBufferTextures[1], 1.0f / width, 1.0f / height);

		vertexArray.setVertexAttribPointer(
				0,
				nonmaximumSuppressionProgram.getPositionAttributeLocation(),
				POSITION_COMPONENT_COUNT,
				STRIDE);

		vertexArray.setVertexAttribPointer(
				POSITION_COMPONENT_COUNT,
				nonmaximumSuppressionProgram.getTextureCoordinatesAttributeLocation(),
				TEXTURE_COORDINATES_COMPONENT_COUNT,
				STRIDE);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

		//render to screen,weakPixelInclusion
		weakPixelInclusionProgram.useProgram();
		weakPixelInclusionProgram.setUniforms(mFrameBufferTextures[2], 1.0f / width, 1.0f / height);

		flipVertexArray.setVertexAttribPointer(
				0,
				weakPixelInclusionProgram.getPositionAttributeLocation(),
				POSITION_COMPONENT_COUNT,
				STRIDE);

		flipVertexArray.setVertexAttribPointer(
				POSITION_COMPONENT_COUNT,
				weakPixelInclusionProgram.getTextureCoordinatesAttributeLocation(),
				TEXTURE_COORDINATES_COMPONENT_COUNT,
				STRIDE);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
	}
}
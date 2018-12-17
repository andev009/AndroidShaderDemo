package com.andev.androidshaderdemo.record.filter;


import android.content.Context;
import android.opengl.GLES20;

import com.andev.androidshaderdemo.R;
import com.andev.androidshaderdemo.data.VertexArray;
import com.andev.androidshaderdemo.programs.GrayTextureProgram;
import com.andev.androidshaderdemo.util.TextureHelper;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static com.andev.androidshaderdemo.Constants.BYTES_PER_FLOAT;

public class BaseImageFilter {
	private static final int POSITION_COMPONENT_COUNT = 2;
	private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
	private static final int STRIDE = (POSITION_COMPONENT_COUNT
			+ TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT;

	public static final float CUBE[] = {
			-1.0f, -1.0f, 0f, 0f,
			1.0f, -1.0f, 1f, 0f,
			-1.0f, 1.0f, 0f, 1f,
			1.0f, 1.0f, 1f, 1f,
	};

	Context context;
	VertexArray vertexArray;
	GrayTextureProgram grayTextureProgram;

	private int texture;

	public BaseImageFilter(Context context){
		this.context = context;
		vertexArray = new VertexArray(CUBE);
		grayTextureProgram = new GrayTextureProgram(context);
		texture = TextureHelper.loadTexture(context, R.drawable.lena);
	}

	public void onDrawFrame(final int textureId) {
		glClear(GL_COLOR_BUFFER_BIT);

		grayTextureProgram.useProgram();
		grayTextureProgram.setUniforms(textureId);

		vertexArray.setVertexAttribPointer(
				0,
				grayTextureProgram.getPositionAttributeLocation(),
				POSITION_COMPONENT_COUNT,
				STRIDE);

		vertexArray.setVertexAttribPointer(
				POSITION_COMPONENT_COUNT,
				grayTextureProgram.getTextureCoordinatesAttributeLocation(),
				TEXTURE_COORDINATES_COMPONENT_COUNT,
				STRIDE);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
	}
}
package com.andev.androidshaderdemo.filter;


import android.content.Context;
import android.opengl.GLES20;

import com.andev.androidshaderdemo.data.VertexArray;
import com.andev.androidshaderdemo.programs.SplitScreenProgram;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static com.andev.androidshaderdemo.Constants.BYTES_PER_FLOAT;

public class SplitScreenFilter extends BaseFilter {

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
	SplitScreenProgram spliteSreenProgram;


	public SplitScreenFilter(Context context) {
		this.context = context;
		vertexArray = new VertexArray(CUBE);
		spliteSreenProgram = new SplitScreenProgram(context);
	}

	@Override
	public void onDrawFrame(final int textureId) {
		glClearColor (0.0f, 1.0f, 0.0f, 0.0f);
		glClear(GL_COLOR_BUFFER_BIT);

		spliteSreenProgram.useProgram();
		spliteSreenProgram.setUniforms(textureId);

		vertexArray.setVertexAttribPointer(
				0,
				spliteSreenProgram.getPositionAttributeLocation(),
				POSITION_COMPONENT_COUNT,
				STRIDE);

		vertexArray.setVertexAttribPointer(
				POSITION_COMPONENT_COUNT,
				spliteSreenProgram.getTextureCoordinatesAttributeLocation(),
				TEXTURE_COORDINATES_COMPONENT_COUNT,
				STRIDE);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
	}
}
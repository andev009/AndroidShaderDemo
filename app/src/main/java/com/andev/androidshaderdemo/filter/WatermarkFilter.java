package com.andev.androidshaderdemo.filter;


import android.content.Context;
import android.opengl.GLES20;

import com.andev.androidshaderdemo.data.VertexArray;
import com.andev.androidshaderdemo.programs.WatermarkProgram;
import com.andev.androidshaderdemo.util.TextTextureHelper;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_DST_ALPHA;
import static android.opengl.GLES20.GL_SRC_ALPHA;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glViewport;
import static com.andev.androidshaderdemo.Constants.BYTES_PER_FLOAT;

public class WatermarkFilter{
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

	private int width;
	private int height;
	private String text;
	private int textSize;

	private int texture;

	Context context;
	VertexArray vertexArray;
	WatermarkProgram watermarkProgram;

	public WatermarkFilter(Context context, int width, int height, String text, int textSize){
		this.width = width;
		this.height = height;
		this.text = text;
		this.textSize = textSize;

		this.context = context;
		vertexArray = new VertexArray(CUBE);
		watermarkProgram = new WatermarkProgram(context);

		texture = TextTextureHelper.createTexture(text, width, height, textSize);
		//texture = TextureHelper.loadTexture(context, R.drawable.lena);
	}


	public void onDrawFrame() {
		glViewport(0, 0, width, height);


		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_DST_ALPHA);

		watermarkProgram.useProgram();
		watermarkProgram.setUniforms(texture);

		vertexArray.setVertexAttribPointer(
				0,
				watermarkProgram.getPositionAttributeLocation(),
				POSITION_COMPONENT_COUNT,
				STRIDE);

		vertexArray.setVertexAttribPointer(
				POSITION_COMPONENT_COUNT,
				watermarkProgram.getTextureCoordinatesAttributeLocation(),
				TEXTURE_COORDINATES_COMPONENT_COUNT,
				STRIDE);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

		GLES20.glDisable(GLES20.GL_BLEND);
	}
}

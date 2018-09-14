package com.andev.androidshaderdemo.programs;


import android.content.Context;
import com.andev.androidshaderdemo.R;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniform1i;

public class TwoFilterOneProgram extends ShaderProgram{
	private final int []uTextureUnitLocation;

	private final int texelWidthOffsetLocation;
	private final int texelHeightOffsetLocation;

	private final int aPositionLocation;
	private final int aTextureCoordinatesLocation;

	public TwoFilterOneProgram(Context context) {
		super(context, R.raw.two_filter_one_vertex_shader, R.raw.two_filter_one_fragment_shader);

		aPositionLocation = glGetAttribLocation(program, A_POSITION);
		aTextureCoordinatesLocation = glGetAttribLocation(program, A_TEXTURE_COORDINATES);


		texelWidthOffsetLocation = glGetUniformLocation(program, TexelWidthOffset);
		texelHeightOffsetLocation = glGetUniformLocation(program, TexelHeightOffset);

		uTextureUnitLocation = new int[1];

		for(int i = 0; i < uTextureUnitLocation.length; i++){
			uTextureUnitLocation[i] = glGetUniformLocation(program, U_TEXTURE_UNIT + i);
		}
	}

	public void setUniforms(int [] textureIDs, float widthFactor, float heightFactor) {
		for(int i = 0; i < textureIDs.length; i++){
			glActiveTexture(GL_TEXTURE0  + i);
			glBindTexture(GL_TEXTURE_2D, textureIDs[i]);
			glUniform1i(uTextureUnitLocation[i], i);
		}

		glUniform1f(texelWidthOffsetLocation, widthFactor);
		glUniform1f(texelHeightOffsetLocation, heightFactor);
	}

	public int getPositionAttributeLocation() {
		return aPositionLocation;
	}

	public int getTextureCoordinatesAttributeLocation() {
		return aTextureCoordinatesLocation;
	}
}

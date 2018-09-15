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

public class SharpShaderProgram extends ShaderProgram{
	private final int []uTextureUnitLocation;
	private final int uStrength;
	private final int uWidthFactor;
	private final int uHeightFactor;

	private final int aPositionLocation;
	private final int aTextureCoordinatesLocation;

	public SharpShaderProgram(Context context) {
		super(context, R.raw.base_sample_vertex_shader, R.raw.sharp_fragment_shader);

		aPositionLocation = glGetAttribLocation(program, A_POSITION);
		aTextureCoordinatesLocation = glGetAttribLocation(program, A_TEXTURE_COORDINATES);

		uStrength = glGetUniformLocation(program, U_STRENGTH);
		uWidthFactor = glGetUniformLocation(program, U_WIDTH_FACTOR);
		uHeightFactor = glGetUniformLocation(program, U_HEIGHT_FACTOR);

		uTextureUnitLocation = new int[2];

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

		glUniform1f(uWidthFactor, widthFactor);
		glUniform1f(uHeightFactor, heightFactor);

		glUniform1f(uStrength, widthFactor);
	}

	public int getPositionAttributeLocation() {
		return aPositionLocation;
	}

	public int getTextureCoordinatesAttributeLocation() {
		return aTextureCoordinatesLocation;
	}
}

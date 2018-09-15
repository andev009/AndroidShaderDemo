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

public class BaseSampleProgram extends ShaderProgram {
	private final int uTextureUnitLocation;
	private final int uStrength;
	private final int uWidthFactor;
	private final int uHeightFactor;

	private final int aPositionLocation;
	private final int aTextureCoordinatesLocation;

	public BaseSampleProgram(Context context) {
		super(context, R.raw.base_sample_vertex_shader, R.raw.base_sample_fragment_shader);

		aPositionLocation = glGetAttribLocation(program, A_POSITION);
		aTextureCoordinatesLocation = glGetAttribLocation(program, A_TEXTURE_COORDINATES);

		uStrength = glGetUniformLocation(program, U_STRENGTH);
		uWidthFactor = glGetUniformLocation(program, U_WIDTH_FACTOR);
		uHeightFactor = glGetUniformLocation(program, U_HEIGHT_FACTOR);

		uTextureUnitLocation =  glGetUniformLocation(program, U_TEXTURE_UNIT);
	}

	public BaseSampleProgram(Context context, int vertexShader, int fragmentShader){
		super(context, vertexShader, fragmentShader);

		aPositionLocation = glGetAttribLocation(program, A_POSITION);
		aTextureCoordinatesLocation = glGetAttribLocation(program, A_TEXTURE_COORDINATES);

		uStrength = glGetUniformLocation(program, U_STRENGTH);
		uWidthFactor = glGetUniformLocation(program, U_WIDTH_FACTOR);
		uHeightFactor = glGetUniformLocation(program, U_HEIGHT_FACTOR);

		uTextureUnitLocation =  glGetUniformLocation(program, U_TEXTURE_UNIT);
	}

	public void setUniforms(int textureId, float widthFactor, float heightFactor) {
		// Set the active texture unit to texture unit 0.
		glActiveTexture(GL_TEXTURE0);

		// Bind the texture to this unit.
		glBindTexture(GL_TEXTURE_2D, textureId);

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

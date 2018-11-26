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

public class AlphaTextureProgram extends ShaderProgram{
	private final int uTextureUnitLocation;

	private final int aPositionLocation;
	private final int uMvpMatrixLocation;
	private final int aTextureCoordinatesLocation;
	private final int uAlphaLocation;


	public AlphaTextureProgram(Context context){
		super(context, R.raw.scale_animation_vertex_shader, R.raw.alpha_texture_fragment_shader);

		uTextureUnitLocation =  glGetUniformLocation(program, U_TEXTURE_UNIT);
		aPositionLocation = glGetAttribLocation(program, A_POSITION);
		uMvpMatrixLocation = glGetUniformLocation(program, U_MVPMATRIX);
		aTextureCoordinatesLocation = glGetAttribLocation(program, A_TEXTURE_COORDINATES);
		uAlphaLocation = glGetUniformLocation(program, U_ALPHA);
	}

	public void setUniforms(int textureId, float alpha) {
		// Set the active texture unit to texture unit 0.
		glActiveTexture(GL_TEXTURE0);

		// Bind the texture to this unit.
		glBindTexture(GL_TEXTURE_2D, textureId);

		// Tell the texture uniform sampler to use this texture in the shader by
		// telling it to read from texture unit 0.
		glUniform1i(uTextureUnitLocation, 0);

		glUniform1f(uAlphaLocation, alpha);
	}

	public int getPositionAttributeLocation() {
		return aPositionLocation;
	}

	public int getTextureCoordinatesAttributeLocation() {
		return aTextureCoordinatesLocation;
	}

	public int getMvpMatrixLocation() {
		return uMvpMatrixLocation;
	}

	public int getAlphaLocation() {
		return uAlphaLocation;
	}
}
